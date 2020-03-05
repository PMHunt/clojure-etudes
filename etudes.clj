(ns clojure-etudes.core
  (:import (java.text MessageFormat)))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn onlycaps
  "Takes a string and returns only the capital letters"
  [s]
  (filter #(Character/isUpperCase %) s))

(require 'examples.introduction)

(def rnd "new instance of Java thingy" (new java.util.Random))

(import '(java.util Random Locale)
        '(java.text MessageFormat))

(defn is-small? [number]
  (if (< number 100)
    "Yes"
    (do
      (println "big" number)
      "No")))

(defn countdown [target]
  (loop [result [] x target]
    (if (zero? x)
      result
      (recur (conj result x) (dec x)))))

(defn indexed [coll] (map-indexed vector coll))


(defn last-cell [x]
  (list (last x)))

(defn next-to-last [x]
  (nth (reverse x) 1))
