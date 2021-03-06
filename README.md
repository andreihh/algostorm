## Algostorm

[![](https://jitpack.io/v/andreihh/algostorm.svg)](https://jitpack.io/#andreihh/algostorm)
[![Build Status](https://travis-ci.org/andreihh/algostorm.svg)](https://travis-ci.org/andreihh/algostorm)
[![codecov](https://codecov.io/gh/andreihh/algostorm/branch/master/graph/badge.svg)](https://codecov.io/gh/andreihh/algostorm)
[![License](http://img.shields.io/:license-apache-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

Algostorm is a 2d grid-based game engine for the JVM. It is designed for
turn-based games, but can be adapted for real-time with a little effort.

### Features
* decoupled engine drivers (audio, 2d graphics, input, scripting, serialization)
* engine that runs on its own private thread
* an entity-system framework
* event-based inter-system communication along with an annotation processor to
type-check the event subscribers
* default physics, in-game timers, graphics, JSON serialization and animation
utilities

### Using Algostorm
To use the engine, you need the following dependencies:
* kotlin-stdlib-1.0.5 or newer
* kotlin-reflect-1.0.5 or newer
* jackson-module-kotlin-2.7.7 or newer

You may download the latest release [here](https://github.com/andrei-heidelbacher/algostorm/releases).

Alternatively, you may fetch the jar along with all its dependencies remotely
from [jitpack](https://jitpack.io). The github repository can be found
[here](https://github.com/andrei-heidelbacher/algostorm).

### Licensing
The engine is released under the Apache V2.0 License.
