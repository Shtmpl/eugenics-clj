(ns eugenics.main
  (:require [eugenics.visualisation :as visualisation]
            [eugenics.application :as application]))

(defn random-int [from to]
  (+ (rand-int (- to from)) from))

(defn random-points [[from-x from-y :as from-point] [to-x to-y :as to-point]]
  (lazy-seq (cons [(random-int from-x to-x) (random-int from-y to-y)]
                  (random-points from-point to-point))))

(defn distinct-random-points [n [from-x from-y :as from-point] [to-x to-y :as to-point]]
  ;(if (< (* x y) n)
  ;  (recur (* x y) point)
  (vec (take n (distinct (random-points from-point to-point)))))

(def available-actions
  [:idle
   :move-north :move-east :move-south :move-west
   :take-north :take-east :take-south :take-west
   :look-north :look-east :look-south :look-west])

(defn random-actions [n]
  (->> available-actions
       (repeat n)
       (mapv rand-nth)))

(def actions
  [(random-actions 64)
   (random-actions 64)
   (random-actions 64)
   (random-actions 64)
   (random-actions 64)
   (random-actions 64)
   (random-actions 64)
   (random-actions 64)])

(def points (distinct-random-points 256 [-25 -15] [26 16]))
;(def living-points (subvec points 0 64))
;(def food-points (subvec points 64 128))
;(def poison-points (subvec points 128 192))

(def living-points (distinct-random-points 64 [-9 -15] [-3 15]))
(def poison-points (distinct-random-points 64 [-3 -15] [3 16]))
(def food-points (distinct-random-points 64 [3 -15] [9 16]))

(def configuration
  {:title      "Genetic Algorithms"
   :size       [1300 700]
   :speed      1024

   :experiment {:configuration {:points            {:living living-points
                                                    :food   food-points
                                                    :poison poison-points}
                                :available-actions available-actions
                                :actions           actions
                                :health            35}
                :statistics    {:iteration 0
                                :lifespan  0}
                :result        {}}})

(defn -main [& args]
  (visualisation/display configuration))

(-main)
