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

package com.aheidelbacher.algostorm.data

import org.junit.Test

import com.aheidelbacher.algostorm.data.TileSet.Builder.Companion.tileSet
import com.aheidelbacher.algostorm.data.TileSet.Tile.Frame
import com.aheidelbacher.algostorm.data.TileSet.Tile.Builder.Companion.tile
import com.aheidelbacher.algostorm.engine.driver.Resource
import com.aheidelbacher.algostorm.engine.driver.Resource.Companion.SCHEMA
import com.aheidelbacher.algostorm.engine.serialization.Deserializer.Companion.readValue
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver
import com.aheidelbacher.algostorm.engine.serialization.JsonDriver.Companion.FORMAT

import java.io.ByteArrayOutputStream

import kotlin.test.assertEquals

class TileSetTest {
    private val jsonDriver = JsonDriver()
    private val inputStream = Resource("$SCHEMA/tileSet.$FORMAT").inputStream()
    private val tileSet = tileSet {
        name = "world"
        tileWidth = 24
        tileHeight = 24
        image(Resource("$SCHEMA/image.png"), 288, 240)
        +tile {
            id = 1
            +Frame(1, 250)
            +Frame(2, 250)
        }
        +tile {
            id = 2
        }
    }

    @Test fun testTileSetDeserialization() {
        val actualTileSet = jsonDriver.readValue<TileSet>(inputStream)
        assertEquals(tileSet, actualTileSet)
    }

    @Test fun testTileSetSerialization() {
        val bos = ByteArrayOutputStream()
        jsonDriver.writeValue(bos, tileSet)
        val actualTileSet = jsonDriver.readValue<TileSet>(
                src = bos.toByteArray().inputStream()
        )
        assertEquals(tileSet, actualTileSet)
    }
}