(ns coast.beta
  (:require [coast.middleware :as middleware]
            [coast.router :as router]
            [coast.env :as env]
            [ring.middleware.defaults :as middleware.defaults]
            [coast.server :as server]
            [pyro.printer :as printer]))

(printer/swap-stacktrace-engine!)

(defn app
  ([routes opts]
   (let [{:keys [layout error-fn not-found-fn]} opts]
     (-> (router/match-routes routes not-found-fn)
         (middleware/wrap-layout layout)
         (middleware/wrap-with-logger)
         (middleware.defaults/wrap-defaults (middleware/coast-defaults opts))
         (middleware/wrap-not-found not-found-fn)
         (middleware/wrap-reload)
         (middleware/wrap-if #(= "prod" (env/env :coast-env)) middleware/wrap-errors error-fn))))
  ([routes]
   (app routes {})))

(def start-server server/start)
(def stop-server server/stop)
(def restart-server server/restart)
