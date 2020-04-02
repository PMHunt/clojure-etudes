(ns PMHunt.covid-19.world
  (:require [clojure.java.io :as io]
            [clojure.data.csv :as csv]
            [clojure.string :as s]))

(def data "https://open-covid-19.github.io/data/data.csv")

;; easier just to load it from filesystem though, so save it to resources and
;; make a function to load it as string using io and then into csv

(defn csv->clj [location]
  (csv/read-csv
   (slurp
    (io/reader
     (format "resources/%s.csv" location)))))

;; load the 'world' data into  var

(def covid-world (csv->clj "world"))
