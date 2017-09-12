(ns eugenics.core
  (:require [eugenics.representation :as representation]))


;;; (defprotocol GeneticRepresentation ...
;;; encode: {} => []
;;; decode: [] => {}

;;; (defprotocol GeneticAlgorithm ...


(defn- assoc-population [grid population]
  (reduce (fn [grid entity]
            (representation/assoc-element grid (:coordinates entity) entity))
          grid
          population))

(defn initiate [{:keys [width height population-size genome health max-health] :as configuration}]
  (let [random-coordinates (representation/distinct-random-coordinates population-size [width height])
        population (map (fn [coordinates]
                          (representation/creature genome health coordinates :north))
                        random-coordinates)]
    (-> {}
        (assoc :environment
               (-> (representation/grid width height)
                   (assoc-population population))))))

(defn step [{:keys [environment] :as representation}]
  (let [creatures (representation/find-creatures environment)
        environment' (representation/interaction environment creatures)]
    (-> representation
        (assoc-in [:environment] environment'))))

(defn experiment [{:as representation} completed? & steps]
  (while (not (completed? representation))

    ))

(defn select [{:keys [environment] :as representation}])

(defn adapt [{:as representation}])                         ;; => {:representation ...}

(defn terminate? [{:as representation}])                    ;; => true/false
