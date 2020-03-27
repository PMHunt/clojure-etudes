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

(defn print-down-from [x]
  "using recur to make a print loop, think of it as a block with 'when' test.
   Recur takes (dec x) as a parameter and rebinds calling fn param to result,
   it then passes control back to calling fn (or to loop if you need closure)"
  (when (pos? x)
    (println x)
    (recur (dec x))))

(defn sum-down-from [sum x]
  "This time we make an accumulator"
  (if (pos? x)
    (recur (+ sum x) (dec x))
    sum))

(defn loopy-sum-down-from [initial-x]
  "We use loop as a 'let' that's also recur's rebinding target"
  (loop [sum 0 x initial-x]
    (if (pos? x)
      (recur (+ sum x) (dec x))
      sum)))

(def guys-whole-name ["Guy" "Lewis" "Steele"])

(let [[f-name m-name l-name] guys-whole-name]
  (str l-name ", " f-name " " m-name))

;; 4Clojure #49
(= (__ 3 [1 2 3 4 5 6]) [[1 2 3] [4 5 6]])

(defn my-split [n coll]
  [(take n coll) (drop n coll )])

(defrecord recipe [name
                   ingredients
                   ])

(def quiche
  (->recipe "Quiche" '(eggs)))

(def quiche
  (map->recipe {:name "Quiche"
                :ingredients '(eggs buttter milk flour salt)}))
(defn say-hi []
  (println "Hello from thread " (.getName (Thread/currentThread))))

(dotimes [_ 3]
  (.start (Thread. say-hi)))

(.toString x)
;; Java: x.toString()

(.addVector2D v v2)
;; Java: v.addVector2D(v2)

(require '[clojure.string :as str])

(defn ellipsize [words]
  (let [[x y z] (str/split words #"\s+")] ; split on whitespace and destruct.
    (str/join " " [x y z " "])))

(defn punch
  ([name punch-type]
   (str "I " punch-type "-punch " name))
  ([name]
   (punch name "karate")))

(defn codger [& whippersnappers]
  (map (fn [ws] (str "Get off my lawn " ws )) whippersnappers))

(defn coords [[x y & rest]]
  (list x y rest))

(defn receive-treasure-location
  [{:keys [lat lng] :as treasure-location}]
  (println (str "Treasure lat: " lat))
  (println (str "treasure long: " lng))
  treasure-location)

(defn fac [n]
  (loop [cnt n acc 1]
    (if (= cnt 0)
      acc
      (recur (- cnt 1) (* cnt acc)))))


;;; ----- hobbit bothering -----------------------

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}]) ; vector of maps

(defn matching-part
  "use regex against :name to replace left with right"
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts
  "expects seq of maps with :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
      (let [[part & remaining] remaining-asym-parts]
        (recur remaining
               (into final-body-parts
                     (set [part (matching-part part)])))))))

;; can we translate some of the family tree and haunted house code to this idiom?

(defn better-symmetrize-body-parts
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (into final-body-parts (set [part (matching-part part)])))
          []
          asym-body-parts))

(defn bother
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + (map :size sym-parts))
        target (rand body-part-size-sum)]
    (loop [[part & remaining] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur remaining (+ accumulated-size (:size (first remaining))))))))

(defn bother-hobbit
  [asyn-body-parts]
  (println (str "You bother the hobbit in the " (:name (bother asyn-body-parts)))))
