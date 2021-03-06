/*
 * Copyright (c) 2017  Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at:
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.andreihh.algostorm.core.event

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.util.LinkedList

/**
 * An event bus which allows a [Subscriber] to [subscribe] and [unsubscribe]
 * from certain topics and allows to [post] an event to the bus or make a
 * [request] and notify its subscribers.
 */
class EventBus {
    private val eventQueue = LinkedList<Event>()
    private val subscribers =
            hashMapOf<Subscriber, List<Pair<Method, Class<*>>>>()

    private fun Method.validateHandler() {
        require(Modifier.isFinal(modifiers)) { "$name is not final!" }
        require(returnType.name == "void") {
            "$name doesn't return Unit/void!"
        }
        require(parameterTypes.size == 1) {
            "$name doesn't receive a single parameter!"
        }
        val parameterType = parameterTypes[0]
        require(Event::class.java.isAssignableFrom(parameterType)
                || Request::class.java.isAssignableFrom(parameterType)) {
            "$name doesn't receive an Event or Request as parameter!"
        }
        require(typeParameters.isEmpty()) { "$name is a generic method!" }
        require(parameterType.typeParameters.isEmpty()) {
            "$name receives a generic parameter!"
        }
    }

    private fun <T : Any> publish(value: T) {
        for ((subscriber, handlers) in subscribers) {
            for ((handler, parameterType) in handlers) {
                if (parameterType.isInstance(value)) {
                    handler.invoke(subscriber, value)
                }
            }
        }
    }

    /**
     * Registers the given `subscriber` to this event bus.
     *
     * @param subscriber the object that subscribes for events posted to this
     * event bus
     * @throws IllegalArgumentException if the `subscriber` contains an
     * annotated handler that does not conform to the [Subscribe] contract.
     * However, if any non-public or static method is annotated, it will be
     * ignored instead of throwing an exception.
     */
    fun subscribe(subscriber: Subscriber) {
        val handlers = subscriber.javaClass.methods.filter {
            it.isAnnotationPresent(Subscribe::class.java)
        }.apply { forEach { it.validateHandler() } }
        subscribers[subscriber] = handlers.map {
            it to it.parameterTypes[0]
        }
    }

    /**
     * Unregisters the given `subscriber` from this event bus.
     *
     * @param subscriber the object that should be unsubscribed from events
     * posted to this event bus
     */
    fun unsubscribe(subscriber: Subscriber) {
        subscribers.remove(subscriber)
    }

    /**
     * Posts the given `event` and notifies all subscribers.
     *
     * This should be an asynchronous method and return before the `event` was
     * handled by its subscribers.
     *
     * @param event the event that should be posted
     */
    fun post(event: Event) {
        eventQueue.add(event)
    }

    /**
     * Immediately publishes the given `request` and returns its result.
     *
     * @param T the result type
     * @param request the request which should be completed
     * @return the result with which the `request` was completed
     * @throws IllegalStateException if the `request` was not completed or if it
     * was completed more than once
     */
    fun <T> request(request: Request<T>): T {
        publish(request)
        return request.get()
    }

    /**
     * Blocks until all posted events have been handled by their subscribers.
     */
    fun publishPosts() {
        while (eventQueue.isNotEmpty()) {
            publish(eventQueue.remove())
        }
    }
}
