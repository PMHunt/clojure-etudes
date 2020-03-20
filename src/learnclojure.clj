(ns learnclojure)

(require '[clojure.repl :refer [doc]]
         )

(print "Hello World")

(def x "Hello Clojure")

(let [x "Steve"]
  (print "Hello" x))

(if (empty? x)
  "X is emptry"
  "X is not empty")

(if (empty? x)
  nil
  (do
    (println "OK")
    :ok))

(reverse x)

(apply str (reverse x))

(fn [] "Hello, I have less brackets")

(defn hello [name] (str "Hello, " name  " I have less brackets"))

(hello "Phil")

(doc hello)

(defn my-set [x y]
  "JoC2 book version won't make a set using e.g. a a, this work around this"
  (println (str "Make me a set bitch. Use " x " and " y))
  (set (vector x y)))

(my-set 'a 'bq)

;; 4Clojure #29 - return seq of upper case chars only

(= (#(clojure.string/join (re-seq #"[A-Z]" %)) "HeLlO, WoRlD!") "HLOWRD")

;; 4Clojure #30 - remove consecutive dupliates and return

(= (apply str ( #(dedupe (seq %)) "Leeeeeerrroyyy")) "Leroy")

(= ( #(map first (partition-by identity %)) [1 1 2 3 3 2 2 3]) '(1 2 3 2 3))

;; 4Clojure #39 - interleave

(= (#(map first [%1 %2]) [1 2 3] [:a :b :c]) '(1 :a 2 :b 3 :c))
; I think I need to understand 'recur' and/or 'loop' here, vs CAR/CDR recursion
