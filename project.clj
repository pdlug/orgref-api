(defproject orgref-api "0.1.0-SNAPSHOT"
  :description "Experimental OrgRef API"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.3.4"]
                 [org.clojure/data.csv "0.1.2"]
                 [org.clojure/java.jdbc "0.3.7"]
                 [com.h2database/h2 "1.4.187"]
                 [korma "0.4.2"]
                 [ring/ring-defaults "0.1.5"]
                 [ring/ring-json "0.3.1"]]
  :plugins [[lein-ring "0.8.13"]]
  :ring {:handler orgref-api.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring-mock "0.1.5"]]}
   :uberjar {:aot :all}})
