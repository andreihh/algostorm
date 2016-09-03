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

package com.aheidelbacher.algostorm.engine

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.serialization.Serializer
import com.aheidelbacher.algostorm.test.engine.EngineTest

import java.io.ByteArrayOutputStream
import java.io.OutputStream

class EngineMockTest : EngineTest(EngineMock()) {
    private fun getState(): List<Int> {
        val outputStream = ByteArrayOutputStream()
        engine.serializeState(outputStream)
        val inputStream = outputStream.toByteArray().inputStream()
        return Serializer.readValue<List<Int>>(inputStream)
    }

    override fun getElapsedFrames(): Int = getState().size

    @Test(timeout = MAX_TIME_LIMIT)
    fun testSerializeState() {
        engine.start()
        repeat(1000) {
            val state = getState()
            assertEquals((0 until state.size).toList(), state)
        }
        engine.stop()
    }

    @Test(timeout = MAX_TIME_LIMIT)
    fun testClearState() {
        engine.start()
        Thread.sleep(1000)
        engine.shutdown()
        assertEquals(0, getState().size)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCreateEngineWithZeroMillisPerUpdateShouldThrow() {
        object : Engine(0) {
            override fun clearState() {}
            override fun onHandleInput() {}
            override fun onRender() {}
            override fun onUpdate() {}
            override fun writeStateToStream(outputStream: OutputStream) {}
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testCreateEngineWithNegativeMillisPerUpdateShouldThrow() {
        object : Engine(-25) {
            override fun clearState() {}
            override fun onHandleInput() {}
            override fun onRender() {}
            override fun onUpdate() {}
            override fun writeStateToStream(outputStream: OutputStream) {}
        }
    }
}
