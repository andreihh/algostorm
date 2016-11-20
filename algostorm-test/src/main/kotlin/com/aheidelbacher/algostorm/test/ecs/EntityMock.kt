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

package com.aheidelbacher.algostorm.test.ecs

import com.aheidelbacher.algostorm.ecs.Component
import com.aheidelbacher.algostorm.ecs.MutableEntity

import kotlin.reflect.KClass

class EntityMock(id: Int) : MutableEntity(id) {
    private val componentMap = hashMapOf<KClass<out Component>, Component>()

    override val components: Collection<Component>
        get() = componentMap.values

    override fun contains(type: KClass<out Component>): Boolean =
            type in componentMap

    override fun <T : Component> get(type: KClass<T>): T? =
            type.java.cast(componentMap[type])

    override fun <T : Component> remove(type: KClass<T>): T? =
            type.java.cast(componentMap.remove(type))

    override fun set(value: Component) {
        componentMap[value.javaClass.kotlin] = value
    }
}