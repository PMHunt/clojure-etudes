(ns clojure-workshop
  (:require [clojure.string :as str]))


;; Activity 1.02
(defn co2-estimate
  "Crude estimate of CO2 ppm/year"
  [year]
  (let [year-diff #(- % 2006)]
    (+ 382 (* 2 (year-diff year)))))

;; Activity 1.03
(defn meditate
  "Become one with the great god Boole"
  [s calmness-level]
  (println "Clojure Meditate 2.0")
  (println s calmness-level)
  (if (< calmness-level 5)
    (println (str (str/upper-case s) "I TELL YA!!!"))
    (if (<= 5 calmness-level 9)
      (println (str/capitalize s))
      (if (= 10 calmness-level)
        (println (str/reverse s))
        (println "wrong number")))))
    )
