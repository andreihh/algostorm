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

package com.andreihh.algostorm.core.drivers.io

import com.andreihh.algostorm.core.drivers.io.Resource.Companion.SCHEMA
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ResourceTest {
    @Test fun `test invalid schema throws`() {
        assertFailsWith<IllegalArgumentException> {
            Resource<Any>("res:/path")
        }
    }

    @Test fun `test relative path throws`() {
        assertFailsWith<IllegalArgumentException> {
            Resource<Any>("${SCHEMA}path")
        }
    }

    @Test fun `test directory path throws`() {
        assertFailsWith<IllegalArgumentException> {
            Resource<Any>("$SCHEMA/path/")
        }
    }

    @Test fun `test multiple file separators throws`() {
        assertFailsWith<IllegalArgumentException> {
            Resource<Any>("$SCHEMA/path//file")
        }
    }
}
