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

package com.aheidelbacher.algostorm.engine.physics2d

import com.aheidelbacher.algostorm.engine.physics2d.Rigid.isRigid
import com.aheidelbacher.algostorm.engine.state.Object
import com.aheidelbacher.algostorm.engine.state.ObjectManager
import com.aheidelbacher.algostorm.event.Publisher
import com.aheidelbacher.algostorm.event.Subscribe
import com.aheidelbacher.algostorm.event.Subscriber

/**
 * A system that handles [TransformIntent] events and publishes [Transformed]
 * and [Collision] events.
 *
 * @property objectManager the object manager used to retrieve and update the
 * objects
 * @property publisher the publisher used to post `Transformed` and `Collision`
 * events
 */
class PhysicsSystem(
        private val objectManager: ObjectManager,
        private val publisher: Publisher
) : Subscriber {
    companion object {
        /**
         * Transforms this object with the given amounts.
         *
         * @param dx the translation amount on the x-axis
         * @param dy the translation amount on the y-axis
         * @param rotate the rotation amount in radians
         */
        fun Object.transform(dx: Int, dy: Int, rotate: Float) {
            x += dx
            y += dy
            rotation += rotate
        }

        /**
         * Returns whether the two objects overlap (that is, there exists a
         * pixel `(x, y)` such that it lies inside both objects).
         *
         * @param other the object with which the intersection is checked
         * @return `true` if the two objects overlap, `false` otherwise
         */
        fun Object.overlaps(other: Object): Boolean =
                x < other.x + other.width && x + width > other.x &&
                        y < other.y + other.height && y + height > other.y
    }

    /**
     * Upon receiving a [TransformIntent] event, the the object is transformed
     * by the indicated amount. If the moved object is [Rigid] and there are any
     * other `Rigid` objects with their boxes overlapping the destination
     * location, the object is restored to it's initial location and a
     * [Collision] event is triggered with every overlapping object, having this
     * object as the source and each other object as the target.
     *
     * @param event the [TransformIntent] event
     */
    @Subscribe fun handleTranslateIntent(event: TransformIntent) {
        objectManager[event.objectId]?.let { obj ->
            obj.transform(event.dx, event.dy, event.rotate)
            val overlappingObjects = objectManager.objects.filter {
                it != obj && it.isRigid && it.overlaps(obj)
            }
            if (!obj.isRigid || overlappingObjects.count() == 0) {
                publisher.post(Transformed(
                        objectId = event.objectId,
                        dx = event.dx,
                        dy = event.dy,
                        rotate = event.rotate
                ))
            } else {
                obj.transform(-event.dx, -event.dy, -event.rotate)
                overlappingObjects.forEach {
                    publisher.post(Collision(event.objectId, it.id))
                }
            }
        }
    }
}