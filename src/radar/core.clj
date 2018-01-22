(ns radar.core
  (:gen-class)
  (:require [clojure.data :refer [diff]]
            [clj-time.core :as coretime]
            [clj-time.local :as localtime]
            [clj-time.coerce :as coercetime])
  (:use [radar.unifi]))

(defn get-wifi-client-names
  "Get the human-readable names for connected devices"
  []
  (map (fn [client] (get client "hostname"))
       (filter (fn [client] (>= (get client "last_seen")
                                (- (int (/ (coercetime/to-long (localtime/local-now)) 1000)) 300)))
                 (clients))))

(defn -main
  "Monitor wifi clients attached to UniFi and emit Aurora events"
  [& args]
  (loop [seen (get-wifi-client-names)]
    (let [latest (get-wifi-client-names)]
      (do
        (Thread/sleep 5000)
        ;; Get the hosts that are only in the latest sweep
        (println (get (diff seen latest) 1))
        (recur latest)))))
