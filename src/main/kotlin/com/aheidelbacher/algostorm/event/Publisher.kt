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

package com.aheidelbacher.algostorm.event

/**
 * Provides functionality to post events and notify subscribers.
 *
 * It should preserve the order of submitted events (if an event A is posted
 * before an event B, then subscribers will be notified for A before they are
 * notified for B).
 */
interface Publisher {
    /**
     * Posts the given [event] and notifies all subscribers which subscribed at
     * this publisher for this `event` type.
     *
     * @param T the type of the event
     * @param event the event that should be posted
     */
    fun <T : Event> post(event: T): Unit

    /**
     * Calls [post] for each given event.
     *
     * @param events the events that should be posted
     */
    fun post(events: List<Event>) {
        events.forEach { post(it) }
    }

    /**
     * Calls [post] for each given event.
     *
     * @param events the events that should be posted
     */
    fun post(vararg events: Event) {
        events.forEach { post(it) }
    }
}
