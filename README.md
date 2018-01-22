# radar

Clojure interface to the Ubiquiti UniFi API, with a primary purpose of reading
information about WiFi devices in the area and emitting related events to
Amazon AWS SQS.

## Installation

Clone the repo. Run `lein uberjar`.

The `recognized-devices` config option should be a map of device name to a
color to indicate on the Aurora, such as:
`{"Device Name" {:hue 0, :saturation 100, :brightness 100}}`

## Usage

    $ java -jar radar-0.1.0.jar [args]

## License

Copyright © 2018 Max Burkhardt

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
