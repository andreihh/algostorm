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

package com.andreihh.algostorm.core.engine

import com.andreihh.algostorm.core.engine.Engine.Status
import com.andreihh.algostorm.test.drivers.audio.AudioDriverStub
import com.andreihh.algostorm.test.drivers.graphics2d.GraphicsDriverStub
import com.andreihh.algostorm.test.drivers.input.InputDriverStub
import com.andreihh.algostorm.test.drivers.io.FileSystemDriverStub
import com.andreihh.algostorm.test.drivers.ui.UiDriverStub
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

/**
 * An abstract test class for an [Engine].
 *
 * In order to test common functionality to all engines, you may implement this
 * class and provide a concrete engine instance to test.
 */
class EngineTest {
    companion object {
        /** The timeout used for stopping and shutting down the engine. */
        const val TIMEOUT: Int = 500
    }

    /** The engine instance that should be tested. */
    private val engine = Engine(Platform(
            AudioDriverStub(),
            GraphicsDriverStub(),
            InputDriverStub(),
            FileSystemDriverStub(),
            UiDriverStub()
    ))

    @Before fun init() {
        engine.init(emptyMap())
    }

    @Test fun testStartAndInstantStop() {
        assertEquals(Status.STOPPED, engine.status)
        engine.start()
        assertEquals(Status.RUNNING, engine.status)
        engine.stop(TIMEOUT)
        assertEquals(Status.STOPPED, engine.status)
    }

    @Test fun testStartAndInstantShutdown() {
        engine.start()
        engine.stop(TIMEOUT)
        engine.release()
    }

    @Test(expected = IllegalStateException::class)
    fun testStartTwiceShouldThrow() {
        engine.start()
        engine.start()
    }

    @Test(expected = IllegalStateException::class)
    fun testReleaseTwiceShouldThrow() {
        engine.stop(TIMEOUT)
        engine.release()
        engine.release()
    }

    @Test(expected = IllegalStateException::class)
    fun testStopMultipleTimesShouldThrow() {
        engine.start()
        engine.stop(TIMEOUT)
        engine.stop(TIMEOUT)
    }

    @Test fun testRunOneSecondThenReleaseShouldNotThrow() {
        engine.start()
        Thread.sleep(1000)
        engine.stop(TIMEOUT)
        engine.release()
    }
}
