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
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

import com.aheidelbacher.algostorm.engine.tiled.Layer.ImageLayer
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup
import com.aheidelbacher.algostorm.engine.tiled.Layer.ObjectGroup.DrawOrder.TOP_DOWN
import com.aheidelbacher.algostorm.engine.tiled.Layer.TileLayer
import com.aheidelbacher.algostorm.engine.tiled.Properties.Color
import com.aheidelbacher.algostorm.engine.tiled.Properties.File
import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType

import kotlin.collections.Map

/**
 * An abstract layer in the game world.
 *
 * @property name the name of this layer
 * @property visible whether this layer should be rendered or not
 * @property opacity the opacity of this layer
 * @property offsetX the x-axis rendering offset in pixels
 * @property offsetY the y-axis rendering offset in pixels
 * @throws IllegalArgumentException if [opacity] is not in the range `0..1`
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes(
        JsonSubTypes.Type(value = TileLayer::class, name = "tilelayer"),
        JsonSubTypes.Type(value = ImageLayer::class, name = "imagelayer"),
        JsonSubTypes.Type(value = ObjectGroup::class, name = "objectgroup")
)
sealed class Layer(
        val name: String,
        var visible: Boolean = true,
        val opacity: Float = 1F,
        @JsonProperty("offsetx") val offsetX: Int = 0,
        @JsonProperty("offsety") val offsetY: Int = 0
) : MutableProperties {
    final override val properties: MutableMap<String, Any> = hashMapOf()

    @JsonProperty("propertytypes")
    final override val propertyTypes: MutableMap<String, PropertyType> =
            hashMapOf()

    init {
        require(0F <= opacity && opacity <= 1F) {
            "$name opacity $opacity must be in the range 0..1!"
        }
    }

    /**
     * Two layers are equal if and only if they have the same name.
     *
     * @param other the layer with which equality is checked
     * @return `true` if the two layers have the same name, `false` otherwise
     */
    final override fun equals(other: Any?): Boolean =
            other is Layer && name == other.name

    final override fun hashCode(): Int = name.hashCode()

    final override fun toString(): String = "${javaClass.simpleName}($name)"

    /**
     * A layer which consists of `width x height` tiles, where `width` and
     * `height` are the dimensions of the containing [Map].
     *
     * @property data the global ids of the tiles on the containing map. Index
     * `i` of this array represents the tile with `x = i % width` and
     * `y = i / width`.
     */
    class TileLayer(
            name: String,
            val data: LongArray,
            visible: Boolean = true,
            opacity: Float = 1F,
            @JsonProperty("offsetx") offsetX: Int = 0,
            @JsonProperty("offsety") offsetY: Int = 0
    ) : Layer(name, visible, opacity, offsetX, offsetY)

    /**
     * A layer which consists of a single [image].
     *
     * @param image the URI of the image that should be rendered
     */
    class ImageLayer(
            name: String,
            var image: File,
            visible: Boolean = true,
            opacity: Float = 1F,
            @JsonProperty("offsetx") offsetX: Int = 0,
            @JsonProperty("offsety") offsetY: Int = 0
    ) : Layer(name, visible, opacity, offsetX, offsetY)

    /**
     * A layer which contains a set of [objects].
     *
     * @property objects the set of objects contained by this layer
     * @property drawOrder indicates the order in which the objects should be
     * rendered
     * @property color the color with which objects that have their `gid` set to
     * `0` will be filled
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ObjectGroup(
            name: String,
            val objects: MutableList<Object>,
            @JsonProperty("draworder") val drawOrder: DrawOrder = TOP_DOWN,
            val color: Color? = null,
            visible: Boolean = true,
            opacity: Float = 1F,
            @JsonProperty("offsetx") offsetX: Int = 0,
            @JsonProperty("offsety") offsetY: Int = 0
    ) : Layer(name, visible, opacity, offsetX, offsetY) {
        /**
         * The order in which objects are rendered.
         */
        enum class DrawOrder {
            @JsonProperty("topdown") TOP_DOWN,
            @JsonProperty("index") INDEX
        }
    }
}
