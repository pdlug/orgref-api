(ns orgref-api.tools.query
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [orgref-api.db :as db])
  (:gen-class :main true))

(defn -main
  "Main function"
  [& args]
  ; (println (db/find-by-id 18548241))
  ; (println (db/count-organizations)))
  ; (let [results (db/find-organizations {:name "federal"
                                        ; :country "US"})]
    ; (doall (map println results))))
  (println (db/with-pagination :organizations (db/organization-criteria {:name "fed" :country "US"}))))

  ; (map #(println %) (db/find-by-name "Aichi")))
