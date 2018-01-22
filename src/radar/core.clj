(ns radar.core
  (:gen-class)
  (:require [clj-time.local :as localtime]
            [clj-time.coerce :as coercetime])
  (:use [radar.unifi]))

(defn get-wifi-client-names
  "Get the human-readable names for connected devices"
  []
  (set (map (fn [client] (get client "hostname"))
       (filter (fn [client] (>= (get client "last_seen")
                                (- (int (/ (coercetime/to-long (localtime/local-now)) 1000)) 300)))
                 (clients)))))

(defn -main
  "Monitor wifi clients attached to UniFi and emit Aurora events"
  [& args]
  (loop [seen (get-wifi-client-names)]
    (let [latest (get-wifi-client-names)]
      ;; Get the hosts that are only in the latest sweep
      (println (clojure.set/difference latest seen))
      ;; Avoid crushing the UniFi server
      (Thread/sleep 5000)
      (recur latest))))
