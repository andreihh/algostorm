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

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class KotlinScriptDriver : ScriptDriver {
    private val procedures = hashMapOf<String, KFunction<Unit>>()
    private val functions = hashMapOf<String, KFunction<*>>()

    override fun loadProcedure(procedure: KFunction<Unit>) {
        procedures[procedure.name] = procedure
    }

    override fun <T> loadFunction(function: KFunction<T>) {
        functions[function.name] = function
    }

    override fun invokeProcedure(procedureName: String, vararg args: Any?) {
        requireNotNull(procedures[procedureName]).call(*args)
    }

    override fun <T : Any> invokeFunction(
            functionName: String,
            returnType: KClass<T>,
            vararg args: Any?
    ): T? = returnType.java.cast(
            requireNotNull(functions[functionName]).call(*args)
    )

    override fun release() {
        procedures.clear()
        functions.clear()
    }
}