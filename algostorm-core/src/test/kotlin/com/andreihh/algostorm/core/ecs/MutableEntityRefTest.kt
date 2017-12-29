/*
 * Copyright 2017 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.core.ecs

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

abstract class MutableEntityRefTest : EntityRefTest() {
    override abstract fun createEntity(
            components: Collection<Component>
    ): MutableEntityRef

    @Test fun testGetComponentAfterRemoveReturnsNull() {
        val id = 9
        val components = setOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(components)
        entity.remove(TestComponent::class)
        assertNull(entity[TestComponent::class])
    }

    @Test fun testContainsComponentAfterRemoveReturnsNull() {
        val id = 9
        val components = setOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(components)
        entity.remove(TestComponent::class)
        assertFalse(TestComponent::class in entity)
    }

    @Test fun testGetComponentAfterSetReturnsEqualComponent() {
        val oldId = 7
        val id = 9
        val components = setOf(ComponentMock(oldId))
        val entity = createEntity(components)
        entity.set(ComponentMock(id))
        assertEquals(ComponentMock(id), entity[ComponentMock::class])
    }
}