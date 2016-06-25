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

package algostorm.time

/**
 * A container for all the timers in the game.
 */
interface Timeline {
    /**
     * @param timer the timer which should be registered
     */
    fun registerTimer(timer: Timer): Unit

    /**
     * Signals that a tick has passed and all registered timers should be
     * updated.
     *
     * @return the list of all timers that expired upon this tick
     */
    fun tick(): List<Timer>
}
