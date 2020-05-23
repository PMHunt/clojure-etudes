(ns learnclojure
  (:use clojure.repl))

;;; This is a port of a Common Lisp program, so state is going to be an issue

(defn make-board []
  "Represent the board as a vector with nine cells, padded vs input off-by-one."
  (atom  (vector 'board 0 0 0 0 0 0 0 0 0)))

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
   (print-row (nth @board 1) (nth @board 2) (nth @board 3))
   (println "----------")
   (print-row (nth @board 4) (nth @board 5) (nth @board 6))
   (println "----------")
   (print-row (nth @board 7) (nth @board 8) (nth @board 9))))

; for REPL testing, need a board
(def game-state (make-board))

(defn make-move [player pos board]
  "This function returns a new board state by swap!'ing the atom using assoc"
  (swap! board #(assoc % pos player)))

;; In Lisp I'd use defparameter, not sure what that would be in Clojure
;; These constants are used to represent X and O numerically for scoring
;; Clojure is unhappy when I use asterisks to denote global constants
(def computer 10)
(def opponent 1)
(def triplets
  '([1 2 3] [4 5 6] [7 8 9]
    [1 4 7] [2 5 8] [3 6 9]
    [1 5 9] [3 5 7]))

(defn sum-triplet [board triplet]
  "Need to use @ to get at the current state of board atom"
  (+ (nth @board (nth triplet 0))
     (nth @board (nth triplet 1))
     (nth @board (nth triplet 2))))

(defn compute-sums [board]
  (map (fn [triplet]
         (sum-triplet board triplet))
       triplets))

(defn winner-p [board]
  (let [sums (compute-sums board)]
    (or (some #(= (* 3 computer) %) sums )
        (some #(= (* 3 opponent) %) sums ))))

(defn get-input
  "Waits for user to enter text and hit enter, then cleans the input"
  ([] (get-input ""))
  ([default]
   (let [input (clojure.string/trim (read-line))]
     (if (empty? input) default input))))

(defn parse-int [s]
  (try
    (Integer. s)
    (catch Exception e (str "Invalid input, must be integer"))))

(defn read-a-legal-move [board]
  "dummy data to represent a legal move"
  (println "your move")
  (let [pos (parse-int (get-input))]
    (cond
      (not (and (integer? pos) (<= 1 pos 9)))
      (do (println "Invalid input, integer must be 1-9")
          (read-a-legal-move board))
      (not (= 0 (nth @board pos)))
      (do (println "That space is already occupied")
          (read-a-legal-move board))
      :else pos)))


(defn board-full-p [board])

(defn pick-random-empty-position [board]
  (let [pos (+ 1 (rand-int 9))]
    (if (= 0 (nth @board pos))
      pos
      (pick-random-empty-position board))))

(defn random-move-strategy [board]
  (list (pick-random-empty-position board) "random move"))

(defn choose-best-move [board]
  (random-move-strategy board))


(defn opponent-move [board]
  (println "Cells are numbered left-right from 1-9 pick one")
  (let [pos (read-a-legal-move board)]
    (make-move opponent pos board) ; this is where board state is changed
    (print-board board)
    (cond
      (winner-p board) (println "you win!")
      (board-full-p board) (println "Tied game")
      :else (computer-move board))))


(defn computer-move [board]
  (let [best-move (choose-best-move board)
        pos (first best-move)
        strategy (second best-move)]
    (make-move computer pos board)
    (println "my move" pos)
    (println "my strategy" strategy)
    (print-board board)
    (cond
      (winner-p board) (println "I win!")
      (board-full-p board) (println "Tied game")
      :else (opponent-move board))))


;; still don't understand recur

(defn play-one-game []
  "need to figure out how to do y-or-n-p input loop, meanwhile this ... "
  (let [starter (rand-int 2)]
    (cond
      (= starter 0) (opponent-move (make-board))
      (= starter 1) (computer-move (make-board)))))
