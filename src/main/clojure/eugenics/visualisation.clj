(ns eugenics.visualisation
  (:require [quil.core :as quil]
            [quil.middleware :as quil-middleware]
            [eugenics.application :as application]))


(def background-colour [250 250 250])
(def foreground-colour [50 50 50])

(def living-colour [255 105 35])
(def living-health-colour [250 250 250])

(def food-colour [3 124 15])

(def poison-colour [50 50 50])


(defn result->grid [result]
  (if (seq result)
    (apply assoc {} (interleave (for [entity result] (:point entity))
                                (for [entity result]
                                  (case (:type entity)
                                    :living
                                    {:type :living, :value (:health entity)}

                                    :food
                                    {:type :food}

                                    :poison
                                    {:type :poison}))))
    {}))

(defn setup [speed experiment]
  (let [{:keys [result] :as experiment} (application/initiate experiment)]
    {:experiment    experiment
     :grid          (result->grid result)
     :cell-diameter 16

     :speed         speed}))

(defn- sleep [state]
  (let [speed (:speed state)]
    (Thread/sleep (quot 1000 speed)))
  state)

(defn modify [state]
  (let [{:keys [result] :as experiment} (application/step (:experiment state))
        grid (result->grid result)]
    (-> state
        (assoc-in [:experiment] experiment)
        (assoc-in [:grid] grid)
        (sleep))))

(defn- draw-cell [x y diameter content]
  (quil/no-stroke)

  (case (:type content)
    :living
    (do (quil/with-fill living-colour
                        (quil/rect-mode :center)
                        (quil/rect x y diameter diameter))
        (quil/with-fill living-health-colour
                        (quil/text-align :center :center)
                        (quil/text-size (quot diameter 2))
                        (quil/text (str (:value content)) x y diameter diameter)))

    :food
    (do (quil/with-fill food-colour
                        (quil/rect-mode :center)
                        (quil/rect x y diameter diameter)))

    :poison
    (do (quil/with-fill poison-colour
                        (quil/rect-mode :center)
                        (quil/rect x y diameter diameter)))))

(defn- format-statistics [statistics]
  (format "# %-4s%nLifespan: %-3s"
          (:iteration statistics)
          (:lifespan statistics)))

(defn draw [state]
  (apply quil/background background-colour)
  (apply quil/fill foreground-colour)

  (quil/with-translation [(quot (quil/width) 2) (quot (quil/height) 2)]
                         (doseq [[[x y] content] (:grid state)
                                 :let [cell-x (* x (:cell-diameter state))
                                       cell-y (* y (:cell-diameter state))]]
                           (draw-cell cell-x cell-y (:cell-diameter state) content)))

  (quil/text-align :left :top)
  (quil/text-size 20)
  (quil/text (format-statistics (application/statistics (:experiment state))) 10 10))



(defn display [{:keys [title size speed experiment] :as configuration}]
  (quil/sketch :host "eugenics"
               :setup (fn [] (setup speed experiment))
               :update modify
               :draw draw

               :title title
               :size size
               :settings (fn [] (quil/smooth 2))
               :features [:resizable]

               :middleware [quil-middleware/fun-mode]))
