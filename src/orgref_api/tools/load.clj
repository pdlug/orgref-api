(ns orgref-api.tools.load
  "Load the OrgRef CSV into the embedded database"
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as sql]
            [clojure.string :as string]
            [orgref-api.db :as db])
  (:gen-class :main true))

(defn- remove-blank-values
  "Remove blank values from the map"
  [m]
  (into {} (remove (comp string/blank? second) m)))

(defn csv-to-db-record [record]
  "Format fields in record from a CSV for addition to the DB"
  (conj (remove-blank-values record)
    {:id (Integer/parseInt (:id record))}))

(def csv-keys
  "Fields in the order they appear in the CSV file"
  [:name :country :state :level :wikipedia :wikidata :viaf :isni :website :id])

(defn line-to-record [line]
  "Convert a single line in a CSV file to a record"
  (zipmap csv-keys line))

(defn load-csv-file [filename]
  "Import a CSV file of OrgRef records into the DB"
  (with-open [in-file (io/reader filename)]
    (doall
      (map #(-> (line-to-record %) csv-to-db-record db/add-organization)
           (rest (csv/read-csv in-file))))))

(defn -main
  "Main function"
  [& args]
  (db/create-tables)
  (let [filename (first args)]
    (load-csv-file filename)))
