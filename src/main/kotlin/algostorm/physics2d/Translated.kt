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

package algostorm.physics2d

import algostorm.ecs.EntityId
import algostorm.event.Event

/**
 * An event which signals that the given entity has translated by the given amount.
 *
 * Only the [PositioningSystem] should post this event.
 *
 * @property entityId the id of the translated entity
 * @property dx the amount the entity translated on the x-axis
 * @property dy the amount the entity translated on the y-axis
 */
data class Translated internal constructor(val entityId: EntityId, val dx: Int, val dy: Int) : Event
