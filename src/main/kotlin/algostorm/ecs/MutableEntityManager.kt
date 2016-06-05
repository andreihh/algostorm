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
 * A mutable view of an [EntityManager] that manages the creation and deletion
 * of all [entities] in the game.
 *
 * Entities should not be created manually, but through a mutable entity manager.
 */
interface MutableEntityManager : EntityManager {
    override val entities: Sequence<MutableEntity>

    override operator fun get(entityId: Int): MutableEntity?

    override fun <T : Component> filterEntities(
            type: KClass<T>
    ): Sequence<MutableEntity> =
            entities.filter { type in it }

    override fun filterEntities(
            vararg types: KClass<out Component>
    ): Sequence<MutableEntity> =
            entities.filter { entity -> types.all { type -> type in entity } }

    /**
     * Creates an entity with the given [components] and adds it to this
     * manager.
     *
     * The generated id must be unique among all other entities in this manager.
     *
     * @param components the components the created entity must contain
     * @return the created entity
     * @throws IllegalStateException if there are too many entities in this
     * manager
     */
    fun create(components: Iterable<Component>): MutableEntity

    /**
     * Creates an entity with the given id and [components] and adds it to this
     * manager.
     *
     * @param entityId the id the created entity must have
     * @param components the components the created entity must contain
     * @return the created entity
     * @throws IllegalStateException if there are too many entities in this
     * manager
     */
    fun create(entityId: Int, components: Iterable<Component>): MutableEntity

    /**
     * Removes the given entity from this manager.
     *
     * @param entityId the id of the entity to be deleted
     * @return `true` if the entity was successfully deleted, or `false` if it
     * doesn't exist in this manager
   */
    fun delete(entityId: Int): Boolean

    /**
     * Deletes all the entities in the manager.
     */
    fun clear() {
        entities.toList().forEach { delete(it.id) }
    }
}
