(ns orgref-api.pagination
  (:require [clojure.string :as string]
            [ring.util.request :as req]
            [ring.util.codec :refer [form-encode]]
            [orgref-api.util :refer [assoc-if]]))

(defn has-more-pages?
  "Whether or not the current result set has additional pages"
  [total limit page]
  (> total (* limit page)))

(defn last-page
  "Calculate the last page in a result set"
  [total limit]
  (-> (/ total limit) Math/ceil int))

(defn link-for-page
  "Generate a link for a specific page number and type using the request url"
  [request page rel]
  (let [query-params (assoc (:query-params request) "page" page)
        qs (form-encode query-params)
        link-url (req/request-url (assoc request :query-string qs))]
    (str "<" link-url ">; rel=\"" (name rel) "\"")))

(defn link-map
  "Map of link type and page number"
  [total limit current-page]
  (-> {}
    (assoc-if :first (if (not= current-page 1)
                       1))
    (assoc-if :prev (if (> current-page 1)
                      (dec current-page)))
    (assoc-if :next (if (has-more-pages? total limit current-page)
                      (inc current-page)))
    (assoc-if :last (if (not= (last-page total limit) current-page)
                      (last-page total limit)))))

(defn link-pagination-header
  "Generate the Link header used for response pagination"
  [request total limit current-page]
  (string/join ", "
    (map (fn [[type page]] (link-for-page request page type))
      (link-map total limit current-page))))
