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

package algostorm.graphics2d

import algostorm.ecs.EntityManager
import algostorm.ecs.EntitySystem
import algostorm.event.Subscriber

/**
 * A system which handles the rendering of all entities in the game.
 *
 * When a [Render] event is received, the [RenderingEngine.render] method is
 * called.
 *
 * @property renderingEngine the engine that will render the game entities to
 * the screen
 * @property entityManager an entity manager which can be queried to fetch
 * renderable entities
 */
class RenderingSystem(
        private val renderingEngine: RenderingEngine,
        private val entityManager: EntityManager,
        private val properties: Map<String, Any>
) : EntitySystem {
    companion object {
        /**
         * The name of property used by this system.
         */
        const val TILE_COLLECTION: String = "tileCollection"
    }

    private val tileCollection: TileCollection
        get() = (properties[TILE_COLLECTION] as? TileCollection)
                ?: error("Missing $TILE_COLLECTION property!")

    private val renderHandler = Subscriber(Render::class) { event ->
        renderingEngine.render(tileCollection, entityManager.entities)
    }

    /**
     * This system handles [Render] events.
     */
    override val handlers: List<Subscriber<*>> = listOf(renderHandler)
}
