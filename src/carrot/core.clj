(ns carrot.core
  (:gen-class)
  (require [cheshire.core :as json]
           [org.httpkit.client :as http]
           [clj-statsd :as statsd]))

(def rabbit-port (or
                  (System/getenv "RABBIT_PORT")
                  15672))

(def rabbit-host (or
                  (System/getenv "RABBIT_HOST")
                  "192.168.34.11"))

(def rabbit-user (or
                  (System/getenv "RABBIT_USER")
                  "rabbit"))

(def rabbit-pass (or
                  (System/getenv "RABBIT_PASS")
                  "p4ssw0rd"))


(defn rabbit-api-url [path]
  (format "http://%s:%s/%s" rabbit-host rabbit-port path))

(defn get-stats [endpoint]
  (let [resp (http/get (rabbit-api-url endpoint)
                       {:headers
                        {"Accept" "application/json"
                         "Content-Type" "application/json"}

                        :basic-auth [rabbit-user rabbit-pass]
                        })]

    (-> @resp
        :body
        (json/parse-string true))))

(defn queue-stats []
  (get-stats  "api/queues"))

(defn queue-metrics []
  (map #(select-keys % [:name :messages]) (queue-stats)))

(defn server-stats []
  (get-stats "api/nodes"))

(defn server-metrics []
  (-> (server-stats)
      first ;; FIXME ?
      (select-keys [:sockets_total
                    :sockets_used
                    :fd_total
                    :fd_used])))


(defn report-metrics []
  (let [qm (queue-metrics)
        sm (server-metrics)]

    (for [m qm])))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))
