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

(def phone-numbers ["221 610-5007"
                    "221 433-4185"
                    "661 471-3948"
                    "661 653-4480"
                    "661 773-8656"
                    "555 515-0158"])

(defn unique-area-codes
  "using first with higher order function map"
  [numbers]
  (->> numbers
       (map #(str/split % #" "))
       (map first)
       distinct))

;; ex 2.06 nested maps

(def gemstone-db {
                  :ruby {
                         :name "Ruby"
                         :stock 480
                         :sales [1990 6376 4918 7882 6747 7496 8574 6098 2821]
                         :properties {
                                      :hardness 9.0
                                      :refractive-index [1.77 1.78]
                                      :color "Red"}}
                  :sapphire {
                         :name "Sapphire"
                         :stock 480
                         :sales [1990 6376 4918 7882 6747 7496 8574 6098 2821]
                         :properties {
                                      :hardness 9.0
                                      :refractive-index [1.77 1.78]
                                      :color "Blue"}}})

(defn durability
  "takes gem as a param, returns hardness"
  [db gemstone-key]
  (get-in (gemstone-key db) [:properties :hardness]))

(defn change-color
  [db gemstone-key new-color]
  (assoc-in db [gemstone-key :properties :color] new-color))

(defn sell-gem
  [db gemstone-key client-id]
  (let [updated-db (update-in db [gemstone-key :sales] conj client-id ) ]
    (update-in updated-db [gemstone-key :stock] dec)) )

;; Activity 2.01 - in-memory database

(def memory-db (atom {}))

(defn read-db [] @memory-db)

;; just overwrite the db, we'd use swap! if we wanted to apply a # to data
(defn write-db [new-db] (reset! memory-db new-db))

(defn create-table
  [table-name]
  (write-db {table-name {:data [] :indexes {}}}))

(defn drop-table
  [table-name]
  (reset! memory-db (dissoc @memory-db  table-name)))

(defn insert
  "record is #map id-key is a : in the record used as index"
  [table-name record id-key]
  (let [old-db (read-db)
        new-db (update-in old-db [table-name :data] conj record)
        index (- (count (get-in new-db [table-name :data])) 1)]
    (write-db (update-in new-db [table-name :indexes id-key] assoc (id-key record) index))))
