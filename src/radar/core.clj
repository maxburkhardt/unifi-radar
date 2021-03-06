(ns radar.core
  (:gen-class)
  (:require [clj-time.local :as localtime]
            [clj-time.coerce :as coercetime]
            [cheshire.core :refer :all]
            [outpace.config :refer [defconfig]]
            [cemerick.bandalore :as sqs])
  (:use [radar.unifi]))

;; Configuration for the devices to recognize
(defconfig ^:required recognized-devices)
(defconfig ^:required sqs-queue-url)
(defconfig ^:required aws-client-id)
(defconfig ^:required aws-client-secret)

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
  (let [q sqs-queue-url client (sqs/create-client aws-client-id aws-client-secret)]
    (loop [seen (get-wifi-client-names)]
      (let [latest (get-wifi-client-names)]
        ;; Get the hosts that are only in the latest sweep
        (let [diff (clojure.set/difference latest seen)]
          (map (fn [device] (do (println device) (if (not (nil? (recognized-devices device))) (sqs/send client q (generate-string {:type :wheel :palette (get recognized-devices device) :direction :right}))))) diff))
        ;; Avoid crushing the UniFi server
        (Thread/sleep 5000)
        (recur latest)))))
