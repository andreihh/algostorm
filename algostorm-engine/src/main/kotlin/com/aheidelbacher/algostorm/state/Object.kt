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

package com.aheidelbacher.algostorm.state

/**
 * A physical and renderable object in the game.
 *
 * @property id the unique identifier of this object
 * @property name the name of this object
 * @property type the type of this object
 * @property x the x-axis coordinate of the bottom-left corner of this object in
 * pixels
 * @property y the y-axis coordinate of the bottom-left corner of this object in
 * pixels
 * @property width the width of this object in pixels
 * @property width the height of this object in pixels
 * @property isVisible whether this object should be rendered or not
 * @property gid the initial value of the `gid` property
 * @throws IllegalArgumentException if [id], [width] or [height] are not
 * positive or if [gid] is negative
 */
class Object private constructor(
        val id: Int,
        val name: String,
        val type: String,
        var x: Int,
        var y: Int,
        val width: Int,
        val height: Int,
        var isVisible: Boolean,
        gid: Long
) : MutableProperties {
    companion object {
        /** Utility factory method. */
        operator fun invoke(
                id: Int,
                name: String = "",
                type: String = "",
                x: Int,
                y: Int,
                width: Int,
                height: Int,
                isVisible: Boolean = true,
                gid: Long = 0L,
                properties: Map<String, Property> = emptyMap()
        ) = Object(id, name, type, x, y, width, height, isVisible, gid)
                .apply { this.properties.putAll(properties) }
    }

    /** Factory that handles associating unique ids to created objects. */
    interface Factory {
        /**
         * Creates an object with the specified parameters.
         *
         * @param name the name of the created object
         * @param type the type of the created object
         * @param x the horizontal coordinate in pixels of the top-left corner
         * of the created object
         * @param y the vertical coordinate in pixels of the top-left corner of
         * the created object (positive is down)
         * @param width the width in pixels of the created object
         * @param height the height in pixels of the created object
         * @param isVisible whether the created object should be rendered or not
         * @param gid the global id of the object tile. A value of `0` indicates
         * the empty tile (nothing to draw)
         * @throws IllegalStateException if too many objects were created
         * @throws IllegalArgumentException if [gid] is negative or if [width]
         * or [height] are not positive
         */
        fun create(
                name: String = "",
                type: String = "",
                x: Int,
                y: Int,
                width: Int,
                height: Int,
                isVisible: Boolean = true,
                gid: Long = 0L,
                properties: Map<String, Property> = emptyMap()
        ): Object
    }

    override val properties: MutableMap<String, Property> = hashMapOf()

    /**
     * The global tile id of this object.
     *
     * @throws IllegalArgumentException if the set value is negative
     */
    var gid: Long = gid
        set(value) {
            require(value >= 0L) { "Object $id gid $gid can't be negative!" }
            field = value
        }

    init {
        require(id > 0) { "Object id $id must be positive!" }
        require(gid >= 0L) { "Object $id gid $gid can't be negative!" }
        this.properties.putAll(properties)
    }

    /**
     * Two objects are equal if and only if they have the same [id].
     *
     * @param other the object with which equality is checked
     * @return `true` if the two objects have the same [id], `false` otherwise
     */
    override fun equals(other: Any?): Boolean =
            other is Object && id == other.id

    override fun hashCode(): Int = id

    override fun toString(): String = "Object($id)"
}