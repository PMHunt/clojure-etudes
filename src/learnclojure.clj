(ns learnclojure)

(require '[clojure.repl :refer [doc]])

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
  (println (str "Make me a set bitch. Use " x " and " y))
  (set (vector x y)))

(my-set 'a 'bq)

(def somens [1 2 3 3 4 5])

(apply + somens)
