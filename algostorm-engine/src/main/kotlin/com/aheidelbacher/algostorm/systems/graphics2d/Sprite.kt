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

package com.aheidelbacher.algostorm.systems.graphics2d

import com.aheidelbacher.algostorm.state.Color
import com.aheidelbacher.algostorm.state.Component
import com.aheidelbacher.algostorm.state.Entity
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedDiagonally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedHorizontally
import com.aheidelbacher.algostorm.state.TileSet.Tile.Companion.isFlippedVertically

/**
 * A component which contains data required for rendering.
 *
 * @property gid the global id of this tile
 * @property width the width in pixels
 * @property height the height in pixels
 * @property z the rendering priority (sprites with lower `z` will be rendered
 * before sprites with higher `z`)
 * @property isVisible
 * @property offsetX the horizontal rendering offset in pixels
 * @property offsetY the vertical rendering offset in pixels (positive is down)
 * @property color
 * @throws IllegalArgumentException if [gid] is negative or if [width] or
 * [height] are not positive
 */
data class Sprite(
        val width: Int,
        val height: Int,
        val z: Int,
        val gid: Long = 0,
        val isVisible: Boolean = true,
        val offsetX: Int = 0,
        val offsetY: Int = 0,
        val color: Color? = null
) : Component {
    companion object {
        /** The `Sprite` component of this entity. */
        val Entity.sprite: Sprite?
            get() = get(Sprite::class)
    }

    init {
        require(gid >= 0L) { "$this gid must not be negative!" }
        require(width > 0) { "$this width must be positive!" }
        require(height > 0) { "$this height must be positive!" }
    }

    /** Utility flag. */
    val isFlippedDiagonally: Boolean
        get() = gid.isFlippedDiagonally

    /** Utility flag. */
    val isFlippedHorizontally: Boolean
        get() = gid.isFlippedHorizontally

    /** Utility flag. */
    val isFlippedVertically: Boolean
        get() = gid.isFlippedVertically
}