(ns clojure-workshop)

;; Activity 1.02

(defn co2-estimate
  "Crude estimate of CO2 ppm/year"
  [year]
  (let [year-diff #(- % 2006)]
    (+ 382 (* 2 (year-diff year)))))
