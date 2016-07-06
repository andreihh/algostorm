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

package algostorm.tiled.adapt

import algostorm.ecs.MutableEntity
import algostorm.ecs.MutableEntityManager
import algostorm.tiled.json.Layer
import algostorm.tiled.json.TiledMap
import algostorm.tiled.json.Object

/**
 * A concrete implementation of a mutable entity manager which wraps a Tiled
 * map. Changes made to the entity database and entity records will be reflected
 * in the associated Tiled map.
 *
 * @param tiledMap the underlying Tiled map
 * @throws IllegalArgumentException if the given [tiledMap] doesn't contain a
 * [Layer.ObjectGroup] with the name [ENTITY_LAYER_NAME]
 */
class MutableEntityManagerAdapter(
        private val tiledMap: TiledMap
) : MutableEntityManager {
    companion object {
        /**
         * The name of the [Layer.ObjectGroup] which contains the game entities.
         */
        const val ENTITY_LAYER_NAME = "entities"
    }

    /**
     * A wrapper over a Tiled object.
     *
     * @param tiledObject the underlying Tiled object
     */
    class MutableEntityAdapter(
            val tiledObject: Object
    ) : MutableEntity(tiledObject.id) {
        companion object {
            const val TYPE: String = "type"
            const val NAME: String = "name"
            const val VISIBLE: String = "visible"
            const val ROTATION: String = "rotation"
            const val GID: String = "gid"
        }

        override operator fun get(property: String): Any? = when (property) {
            TYPE -> tiledObject.type
            NAME -> tiledObject.name
            VISIBLE -> tiledObject.isVisible
            ROTATION -> tiledObject.rotation
            GID -> tiledObject.gid
            else -> tiledObject.properties[property]
        }

        override fun <T : Any> set(property: String, value: T) {
            when (property) {
                TYPE, NAME ->
                    throw IllegalArgumentException("$property is read-only!")
                VISIBLE -> tiledObject.isVisible =
                        requireNotNull(value as? Boolean) {
                            "$VISIBLE property must be of type Boolean!"
                        }
                ROTATION -> tiledObject.rotation =
                        requireNotNull(value as? Float) {
                            "$ROTATION property must be of type Float!"
                        }
                GID -> tiledObject.gid = requireNotNull(value as? Long) {
                    "$GID property must be of type Long!"
                }
                else -> tiledObject.properties[property] = value
            }
        }

        override fun remove(property: String) {
            when (property) {
                TYPE, NAME ->
                    throw IllegalArgumentException("$property is read-only!")
                VISIBLE -> tiledObject.isVisible = false
                ROTATION -> tiledObject.rotation = 0F
                GID -> tiledObject.gid = null
                else -> tiledObject.properties.remove(property)
            }
        }
    }

    private val objectGroup = requireNotNull(tiledMap.layers.find {
        it.name == ENTITY_LAYER_NAME
    } as? Layer.ObjectGroup) { "Tiled map doesn't contain entity layer!" }
    private val entityMap = objectGroup.objects.associateTo(hashMapOf()) {
        it.id to MutableEntityAdapter(it)
    }

    override val entities: Sequence<MutableEntity>
        get() = entityMap.values.asSequence()

    override fun get(entityId: Int): MutableEntity? = entityMap[entityId]

    override fun create(properties: Map<String, Any>): MutableEntity {
        check(tiledMap.nextObjectId >= 0) { "Too many entities!" }
        val id = tiledMap.nextObjectId
        val tiledObject = Object(id)
        val entity = MutableEntityAdapter(tiledObject)
        tiledMap.nextObjectId++
        tiledObject.properties.putAll(properties)
        objectGroup.objects.add(tiledObject)
        entityMap[id] = entity
        return entity
    }

    override fun delete(entityId: Int): Boolean {
        return entityMap[entityId]?.let { entity ->
            entityMap.remove(entityId)
            objectGroup.objects.remove(entity.tiledObject)
            true
        } ?: false
    }
}
