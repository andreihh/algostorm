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

import com.aheidelbacher.algostorm.engine.tiled.Properties.Color
import com.aheidelbacher.algostorm.engine.tiled.Properties.File
import com.aheidelbacher.algostorm.engine.tiled.Properties.PropertyType

interface MutableProperties : Properties {
    override val properties: MutableMap<String, Any>
    override val propertyTypes: MutableMap<String, PropertyType>

    /**
     * Removes the property with the given name.
     *
     * @param name the name of the property that should be removed
     */
    fun remove(name: String) {
        properties.remove(name)
        propertyTypes.remove(name)
    }

    operator fun set(name: String, value: Int) {
        properties[name] = value
        propertyTypes[name] = PropertyType.INT
    }

    operator fun set(name: String, value: Float) {
        properties[name] = value
        propertyTypes[name] = PropertyType.FLOAT
    }

    operator fun set(name: String, value: Boolean) {
        properties[name] = value
        propertyTypes[name] = PropertyType.BOOLEAN
    }

    operator fun set(name: String, value: String) {
        properties[name] = value
        propertyTypes[name] = PropertyType.STRING
    }

    operator fun set(name: String, value: Color) {
        properties[name] = value.color
        propertyTypes[name] = PropertyType.COLOR
    }

    operator fun set(name: String, value: File) {
        properties[name] = value.path
        propertyTypes[name] = PropertyType.FILE
    }
}
