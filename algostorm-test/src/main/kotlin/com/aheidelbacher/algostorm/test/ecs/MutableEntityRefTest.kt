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

package com.aheidelbacher.algostorm.test.ecs

import org.junit.Test

import com.aheidelbacher.algostorm.core.ecs.Component
import com.aheidelbacher.algostorm.core.ecs.MutableEntityRef
import com.aheidelbacher.algostorm.core.ecs.Prefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.emptyPrefab
import com.aheidelbacher.algostorm.core.ecs.Prefab.Companion.prefabOf

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

abstract class MutableEntityRefTest : EntityRefTest() {
    override abstract fun createEntity(prefab: Prefab): MutableEntityRef

    @Test fun testGetComponentAfterRemoveReturnsNull() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(prefab)
        entity.remove(TestComponent::class)
        assertNull(entity[TestComponent::class])
    }

    @Test fun testContainsComponentAfterRemoveReturnsNull() {
        val id = 9
        val prefab = prefabOf(ComponentMock(id), TestComponent(id))
        val entity = createEntity(prefab)
        entity.remove(TestComponent::class)
        assertFalse(TestComponent::class in entity)
    }

    @Test fun testGetComponentAfterSetReturnsEqualComponent() {
        val oldId = 7
        val id = 9
        val prefab = prefabOf(ComponentMock(oldId))
        val entity = createEntity(prefab)
        entity.set(ComponentMock(id))
        assertEquals(ComponentMock(id), entity[ComponentMock::class])
    }

    /*@Test(expected = IllegalStateException::class)
    fun testSetUnregisteredComponentTypeThrows() {
        data class UnregisteredComponent(val id: Int) : Component

        createEntity(emptyPrefab()).set(UnregisteredComponent(1))
    }*/
}