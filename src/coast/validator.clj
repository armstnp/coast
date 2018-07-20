; TODO this namespace has moved to validation. Delete in the next version (eta)
(ns coast.validator
  (:require [jkkramer.verily :as v]
            [coast.utils :as utils]
            [clojure.string :as string]
            [coast.error :refer [raise]]))

(defn fmt-validation [result]
  (let [{:keys [keys msg]} result]
    (map #(hash-map % (str (utils/humanize %) " " msg)) keys)))

(defn fmt-validations [results]
  (when (some? results)
    (->> (map fmt-validation results)
         (flatten)
         (into {}))))

(defn validate [validations m]
  (let [errors (-> (v/validate m validations)
                   (fmt-validations))]
    (if (empty? errors)
      m
      (raise (str "Invalid data: " (string/join ", " (keys errors)))
             {:type :invalid
              :errors errors
              ::error :validation}))))
