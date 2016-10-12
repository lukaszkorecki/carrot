(defproject carrot "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [clj-statsd "0.4.0"]
                 [http-kit "2.1.18"]
                 [cheshire "5.6.1"]
                 [overtone/at-at "1.2.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [log4j/log4j "1.2.17"
                  :exclusions [javax.mail/mail
                               javax.jms/jms
                               com.sun.jdmk/jmxtools
                               com.sun.jmx/jmxri]]]
  :main ^:skip-aot carrot.core
  :target-path "target/%s"
  :profiles { :uberjar {:aot :all}})
