(ns joy.chess)

;; represent a chessboard and placement of pieces on it

(defn initial-board []
  [\r \n \b \q \k \b \n \r
   \p \p \p \p \p \p \p \p
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \- \- \- \- \- \- \- \-
   \P \P \P \P \P \P \P \P
   \R \N \B \Q \K \B \N \R] )

(def ^:dynamic *file-key* \a)
(def ^:dynamic *rank-key* \0)

(defn- file-component [file]
  "calculate horizontal projection of a file"
  (- (int file) (int *file-key*)))

(defn- rank-component [rank]
  "calculate vertical projection of a rank"
  (->>  (int *rank-key*)
        (- (int rank))
        (- 8)
        (* 8)))

(defn- index [file rank]
  "project the 1d layout onto a 2d chessboard"
  (+ (file-component file) (rank-component rank)))

(defn lookup [board pos]
  "destructure the standard notation into rank and file and lookup square"
  (let [[file rank] pos]
  ;  (println file)
  ;  (println rank)
    (board (index file rank))))
