(ns orgref-api.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]
            [ring.middleware.defaults :refer [wrap-defaults]]
            [orgref-api.db :as db]
            [orgref-api.routes.organizations :as org-routes]))

(defn home-route []
  {:status 200
   :body {:data {:version "1.0"
                 :name "OrgRef API"}}})

(defroutes app-routes
  (GET "/" [] (home-route))
  (context "/organizations" []
    (defroutes organizations-routes
      (GET "/" request
        (org-routes/get-organizations request))
      (context "/:id" [id]
        (defroutes organization-routes
          (GET "/" [] (org-routes/get-organization id))))))
  (route/not-found "Not Found"))

(def app-defaults
  "Default ring configuration for this app"
  {:params    {:urlencoded true
               :keywordize true}
   :responses {:not-modified-responses true
               :absolute-redirects     true
               :default-charset        "utf-8"}})

(def app
  (->
    (handler/site app-routes)
    (wrap-defaults app-defaults)
    (wrap-json-body {:keywords? true})
    wrap-json-response))
