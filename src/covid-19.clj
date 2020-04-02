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

;; load the 'world' data into a lazy sequence of vectors
;; how big does dataset need to be before this isn't viable?

(def covid-world (csv->clj "world"))

;; start exploring

(first covid-world)

(second covid-world)

(last covid-world)

(count (rest covid-world))

;; Countries with observations

(count (distinct
        (map #(nth % 2)
             (rest covid-world))))

;; before there's any point in parsing the conf & death field to long,
;; we need to fix blanks in those fields

(def covid-world-data (next covid-world))

(defn fix-blank-conf [v]
  (if (s/blank? (nth v 6)) (assoc v 6 "0") v))

(defn fix-blank-deth [v]
  (if (s/blank? (nth v 7)) (assoc v 7 "0") v))

(defn fix-blanks-c [data]
  (map fix-blank-conf data))

(defn fix-blanks-d [data]
  (map fix-blank-deth data))

;; performance sucked when I tried both maps at once

(def covid-world-data-c (fix-blanks-c covid-world-data))

(def covid-world-data-d (fix-blanks-d covid-world-data-c))

;; now we can cast confirmed and deaths from string to long

(def covid-world-data-converted
  (map (fn [[date k country-code country-name rc rn confirmed death]]
         [date k country-code country-name rc rn (Long/parseLong confirmed) (Long/parseLong death)])
       covid-world-data-d))
