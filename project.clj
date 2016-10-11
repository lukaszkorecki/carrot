(defproject carrot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-statsd "0.4.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.6.1"]]
  :main ^:skip-aot carrot.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
