(ns eugenics.simulation-test
  (:require [clojure.test :as test]
            [eugenics.simulation :as simulation]))

(test/deftest shift-test
  (test/is (= (simulation/shift [0] 1) [0]))
  (test/is (= (simulation/shift [0 1] 1) [1 0]))
  (test/is (= (simulation/shift [0 1 2] 1) [1 2 0])))

(test/deftest find-entity-by-point-test
  (test/is (= (simulation/find-entity-by-point #{} [0 0])
              nil))
  (test/is (= (simulation/find-entity-by-point #{{:type  :food
                                                  :point [0 1]}}
                                               [0 0])
              nil))
  (test/is (= (simulation/find-entity-by-point #{{:type  :food
                                                  :point [0 1]}}
                                               [0 1])
              {:type  :food
               :point [0 1]}))
  (test/is (= (simulation/find-entity-by-point #{{:type      :living
                                                  :behaviour [:idle]
                                                  :health    42
                                                  :point     [0 0]}
                                                 {:type  :food
                                                  :point [0 1]}}
                                               [0 1])
              {:type  :food
               :point [0 1]})))

(test/deftest act-test
  (test/testing ":idle"
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:idle :_]
                                   :health    42
                                   :point     [0 0]}}
                                {:type      :living
                                 :behaviour [:idle :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :idle]
                   :health    41
                   :point     [0 0]}})))

  (test/testing ":move-north"
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-north :_]
                                   :health    42
                                   :point     [0 0]}}
                                {:type      :living
                                 :behaviour [:move-north :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-north]
                   :health    41
                   :point     [0 1]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-north :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type      :living
                                   :behaviour [:_]
                                   :health    90
                                   :point     [0 1]}}
                                {:type      :living
                                 :behaviour [:move-north :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-north]
                   :health    41
                   :point     [0 0]}
                  {:type      :living
                   :behaviour [:_]
                   :health    90
                   :point     [0 1]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-north :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type  :food
                                   :point [0 1]}}
                                {:type      :living
                                 :behaviour [:move-north :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-north]
                   :health    (+ (dec 42) 10)
                   :point     [0 1]}})))

  (test/testing ":move-east"
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-east :_]
                                   :health    42
                                   :point     [0 0]}}
                                {:type      :living
                                 :behaviour [:move-east :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-east]
                   :health    41
                   :point     [1 0]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-east :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type      :living
                                   :behaviour [:_]
                                   :health    90
                                   :point     [1 0]}}
                                {:type      :living
                                 :behaviour [:move-east :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-east]
                   :health    41
                   :point     [0 0]}
                  {:type      :living
                   :behaviour [:_]
                   :health    90
                   :point     [1 0]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-east :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type  :food
                                   :point [1 0]}}
                                {:type      :living
                                 :behaviour [:move-east :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-east]
                   :health    (+ (dec 42) 10)
                   :point     [1 0]}})))

  (test/testing ":move-south"
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-south :_]
                                   :health    42
                                   :point     [0 0]}}
                                {:type      :living
                                 :behaviour [:move-south :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-south]
                   :health    41
                   :point     [0 -1]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-south :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type      :living
                                   :behaviour [:_]
                                   :health    90
                                   :point     [0 -1]}}
                                {:type      :living
                                 :behaviour [:move-south :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-south]
                   :health    41
                   :point     [0 0]}
                  {:type      :living
                   :behaviour [:_]
                   :health    90
                   :point     [0 -1]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-south :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type  :food
                                   :point [0 -1]}}
                                {:type      :living
                                 :behaviour [:move-south :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-south]
                   :health    (+ (dec 42) 10)
                   :point     [0 -1]}})))

  (test/testing ":move-west"
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-west :_]
                                   :health    42
                                   :point     [0 0]}}
                                {:type      :living
                                 :behaviour [:move-west :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-west]
                   :health    41
                   :point     [-1 0]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-west :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type      :living
                                   :behaviour [:_]
                                   :health    90
                                   :point     [-1 0]}}
                                {:type      :living
                                 :behaviour [:move-west :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-west]
                   :health    41
                   :point     [0 0]}
                  {:type      :living
                   :behaviour [:_]
                   :health    90
                   :point     [-1 0]}}))
    (test/is (= (simulation/act #{{:type      :living
                                   :behaviour [:move-west :_]
                                   :health    42
                                   :point     [0 0]}
                                  {:type  :food
                                   :point [-1 0]}}
                                {:type      :living
                                 :behaviour [:move-west :_]
                                 :health    42
                                 :point     [0 0]})
                #{{:type      :living
                   :behaviour [:_ :move-west]
                   :health    (+ (dec 42) 10)
                   :point     [-1 0]}}))))

(test/deftest act-all-test
  (test/is (= (simulation/act-all #{{:type      :living
                                     :behaviour [:idle]
                                     :health    42
                                     :point     [0 0]}
                                    {:type      :living
                                     :behaviour [:move-north]
                                     :health    42
                                     :point     [0 1]}
                                    {:type  :food
                                     :point [0 2]}
                                    {:type      :living
                                     :behaviour [:move-east]
                                     :health    42
                                     :point     [1 0]}
                                    {:type      :living
                                     :behaviour [:move-south]
                                     :health    42
                                     :point     [0 -1]}
                                    {:type      :living
                                     :behaviour [:move-west]
                                     :health    42
                                     :point     [-1 0]}})
              #{{:type      :living
                 :behaviour [:idle]
                 :health    41
                 :point     [0 0]}
                {:type      :living
                 :behaviour [:move-north]
                 :health    (+ (dec 42) 10)
                 :point     [0 2]}
                {:type      :living
                 :behaviour [:move-east]
                 :health    41
                 :point     [2 0]}
                {:type      :living
                 :behaviour [:move-south]
                 :health    41
                 :point     [0 -2]}
                {:type      :living
                 :behaviour [:move-west]
                 :health    41
                 :point     [-2 0]}})))

(test/deftest disj-entities-test
  (letfn [(dead? [entity] (< (:health entity) 1))]
    (test/is (= (simulation/disj-entities #{} dead?)
                #{}))
    (test/is (= (simulation/disj-entities #{{:type  :food
                                             :point [0 1]}}
                                          dead?)
                #{{:type  :food
                   :point [0 1]}}))
    (test/is (= (simulation/disj-entities #{{:type      :living
                                             :behaviour [:_]
                                             :health    42
                                             :point     [0 0]}}
                                          dead?)
                #{{:type      :living
                   :behaviour [:_]
                   :health    42
                   :point     [0 0]}}))
    (test/is (= (simulation/disj-entities #{{:type      :living
                                             :behaviour [:_]
                                             :health    0
                                             :point     [0 0]}}
                                          dead?)
                #{}))))


(test/run-tests)


