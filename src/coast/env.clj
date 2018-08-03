(ns coast.env
  (:require [clojure.string :as string]
            [clojure.java.io :as io]
            [coast.utils :as utils]))

(defn fmt [m]
  (->> (map (fn [[k v]] [(-> k .toLowerCase (utils/kebab) keyword) v]) m)
       (into {})))

(defn dot-env []
  "Environment variables all come from .env, specify it on prod, specify it on dev, live a happy life"
  (let [file (io/file ".env")]
    (if (.exists file)
      (->> (slurp file)
           (string/split-lines)
           (map #(string/split % #"="))
           (into {}))
      {})))

(def env
  (-> (merge (dot-env) (System/getenv))
      (fmt)))
