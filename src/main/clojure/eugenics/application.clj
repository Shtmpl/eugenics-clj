(ns eugenics.application
  (:require [eugenics.simulation :as simulation]))

(defn- spawn-living [points actions health]
  (let [behaviours (shuffle (mapcat (partial repeat 8) actions))]
    (map (fn [point behaviour] {:type      :living
                                :behaviour behaviour
                                :health    health
                                :point     point})
         points
         behaviours)))

(defn- spawn-food [points]
  (map (fn [point] {:type  :food
                    :point point})
       points))

(defn- spawn-poison [points]
  (map (fn [point] {:type  :poison
                    :point point})
       points))

(defn- spawn-entities [points actions health]
  (concat (spawn-living (:living points) actions health)
          (spawn-food (:food points))
          (spawn-poison (:poison points))))

(defn initiate [{{:keys [points actions health]} :configuration :as experiment}]
  (-> experiment
      (assoc-in [:statistics :lifespan] 0)
      (assoc-in [:result] (set (spawn-entities points actions health)))))

(defn survive [{:keys [configuration result] :as experiment}]
  (-> experiment
      (assoc-in [:configuration :survived] (->> (simulation/find-creatures result)
                                                (sort-by :health)
                                                (take 8)
                                                (vec)))))

(defn- mutate-behaviour [behaviour available-actions]
  (let [index (rand-int (count behaviour))]
    (-> behaviour
        (assoc-in [index] (rand-nth available-actions)))))

(defn- mutate [actions available-actions]
  (let [index (rand-int (count actions))]
    (-> actions
        (update-in [index] mutate-behaviour available-actions))))

(defn adapt [{:keys [configuration result] :as experiment}]
  (let [remaining (map :behaviour (:survived configuration))]
    (-> experiment
        (assoc-in [:configuration :actions]
                  (mutate (vec (take 8 (concat remaining (:actions configuration))))
                          (:available-actions configuration))))))

(defn- average [coll]
  (if (seq coll)
    (quot (apply + coll) (count coll))
    0))

(defn statistics [{:keys [configuration statistics result] :as experiment}]
  (let [creatures (simulation/find-creatures result)]
    {:iteration (:iteration statistics)
     :lifespan  (:lifespan statistics)}))

(defn partially-completed? [{:keys [configuration statistics result] :as experiment}]
  (= (:lifespan statistics) (:health configuration)))

(defn completed? [{:keys [configuration statistics result] :as experiment}]
  (zero? (count (simulation/find-creatures result))))

(defn step [{:keys [configuration result] :as experiment}]
  (if (completed? experiment)
    (do (println (statistics experiment))
        (-> experiment
            (update-in [:statistics :iteration] inc)
            (adapt)
            (initiate)))

    (-> experiment
        ((fn [experiment]
           (if (partially-completed? experiment)
             (-> experiment
                 (survive))
             experiment)))
        (update-in [:statistics :lifespan] inc)
        (assoc-in [:result]
                  (-> result
                      (simulation/act-all)
                      (simulation/disj-entities (fn [entity] (< (:health entity) 1))))))))





