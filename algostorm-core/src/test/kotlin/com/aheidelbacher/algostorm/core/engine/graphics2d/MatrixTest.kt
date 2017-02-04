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

package com.aheidelbacher.algostorm.core.engine.graphics2d

import org.junit.Assert.assertEquals
import org.junit.Test

import com.aheidelbacher.algostorm.test.engine.graphics2d.TOLERANCE
import com.aheidelbacher.algostorm.test.engine.graphics2d.assertEquals

class MatrixTest {
    @Test fun testPostScale() {
        val sx = 5938F
        val sy = -9385F
        val expected = Matrix(
                sx, 0F, 0F,
                0F, sy, 0F
        )
        val actual = Matrix.identity().postScale(sx, sy)
        assertEquals(expected, actual, TOLERANCE)
    }

    @Test fun testPreScale() {
        val sx = 5938F
        val sy = -9385F
        val expected = Matrix(
                sx, 0F, 0F,
                0F, sy, 0F
        )
        val actual = Matrix.identity().preScale(sx, sy)
        assertEquals(expected, actual, TOLERANCE)
    }

    @Test fun testPostTranslate() {
        val dx = 9458F
        val dy = -3948F
        val expected = Matrix(
                1F, 0F, dx,
                0F, 1F, dy
        )
        val actual = Matrix.identity().postTranslate(dx, dy)
        assertEquals(expected, actual, TOLERANCE)
    }

    @Test fun testPostRotate() {
        val degrees = 30F
        val radians = degrees * Math.PI / 180.0
        val cos = Math.cos(radians).toFloat()
        val sin = Math.sin(radians).toFloat()
        val expected = Matrix(
                cos, sin, 0F,
                -sin, cos, 0F
        )
        val actual = Matrix.identity().postRotate(degrees)
        assertEquals(expected, actual, TOLERANCE)
    }

    @Test fun testPostConcat() {
        val dx = 412F
        val dy = -384F
        val degrees = 213F
        val px = 5F
        val py = 8F
        val expected = Matrix.identity()
                .postTranslate(dx, dy)
                .postRotate(degrees, px, py)
        val actual = Matrix.identity()
                .postConcat(Matrix.identity().postTranslate(dx, dy))
                .postConcat(Matrix.identity().postRotate(degrees, px, py))
        assertEquals(expected, actual, TOLERANCE)
    }

    @Test fun testGetByIndex() {
        val matrix = Matrix(
                0F, 1F, 2F,
                3F, 4F, 5F
        )
        for (i in 0..5) {
            assertEquals(1F * i, matrix[i], TOLERANCE)
        }
        assertEquals(0F, matrix[6], TOLERANCE)
        assertEquals(0F, matrix[7], TOLERANCE)
        assertEquals(1F, matrix[8], TOLERANCE)
    }

    @Test fun testGetByRowAndColumn() {
        val matrix = Matrix(
                0F, 1F, 2F,
                3F, 4F, 5F
        )
        val i = 0
        for (r in 0..1) {
            for (c in 0..2) {
                assertEquals(1F * i, matrix[r, c], TOLERANCE)
            }
        }
        assertEquals(0F, matrix[2, 0], TOLERANCE)
        assertEquals(0F, matrix[2, 1], TOLERANCE)
        assertEquals(1F, matrix[2, 2], TOLERANCE)
    }

    @Test(expected = IndexOutOfBoundsException::class)
    fun testGetInvalidIndexThrows() {
        val matrix = Matrix.identity()
        matrix[-1]
    }

    @Test fun testToString() {
        val matrix = Matrix.identity()
        assertEquals(
                "[[1.0, 0.0, 0.0], [0.0, 1.0, 0.0], [0.0, 0.0, 1.0]]",
                "$matrix"
        )
    }
}
