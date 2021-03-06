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

package com.andreihh.algostorm.core.ecs

import com.andreihh.algostorm.core.ecs.EntityRef.Id
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EntityGroupTest {
    private fun createGroup(
            entities: Map<EntityRef.Id, Collection<Component>>
    ): EntityGroup = EntityPool.of(entities).filter { true }

    private lateinit var initialEntities: Map<Id, Collection<Component>>
    private lateinit var group: EntityGroup

    private fun createInitialEntities(): Map<Id, Collection<Component>> =
        (1 until 1000).associate {
            Id(it) to setOf(ComponentMock(it))
        }

    @Before fun initGroup() {
        initialEntities = createInitialEntities()
        group = createGroup(initialEntities)
    }

    @Test fun testGetSnapshotReturnsSameEntities() {
        assertEquals(
            expected = initialEntities,
            actual = group.associate { it.id to it.components.toSet() }
        )
    }

    @Test fun testContainsReturnsTrue() {
        for (id in initialEntities.keys) {
            assertTrue(id in group)
        }
    }

    @Test fun testContainsAbsentReturnsFalse() {
        val maxId = initialEntities.keys.maxBy { it.value }?.value ?: 0
        assertFalse(Id(maxId + 1) in group)
    }

    @Test fun testFilterGroupShouldContainFilteredEntities() {
        val subgroup = group.filter { it.id.value % 2 == 1 }
        assertEquals(
            expected = initialEntities.filter { it.key.value % 2 == 1 },
            actual = subgroup.associate { it.id to it.components.toSet() }
        )
    }

    @Test fun testChangedEntityRemovedFromSubgroup() {
        val subgroup = group.filter {
            val id = it[ComponentMock::class]?.id
            id != null && id % 2 == 1
        }
        group.forEach {
            if (it.id in subgroup) {
                it.remove(ComponentMock::class)
                assertFalse(it.id in subgroup)
            }
        }
    }

    @Test fun testChangedEntityAddedToSubgroup() {
        val subgroup = group.filter {
            val id = it[ComponentMock::class]?.id
            id != null && id % 2 == 0
        }
        group.forEach {
            if (it.id !in subgroup) {
                it.set(ComponentMock(it.id.value - it.id.value % 2))
                assertTrue(it.id in subgroup)
            }
        }
    }
}
