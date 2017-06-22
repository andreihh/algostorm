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

package com.aheidelbacher.algostorm.android.test

import com.aheidelbacher.algostorm.android.AndroidClient
import com.aheidelbacher.algostorm.core.drivers.client.audio.AudioDriver
import com.aheidelbacher.algostorm.core.drivers.client.graphics2d.GraphicsDriver
import com.aheidelbacher.algostorm.core.drivers.client.input.InputDriver
import com.aheidelbacher.algostorm.core.drivers.io.FileSystemDriver
import com.aheidelbacher.algostorm.core.engine.Engine

import java.io.InputStream
import java.io.OutputStream

class MainActivity : AndroidClient() {
    override val splashLayoutId: Int = R.layout.activity_main_splash
    override val contentLayoutId: Int = R.layout.activity_main
    override val surfaceViewContainerLayoutId: Int = R.id.surfaceViewContainer

    override fun createEngine(
            audioDriver: AudioDriver,
            graphicsDriver: GraphicsDriver,
            inputDriver: InputDriver,
            fileSystemDriver: FileSystemDriver
    ): Engine = object : Engine(
            audioDriver = audioDriver,
            graphicsDriver = graphicsDriver,
            inputDriver = inputDriver,
            fileSystemDriver = fileSystemDriver
    ) {
        override val millisPerUpdate: Int = 25
        override fun onError(cause: Exception) {}
        override fun onInit(src: InputStream?) {
            Thread.sleep(2000)
        }
        override fun onStart() {}
        override fun onUpdate() {}
        override fun onStop() {}
        override fun onSerializeState(out: OutputStream) {}
        override fun onRelease() {}
    }
}
