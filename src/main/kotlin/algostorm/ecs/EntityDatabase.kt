/*
 * Copyright 2016 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package algostorm.ecs

import kotlin.reflect.KClass

/**
 * A concrete implementation of a [MutableEntityManager].
 *
 * All entities are kept in-memory in a map.
 */
class EntityDatabase : MutableEntityManager {
    /**
     * A concrete implementation of a [MutableEntity].
     *
     * All components are kept in-memory, within an array.
     */
    class EntityRecord(
            id: Int,
            components: Iterable<Component>
    ) : MutableEntity(id) {
        private companion object {
            private var componentIndex = 0
            private val componentMapper =
                    hashMapOf<KClass<out Component>, Int>()

            fun getIndex(type: KClass<out Component>): Int =
                    componentMapper.getOrPut(type) { componentIndex++ }
        }

        private var fields = run {
            val maxIndex = components.map {
                getIndex(it.javaClass.kotlin)
            }.max() ?: 0
            val fieldsArray = arrayOfNulls<Component>(maxIndex + 1)
            components.forEach {
                fieldsArray[getIndex(it.javaClass.kotlin)] = it
            }
            fieldsArray
        }

        override val components: List<Component>
            get() = fields.filterNotNull()

        override operator fun <T : Component> get(type: KClass<T>): T? =
                getIndex(type).let { index ->
                    if (index < fields.size) type.java.cast(fields[index])
                    else null
                }

        override operator fun <T : Component> set(type: KClass<T>, value: T) {
            getIndex(type).let { index ->
                if (index >= fields.size) {
                    fields = fields.copyOf(index + 1)
                }
                fields[index] = value
            }
        }

        override fun <T : Component> remove(type: KClass<T>) {
            getIndex(type).let { index ->
                if (index < fields.size) {
                    fields[index] = null
                }
            }
        }
    }

    private val entitySet = hashMapOf<Int, EntityRecord>()
    private val nextId: Int = 0
        get() {
            check(entitySet.size < Int.MAX_VALUE) { "Too many entities!" }
            while (field in entitySet) {
                field = if (field == Int.MAX_VALUE) 0 else field + 1
            }
            return field
        }

    override val entities: Sequence<MutableEntity>
        get() = entitySet.values.asSequence()

    override fun get(entityId: Int): MutableEntity? = entitySet[entityId]

    override fun create(components: Iterable<Component>): MutableEntity =
            create(nextId, components)

    override fun create(
            entityId: Int,
            components: Iterable<Component>
    ): MutableEntity {
        require(entityId !in entitySet) { "Entity id is already used!" }
        val entity = EntityRecord(entityId, components)
        entitySet[entityId] = entity
        return entity
    }

    override fun delete(entityId: Int): Boolean =
            entitySet.remove(entityId)?.clear() != null
}