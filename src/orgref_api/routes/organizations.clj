(ns orgref-api.routes.organizations
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [orgref-api.db :as db]
            [orgref-api.util :refer [parse-int]]
            [orgref-api.pagination :refer [link-pagination-header]]))

(defn- org-response
  "Generate the response object for an organization"
  [org]
  {:id (:id org)
   :type "organization"
   :attributes (dissoc org :id)})

(defn offset-for-page
  "Calculate the offset given the limit and current page number"
  [limit page]
  (* limit (dec page)))

(def default-limit
  "Default limit for pagination"
  25)

(def allowed-criteria-fields
  "Fields that may be specified as lookup criteria"
  [:name :country :state :level :wikipedia :wikidata :viaf :isni])

(defn get-organization
  "Route to retrieve a specific organization by ID"
  [id]
  (if-let [id-num (parse-int id)]
    (if-let [org (db/find-by-id id-num)]
      {:status 200
       :body {:data (org-response org)}}
      {:status 404})
    {:status 404
     :body {:errors [{:title "Organization not found"}]}}))

(defn get-organizations
  "Retrieve all organizations (optionally restricted by criteria)"
  [request]
  (let [params (:params request)
        page (parse-int (get-in params [:page] 1))
        limit (parse-int (get-in params [:limit] default-limit))
        offset (offset-for-page limit page)
        criteria (select-keys params allowed-criteria-fields)
        options {:limit limit :offset offset}
        results (db/paginated-query :organizations
                  (db/organization-criteria criteria) options)
        total (:count results)
        orgs (map org-response (:results results))]
    {:status 200
     :headers {"Link" (link-pagination-header request total limit page)}
     :body {:data orgs}}))
