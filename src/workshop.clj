(ns clojure-workshop
  (:require [clojure.string :as str]
            [clojure.repl :refer [doc]]))


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
  (cond
    (< calmness-level 5) (println (str (str/upper-case s) "I TELL YA!!!"))
    (<= 5 calmness-level 9) (println (str/capitalize s))
    (= 10 calmness-level) (println (str/reverse s))
    :else (println "wrong number")))

;; Ex 2.01 The Obfuscation Function

(defn encode-letter
  "Encodes letters by adding word len then squaring their ascii code"
  [s x]
  (let [code (Math/pow (+ x (int (first (char-array s)))) 2)]
    (str "#" (int code))))

(defn encode
  "Encodes string using encode-letter"
  [s]
  (let [number-of-words (count (str/split s #" "))]
    (str/replace s #"\w" (fn [s] (encode-letter s number-of-words)))))

(defn decode-letter
  "cuts out the numeric part of a string, decodes it to a letter"
  [x y]
  (let [number (Integer/parseInt (subs x 1))
        letter (char (- (Math/sqrt number) y))]
    (str letter)))

(defn decode
  "Get word-length and use it to decode letter"
  [s]
  (let [number-of-words (count (str/split s #" "))]
    (str/replace s #"\#\d+" (fn [s] (decode-letter s number-of-words)))))
