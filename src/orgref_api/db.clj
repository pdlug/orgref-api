(ns orgref-api.db
  "Database handling"
  (:require [clojure.java.jdbc :as sql]
            [clojure.string :as str])
  (:use [korma.config]
        [korma.core]
        [korma.db]))

(def db-spec
  {:classname "org.h2.Driver"
   :subprotocol "h2"
   :subname "./db/orgref"})

(defdb db
  (h2 (assoc db-spec :naming {:keys str/lower-case
                              :fields str/upper-case})))

(declare organizations)

(defentity organizations
  (pk :id)

  (entity-fields :id :name :country :state :level :wikipedia :wikidata :viaf
                 :isni :website))

(defn create-tables
  "Create tables"
  []
  (sql/db-do-commands db-spec
    (sql/create-table-ddl :organizations
      [:id        "BIGINT" "PRIMARY KEY" "AUTO_INCREMENT"]
      [:name      "VARCHAR(1000)"]
      [:country   "VARCHAR(2)"]
      [:state     "VARCHAR(2)"]
      [:level     "VARCHAR(3)"]
      [:wikipedia "VARCHAR(1024)"]
      [:wikidata  "VARCHAR(1024)"]
      [:viaf      "VARCHAR(1024)"]
      [:isni      "VARCHAR(1024)"]
      [:website   "VARCHAR(1024)"])))

(defn- remove-nils
  "Remove nils from a database result map"
  [org]
  (into {} (remove (comp nil? second) org)))

(defn- format-isni
  "Format an ISNI as a URL (if provided as an ID)"
  [isni]
  (if (.startsWith isni "http:")
    isni
    (str "http://www.isni.org/isni/" isni)))

(defn add-organization
  "Add a new organization to the database"
  [org]
  (insert organizations
    (values org)))

(defn find-by
  "Find an organization by a specific field and value"
  [field value]
  (if-let [org (first
                 (select organizations
                   (where {field value})
                   (limit 1)))]
    (remove-nils org)))

(defn find-by-id
  "Find an organization by ID"
  [id]
  (find-by :id id))

(defn find-by-isni
  "Find an organization by its ISNI"
  [isni]
  (find-by :isni (format-isni isni)))

(defn- criteria-clause-for
  "Format field specific clause"
  [field value]
  (case field
    :name (let [v (str "%" value "%")]
            [(raw "LOWER(name)") ['like v]])
    :isni [field (format-isni value)]
    [field ['= value]]))

(defn organization-criteria
  "Query with restrictions for the criteria"
  [criteria]
  (into {} (for [[k v] criteria] (criteria-clause-for k v))))

(defn count-results
  "Count results for a given set of criteria"
  [table criteria]
  (let [cnt (->
              (select* table)
              (where criteria)
              (aggregate (count :*) :cnt)
              (exec))]
    (get-in (first cnt) [:cnt] 0)))

(defn result-page
  "Retrieve an individual page of results"
  [table criteria l o]
  (map
    remove-nils
    (->
      (select* table)
      (where criteria)
      (limit l)
      (offset o)
      (order :id :ASC)
      (exec))))

(defn paginated-query
  "Execute a query"
  [table criteria & [opts]]
  (let [l (get-in opts [:limit] 25)
        o (get-in opts [:offset] 0)]
    {:count (count-results table criteria)
     :results (result-page table criteria l o)}))
