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

;; Explore data a bit more, how many observations per day?

(def date-frequencies
  (sort-by first (frequencies (map first covid-world-data-converted))) )


;; Helper functions
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(defn take-countries [data country-set]
  (filter (fn [[_ code]] (country-set code)) data))

(defn date-freqs [data]
  (sort-by first (frequencies (map first data))))

(defn country-freqs [data]
  (sort-by first (frequencies (map second data))))

(def my-countries
  (country-freqs
   (take-countries  covid-world-data-converted
                    #{"IT" "FR" "ES" "CN" "US" "GB" "DE"})))

;; do some acsii plots

(import 'com.mitchtalmadge.asciidata.graph.ASCIIGraph)

;; Get the results from GB
;; just the confirmed cases
;; from the first reported case (ignore the initial zero reports)

(def gb-confirmed
  (drop-while zero?
              (map #(nth % 6)
                   (take-countries covid-world-data-converted #{"GB"}))))

(def gb-deaths
  (drop-while zero?
              (map #(nth % 7)
                   (take-countries covid-world-data-converted #{"GB"}))))

;; use this to print gb-deaths
(println
 (.plot (ASCIIGraph/fromSeries (double-array gb-deaths))))

;; We are interested in growth of  cases,
;; not the absolute numbers.
;; So use the logarithm of this function

;; A logarithm helper function

(defn logarithm ^double [^double x]
  (Math/log x))

(println (.plot (ASCIIGraph/fromSeries
                 (double-array (map logarithm gb-deaths)))))

;; Okay, now we can see the growth over time.

                                        ; Make a helper function from this plotting code

(defn log-plot
  ""
  [series-data]
  (println
   (.plot (ASCIIGraph/fromSeries
           (double-array (map logarithm series-data))))))

;; Plotting other countries
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

(defn confirmed-cases
  "Extracts sequence of confirmed cases for a specified country.
  Observations before the first case was reported are not included
  Arguments:
  Data source as sequence of vectors with confirmed as integer values,
  Country code as a string
  Returns: Sequence of confirmed cases as integer values"

  [data-source country-code]

  (drop-while zero? (map #(nth % 3)
                         (take-countries data-source #{country-code}))) )

;; Explicitly plotting the change
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

;; Show the range of changes of confirmed cases

(defn absolute-plot [series-data]
  (.plot (ASCIIGraph/fromSeries
          (double-array series-data))))

(defn abolute-series
  [data-source country-code]
  (map #(/ % 1000)
       (reduce (fn [acc x]
                 (conj acc (- x (peek acc))))
               [0]
               (confirmed-cases data-source country-code))))

(println
 (absolute-plot
  (abolute-series covid-world-data-converted "CN")))
