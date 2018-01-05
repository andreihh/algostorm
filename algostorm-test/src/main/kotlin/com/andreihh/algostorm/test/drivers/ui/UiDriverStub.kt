/*
 * Copyright 2018 Andrei Heidelbacher <andrei.heidelbacher@gmail.com>
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

package com.andreihh.algostorm.test.drivers.ui

import com.andreihh.algostorm.core.drivers.ui.UiListener
import com.andreihh.algostorm.core.drivers.ui.UiDriver
import com.andreihh.algostorm.core.drivers.ui.UiEvent
import kotlin.reflect.KClass

class UiDriverStub : UiDriver {
    override fun setListener(listener: UiListener) {}
    override fun notify(event: UiEvent) {}
    override fun release() {}
}
