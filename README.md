<!--
  ~ Copyright (C) <2015> <Volodymyr Vagner>

  ~ This program is free software: you can redistribute it and/or modify
  ~ it under the terms of the GNU General Public License as published by
  ~ the Free Software Foundation, either version 3 of the License, or
  ~ (at your option) any later version.

  ~ This program is distributed in the hope that it will be useful,
  ~ but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~ MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~ GNU General Public License for more details at <http://www.gnu.org/licenses/>
  -->

Intervals
======

Intervals is a java based command-line tool that searches for time slot of specified duration within specified range.

## Build

By default jar file with all dependencies will be created.

```sh
mvn clean install
```
The resulting jar is *target/intervals.jar*

#Run

When run with no args, default data & configuration will be used

```
java -jar ./intervals.jar
```

Alternatively, you can run application with parameters *-data* and *-request*

```
java -jar ./intervals.jar -data=<path_to_data_file> -request=<path_to_request_file>
```

#Samples

Sample files can be found in *resources/samples* folder.

**config.xml** contains configuration - working hours range
**data.xml** contains actual data, list of people with their time-tables
**request.xml** contains 'request' data - what needs to be found and when

Schemas to verify input manually can be found in *resources/schema*.
Currently application is NOT verifying input basing on schema automatically.