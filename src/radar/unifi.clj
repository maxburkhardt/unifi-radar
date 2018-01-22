(ns radar.unifi
  (:require [clj-http.client :as client]
            [cheshire.core :refer :all]
            [outpace.config :refer [defconfig]])
  (:use [slingshot.slingshot :only [try+]]))

;; Configuration for the UniFi server
(defconfig ^:required unifi-address)
(defconfig ^:required unifi-username)
(defconfig ^:required unifi-password)

(def cs (clj-http.cookies/cookie-store))

(defn get-auth-cookie []
  (client/post (str unifi-address "/api/login")
               {:content-type :json
                :cookie-store cs
                :form-params
                {:username unifi-username
                 :password unifi-password
                 :remember false
                 :strict true}}))

(defn login-if-required [op]
  (try+
    (op)
    (catch [:status 401] {:keys [request-time headers body]}
      (do
        (get-auth-cookie)
        (op)))))

(defn clients []
  (get (parse-string (:body
                       (login-if-required (fn []
                                            (client/get (str unifi-address "/api/s/default/stat/sta")
                                                        {:cookie-store cs}))))) "data"))

(defn events []
  (get (parse-string (:body
                       (login-if-required (fn []
                                            (client/post (str unifi-address "/api/s/default/stat/event")
                                                         {:cookie-store cs
                                                          :content-type :json
                                                          :headers {:X-Csrf-Token
                                                                    (:value (get (clj-http.cookies/get-cookies cs) "csrf_token"))}
                                                          :form-params
                                                          {:_limit 500
                                                           :_start 0
                                                           :within 4320}}))))) "data"))
