(ns learnclojure
  (:use clojure.repl))

;;; This is a port of a Common Lisp program, so state is going to be an issue

(defn make-board []
  "Represent the board as a vector with nine cells, padded vs input off-by-one.
This should probably be an atom or something, but we'll worry about that later"
  (atom (vector 'board 0 0 0 0 0 0 0 0 0)))

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

; for REPL testing, need a board
(def b (make-board))

(defn make-move [player pos board]
  "This function returns a new board state, so need to figure the Clojure way"
  (swap! board #(assoc % pos player)))

;; In Lisp I'd use defparameter, not sure what that would be in Clojure
;; These constants are used to represent X and O numerically for scoring
;; Clojure is unhappy when I use asterisks to denote global constants
(def computer 10)
(def opponent 1)
