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

package com.aheidelbacher.algostorm.test.engine.serialization

import com.aheidelbacher.algostorm.engine.serialization.SerializationDriver

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

import kotlin.reflect.KClass

class SerializationDriverStub : SerializationDriver {
    override val format: String = "txt"
    override fun <T : Any> readValue(src: InputStream, type: KClass<T>): T =
            throw IOException()
    override fun writeValue(out: OutputStream, value: Any) {}
    override fun release() {}
}
