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

package com.aheidelbacher.algostorm.test.event

import org.junit.Ignore
import org.junit.Test

import com.aheidelbacher.algostorm.core.event.Request

import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@Ignore
abstract class RequestTest<T> {
    protected abstract val request: Request<T>

    protected abstract val value: T

    @Test fun testIsNotCompleted() {
        assertFalse(request.isCompleted)
    }

    @Test fun testIsCompletedAfterComplete() {
        request.complete(value)
        assertTrue(request.isCompleted)
    }

    @Test fun testGetReturnsEqualValueAfterComplete() {
        request.complete(value)
        assertEquals(value, request.get())
    }

    @Test(expected = IllegalStateException::class)
    fun testGetNotCompletedShouldThrow() {
        request.get()
    }

    @Test(expected = IllegalStateException::class)
    fun testCompleteMultipleTimesThrows() {
        request.complete(value)
        request.complete(value)
    }
}