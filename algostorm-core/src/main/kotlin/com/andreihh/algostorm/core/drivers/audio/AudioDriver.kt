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

package com.andreihh.algostorm.core.drivers.audio

import com.andreihh.algostorm.core.drivers.Driver
import com.andreihh.algostorm.core.drivers.io.Resource
import com.andreihh.algostorm.core.drivers.io.InvalidResourceException

/** A driver that offers audio services. */
interface AudioDriver : Driver, MusicPlayer, SoundPlayer {
    /**
     * Synchronously loads the given audio stream `resource`, making it
     * available to future calls of [playMusic] and [playSound].
     *
     * If the same resource is loaded multiple times, this method has no effect.
     *
     * @param resource the sound resource which should be loaded
     * @throws InvalidResourceException if any error occurs when parsing and
     * loading the `resource`
     */
    fun loadAudioStream(resource: Resource<AudioStream>)
}
