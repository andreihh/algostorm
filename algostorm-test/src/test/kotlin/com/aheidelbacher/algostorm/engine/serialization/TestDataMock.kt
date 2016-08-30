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

package com.aheidelbacher.algostorm.engine.serialization

data class TestDataMock(
        val primitiveTestField: Int,
        val defaultPrimitiveTestField: Float = 1.5F,
        val innerTestData: InnerTestDataMock,
        val testProperties: Map<String, Any> = hashMapOf(),
        val testList: List<Int>
) {
    data class InnerTestDataMock(val testField: String = "")
}