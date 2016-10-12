(ns carrot.core
  (:gen-class)
  (require [cheshire.core :as json]
           [org.httpkit.client :as http]
           [clj-statsd :as s]
           [overtone.at-at :as at]))

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

(def statsd-host (or
                  (System/getenv "STATSD_HOST")
                  "127.0.0.1"))

(def statsd-port (or
                  (System/getenv "STATSD_PORT")
                  8125))


(s/setup statsd-host statsd-port)


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

(defn fetch-metrics []
  (let [qm (queue-metrics)
        sm (server-metrics)

        queues (map
                (fn [i]
                  [(format "amqp_queues.%s" (-> i :name name))
                   (-> i :messages)])
                qm)

        stats (map
               (fn [i]
                 [(format "amq_stats.%s" (-> i first name))
                  (last i)])
               sm)]

    (concat queues stats)))

(defn report-metrics [metrics]
  (for [metric metrics]
    (let [[k v] metric]
      (printf ">> %s -> %s\n" k v)
      (s/gauge k v))))

(def scheduler-pool (at/mk-pool))

(def scheduled (atom nil))

(defn start! [cb]
  (reset! scheduled (at/every 5000
                              cb
                              scheduler-pool)))

(defn stop! []
  (at/stop @scheduled))

(defn -main
  [& args]
  (println "let's do this")
  (start! (fn []
            (print ".")
            (report-metrics (fetch-metrics)))))
