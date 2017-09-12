(ns eugenics.simulation)

(defn shift [behaviour n]
  {:pre [(vector? behaviour)]}
  (vec (concat (subvec behaviour n) (subvec behaviour 0 n))))

(defn- neighbour-point [[x y] direction]
  (case direction
    :north [x (inc y)], :east [(inc x) y], :south [x (dec y)], :west [(dec x) y]))

(defn contains-entity? [result point]
  (contains? (set (map :point result)) point))

(defn find-entity-by-point [result point]
  (when (contains-entity? result point)
    (first (filter (fn [entity] (= (:point entity) point))
                   result))))

(defn find-creatures [result]
  (set (filter (fn [entity] (= (:type entity) :living)) result)))

(defn act [result {:keys [behaviour health point] :as entity}]
  (case (first behaviour)
    :idle
    (-> result
        (disj entity)
        (conj (-> entity
                  (update-in [:behaviour] shift 1)
                  (update-in [:health] dec))))

    :move-north
    (let [target-point (neighbour-point point :north)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10)
                        (assoc-in [:point] target-point))))

          :poison
          (-> result
              (disj entity)
              (conj (-> target-entity
                        (assoc-in [:point] (:point entity))))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :move-east
    (let [target-point (neighbour-point (:point entity) :east)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10)
                        (assoc-in [:point] target-point))))
          :poison
          (-> result
              (disj entity)
              (conj (-> target-entity
                        (assoc-in [:point] (:point entity))))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :move-south
    (let [target-point (neighbour-point (:point entity) :south)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10)
                        (assoc-in [:point] target-point))))

          :poison
          (-> result
              (disj entity)
              (conj (-> target-entity
                        (assoc-in [:point] (:point entity))))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :move-west
    (let [target-point (neighbour-point (:point entity) :west)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10)
                        (assoc-in [:point] target-point))))

          :poison
          (-> result
              (disj entity)
              (conj (-> target-entity
                        (assoc-in [:point] (:point entity))))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :take-north
    (let [target-point (neighbour-point (:point entity) :north)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10))))

          :poison
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))
              (conj (-> target-entity
                        (assoc-in [:type] :food)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :take-east
    (let [target-point (neighbour-point (:point entity) :east)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10))))

          :poison
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))
              (conj (-> target-entity
                        (assoc-in [:type] :food)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :take-south
    (let [target-point (neighbour-point (:point entity) :south)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10))))

          :poison
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))
              (conj (-> target-entity
                        (assoc-in [:type] :food)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :take-west
    (let [target-point (neighbour-point (:point entity) :west)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec)
                        (update-in [:health] + 10))))

          :poison
          (-> result
              (disj entity)
              (disj target-entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))
              (conj (-> target-entity
                        (assoc-in [:type] :food)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec)
                      (assoc-in [:point] target-point))))))

    :look-north
    (let [target-point (neighbour-point (:point entity) :north)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec))))

          :poison
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec))))))

    :look-east
    (let [target-point (neighbour-point (:point entity) :east)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec))))

          :poison
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec))))))

    :look-south
    (let [target-point (neighbour-point (:point entity) :south)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec))))

          :poison
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec))))))

    :look-west
    (let [target-point (neighbour-point (:point entity) :west)]
      (if-let [target-entity (find-entity-by-point result target-point)]
        (case (:type target-entity)
          :living
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 1)
                        (update-in [:health] dec))))

          :wall
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 2)
                        (update-in [:health] dec))))

          :food
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 3)
                        (update-in [:health] dec))))

          :poison
          (-> result
              (disj entity)
              (conj (-> entity
                        (update-in [:behaviour] shift 4)
                        (update-in [:health] dec)))))

        (-> result
            (disj entity)
            (conj (-> entity
                      (update-in [:behaviour] shift 5)
                      (update-in [:health] dec))))))
    ))

(defn act-all [result]
  (reduce act result (filter (fn [entity] (= (:type entity) :living)) result)))

(defn disj-entities [result predicate]
  (set (for [entity result
             :when (or (not= (:type entity) :living)
                       ((complement predicate) entity))]
         entity)))

