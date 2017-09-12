(ns eugenics.directions-test
  (:require [clojure.test :as test]
            [eugenics.directions :as directions]))

(test/deftest relative->cardinal-test
  (test/is (= (directions/relative->cardinal :north :forward) :north))
  (test/is (= (directions/relative->cardinal :north :right) :east))
  (test/is (= (directions/relative->cardinal :north :backward) :south))
  (test/is (= (directions/relative->cardinal :north :left) :west))

  (test/is (= (directions/relative->cardinal :east :forward) :east))
  (test/is (= (directions/relative->cardinal :east :right) :south))
  (test/is (= (directions/relative->cardinal :east :backward) :west))
  (test/is (= (directions/relative->cardinal :east :left) :north))

  (test/is (= (directions/relative->cardinal :south :forward) :south))
  (test/is (= (directions/relative->cardinal :south :right) :west))
  (test/is (= (directions/relative->cardinal :south :backward) :north))
  (test/is (= (directions/relative->cardinal :south :left) :east))

  (test/is (= (directions/relative->cardinal :west :forward) :west))
  (test/is (= (directions/relative->cardinal :west :right) :north))
  (test/is (= (directions/relative->cardinal :west :backward) :east))
  (test/is (= (directions/relative->cardinal :west :left) :south)))

(test/run-tests)
