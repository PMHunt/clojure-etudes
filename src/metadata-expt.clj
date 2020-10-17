(ns profilable)

(defn ^:bench profile-me [ms] ;
  (println "Crunching bits for" ms "ms")
  (Thread/sleep ms))

(defn dont-profile-me [ms]
  (println "not expecting profiling"))

(ns user)

(defn- wrap [f]
  (fn [& args]
    (time (apply f args))))

(defn- make-profilable [v]
  (alter-var-root v (constantly (wrap @v))))

(defn- tagged-by [tag nsname]
  (->> (ns-publics nsname)
       vals
       (filter #(get (meta %) tag))))

(defn prepare-bench [nsname] ;
  (->> (tagged-by :bench nsname)
       (map make-profilable)
       dorun))

(profilable/profile-me 500) ;
;; Crunching bits for 500 ms

(prepare-bench 'profilable)

(profilable/profile-me 500) ;
;; Crunching bits for 500 ms
;; "Elapsed time: 502.422309 msecs"

(profilable/dont-profile-me 0) ;
;; not expecting profiling
