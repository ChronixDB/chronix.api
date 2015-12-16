[![Build Status](https://travis-ci.org/ChronixDB/chronix.api.svg)](https://travis-ci.org/ChronixDB/chronix.api)
[![Coverage Status](https://coveralls.io/repos/ChronixDB/chronix.api/badge.svg?branch=master&service=github)](https://coveralls.io/github/ChronixDB/chronix.api?branch=master)
[![Stories in Ready](https://badge.waffle.io/ChronixDB/chronix.api.png?label=ready&title=Ready)](https://waffle.io/ChronixDB/chronix.api)
[![Apache License 2](http://img.shields.io/badge/license-ASF2-blue.svg)](https://github.com/ChronixDB/chronix.api/blob/master/LICENSE)

# Chronix API
The Chronix API defines the Chronix Client class that allows one to stream and store data. 
The Chronix Server and the Chronix Storage provides an implementation that is used by the Chronix Client. 

## Usage
Build script snippet for use in all Gradle versions, using the Bintray Maven repository:

```groovy
repositories {
    mavenCentral()
    maven { 
        url "http://dl.bintray.com/chronix/maven" 
    }
}
dependencies {
   compile 'de.qaware.chronix:chronix-api:0.1'
}
```

## Contributing

Is there anything missing? Do you have ideas for new features or improvements? You are highly welcome to contribute
your improvements, to the Chronix projects. All you have to do is to fork this repository,
improve the code and issue a pull request.

## Maintainer

Florian Lautenschlager @flolaut

## License

This software is provided under the Apache License, Version 2.0 license.

See the `LICENSE` file for details.
