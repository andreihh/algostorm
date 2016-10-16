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

package com.aheidelbacher.algostorm.systems.script

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.engine.script.JavascriptDriver
import com.aheidelbacher.algostorm.state.File
import com.aheidelbacher.algostorm.systems.script.ScriptingSystem.RunScript
import com.aheidelbacher.algostorm.systems.script.ScriptingSystem.RunScriptWithResult
import com.aheidelbacher.algostorm.test.engine.script.ScriptResult

import java.io.FileInputStream

class ScriptingSystemTest {
    companion object {
        const val FUNCTION_NAME: String = "getResult"
    }

    val system = ScriptingSystem(
            JavascriptDriver { FileInputStream(java.io.File(it)) },
            listOf(File("src/test/resources/testScript.js"))
    )

    @Test
    fun testRunScript() {
        val id = 5
        val value = "five"
        val event = RunScript(FUNCTION_NAME, id, value)
        system.onRunScript(event)
    }

    @Test
    fun testRunScriptWithResult() {
        val id = 5
        val value = "five"
        var isOk = false
        val event = RunScriptWithResult(
                FUNCTION_NAME,
                ScriptResult::class,
                id,
                value
        ) { isOk = it == ScriptResult(id, value) }
        system.onRunScriptWithResult(event)
        assertEquals(true, isOk)
    }
}