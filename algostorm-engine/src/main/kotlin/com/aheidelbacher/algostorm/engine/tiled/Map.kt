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

package com.aheidelbacher.algostorm.engine.tiled

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

import com.aheidelbacher.algostorm.engine.tiled.Map.RenderOrder.RIGHT_DOWN
import com.aheidelbacher.algostorm.engine.tiled.Properties.Color
import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType
import com.aheidelbacher.algostorm.engine.tiled.TileSet.Tile.Companion.clearFlags

/**
 * A map which contains all the game state.
 *
 * The x-axis is increasing from left to right and the y-axis is increasing from
 * top to bottom.
 *
 * @property width the width of the map in tiles
 * @property height the height of the map in tiles
 * @property tileWidth the width of a tile in pixels
 * @property tileHeight the height of a tile in pixels
 * @property orientation the orientation of the map
 * @property renderOrder the order in which objects and tiles are rendered
 * @property tileSets the tile sets used for rendering
 * @property layers the layers of the game
 * @property properties the properties of this map
 * @property backgroundColor the color of the map background
 * @property version the version of this map
 * @property nextObjectId the next available id for an object
 * @throws IllegalArgumentException if [nextObjectId] is negative or if there
 * are multiple tile sets with the same name or multiple layers with the same
 * name or if [width] or [height] or [tileWidth] or [tileHeight] or [version]
 * are not positive
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
class Map(
        val width: Int,
        val height: Int,
        @JsonProperty("tilewidth") val tileWidth: Int,
        @JsonProperty("tileheight") val tileHeight: Int,
        val orientation: Orientation,
        @JsonProperty("renderdorder") val renderOrder: RenderOrder = RIGHT_DOWN,
        @JsonProperty("tilesets") val tileSets: List<TileSet>,
        val layers: List<Layer>,
        @JsonProperty("backgroundcolor") val backgroundColor: Color? = null,
        val version: Float = 1F,
        @JsonProperty("nextobjectid") private var nextObjectId: Int
) : MutableProperties {
    /**
     * The orientation of the map.
     */
    enum class Orientation {
        @JsonProperty("orthogonal") ORTHOGONAL
    }

    /**
     * The rendering order of tiles.
     */
    enum class RenderOrder {
        @JsonProperty("right-down") RIGHT_DOWN,
        @JsonProperty("right-up") RIGHT_UP,
        @JsonProperty("left-down") LEFT_DOWN,
        @JsonProperty("left-up") LEFT_UP
    }

    override val properties: MutableMap<String, Any> = hashMapOf()

    @JsonProperty("propertytypes")
    override val propertyTypes: MutableMap<String, PropertyType> = hashMapOf()

    @Transient private lateinit var gidToTileSet: Array<TileSet?>
    @Transient private lateinit var gidToTileId: IntArray

    init {
        require(width > 0) { "Map width $width must be positive!" }
        require(height > 0) { "Map height $height must be positive!" }
        require(tileWidth > 0) { "Map tile width $tileWidth must be positive!" }
        require(tileHeight > 0) {
            "Map tile height $tileHeight must be positive!"
        }
        require(nextObjectId >= 0) {
            "Map next object id $nextObjectId can't be negative!"
        }
        require(tileSets.distinct().size == tileSets.size) {
            "Different tile sets can't have the same name!"
        }
        require(layers.distinct().size == layers.size) {
            "Different layers can't have the same name!"
        }
        val totalGidCount = tileSets.sumBy { it.tileCount }
        gidToTileSet = arrayOfNulls<TileSet>(totalGidCount)
        gidToTileId = IntArray(totalGidCount)
        var firstGid = 1
        for (tileSet in tileSets) {
            for (tileId in 0 until tileSet.tileCount) {
                gidToTileSet[tileId + firstGid - 1] = tileSet
                gidToTileId[tileId + firstGid - 1] = tileId
            }
            firstGid += tileSet.tileCount
        }
    }

    /**
     * Returns the next available object id and increments [nextObjectId].
     *
     * @return the next available object id
     * @throws IllegalStateException if there are too many objects
     */
    fun getAndIncrementNextObjectId(): Int {
        check(nextObjectId < Int.MAX_VALUE) { "Too many objects in the map!" }
        val id = nextObjectId
        nextObjectId++
        return id
    }

    /**
     * Returns the tile set which contains the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested tile set
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileSet(gid: Long): TileSet = gidToTileSet[gid.clearFlags() - 1]
            ?: error("Tile set can't be null!")

    /**
     * Returns the local tile id of the given [gid].
     *
     * @param gid the searched global tile id
     * @return the requested local tile id
     * @throws IndexOutOfBoundsException if the given [gid] is not positive or
     * is greater than the total number of tiles contained in the map tile sets
     */
    fun getTileId(gid: Long): Int = gidToTileId[gid.clearFlags() - 1]
}
