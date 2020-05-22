(ns learnclojure
  (:use clojure.repl))

(defn make-board []
  "Represent the board as a vector with nine cells, padded vs input off-by-one"
  (vector 'board 0 0 0 0 0 0 0 0 0))

(defn convert-to-letter [v]
  "State is represented by numbers, but displayed as characters X O and blank"
  (cond (= v 1) "O"
        (= v 10) "X"
        :else " "))

(defn print-row [x y z]
  (println
   (convert-to-letter x) "|"
   (convert-to-letter y) "|"
   (convert-to-letter  z)))

(defn print-board [board]
  "Wrap it in a do so you don't eval nil and get null pointer exception"
  (do
   (print-row (nth board 1) (nth board 2) (nth board 3))
   (println "----------")
   (print-row (nth board 4) (nth board 5) (nth board 6))
   (println "----------")
   (print-row (nth board 7) (nth board 8) (nth board 9))))

(def b (make-board))
