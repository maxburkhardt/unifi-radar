(defproject radar "0.1.0"
  :description "Use the UniFi API to extract information about the WiFi environment"
  :url "https://armada.systems"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-http "3.7.0"]
                 [clj-time "0.14.2"]
                 [cheshire "5.8.0"]
                 [com.cemerick/bandalore "0.0.6"]
                 [com.outpace/config "0.10.0"]
                 [slingshot "0.12.2"]]
  :main ^:skip-aot radar.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
