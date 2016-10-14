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

package com.aheidelbacher.algostorm.engine.script

import java.io.FileNotFoundException

import kotlin.reflect.KClass

/**
 * An interpreter that can evaluate scripts and invoke named functions contained
 * in previously evaluated scripts.
 */
interface ScriptEngine {
    companion object {
        /** Inline utility which delegates to [ScriptEngine.invokeFunction]. */
        inline fun <reified T : Any> ScriptEngine.invokeFunction(
                functionName: String,
                vararg args: Any?
        ): T? = invokeFunction(functionName, T::class, *args)
    }

    /**
     * Executes the script at the given path.
     *
     * Every variable and function declaration in this script should be
     * available to future [invokeFunction] calls.
     *
     * Paths are relative to a specialized location of script resources.
     *
     * @param scriptSource the path where the script is found
     * @throws FileNotFoundException if the given script doesn't exist
     */
    @Throws(FileNotFoundException::class)
    fun eval(scriptSource: String): Unit

    /**
     * Executes the script function with the given [functionName] with the
     * specified arguments and returns its result.
     *
     * @param functionName the name of the script function that should be
     * executed
     * @param returnType the expected type of the result
     * @param args the script function parameters
     * @return the script result, or `null` if it doesn't return anything.
     * @throws IllegalArgumentException if the given [functionName] is not
     * available to this engine
     * @throws ClassCastException if the result couldn't be converted to the
     * [returnType]
     */
    fun <T : Any> invokeFunction(
            functionName: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T?
}
