# radar

Clojure interface to the Ubiquiti UniFi API, with a primary purpose of reading
information about WiFi devices in the area and emitting related events to
Amazon AWS SQS. It is designed to work with the Aurora utilities here:
https://github.com/maxburkhardt/aurora.

## Installation

Clone the repo. Run `lein uberjar`.

In the repo directory, run `lein run -m outpace.config.generate` to generate a
config file with the required options. Fill those in based on your environment
and desires.

The `recognized-devices` config option should be a map of device name to a
color to indicate on the Aurora, such as:
`{"Device Name" {:hue 0, :saturation 100, :brightness 100}}`

## Usage

    $ java -jar radar-0.1.0.jar

## License

Copyright © 2018 Max Burkhardt

Distributed under the Eclipse Public License version 1.0.
