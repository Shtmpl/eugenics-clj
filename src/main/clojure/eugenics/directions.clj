(ns eugenics.directions)

(def ^:private cardinal-directions [:north :east :south :west])
(def ^:private relative-directions [:forward :right :backward :left])

(defn- cycle-from [n coll]
  (drop n (cycle coll)))

(defn relative->cardinal [this relative]
  (case this
    :north
    (case relative :forward :north, :right :east, :backward :south, :left :west)

    :east
    (case relative :forward :east, :right :south, :backward :west, :left :north)

    :south
    (case relative :forward :south, :right :west, :backward :north, :left :east)

    :west
    (case relative :forward :west, :right :north, :backward :east, :left :south)))
