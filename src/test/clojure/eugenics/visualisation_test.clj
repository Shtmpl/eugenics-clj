(ns eugenics.visualisation-test
  (:require [clojure.test :as test]
            [eugenics.visualisation :as visualisation]))

(test/deftest environment->grid-test
  (test/is (= (visualisation/result->grid #{})
              {}))
  (test/is (= (visualisation/result->grid #{{:type      :living
                                             :behaviour [:_]
                                             :health    42
                                             :point     [0 0]}})
              {[0 0] {:type :living, :value 42}}))
  (test/is (= (visualisation/result->grid #{{:type  :food
                                             :point [0 1]}})
              {[0 1] {:type :food}}))
  (test/is (= (visualisation/result->grid #{{:type      :living
                                             :behaviour [:_]
                                             :health    42
                                             :point     [0 0]}
                                            {:type      :living
                                             :behaviour [:_]
                                             :health    90
                                             :point     [0 1]}})
              {[0 0] {:type :living, :value 42}
               [0 1] {:type :living, :value 90}}))
  (test/is (= (visualisation/result->grid #{{:type      :living
                                             :behaviour [:_]
                                             :health    42
                                             :point     [0 0]}
                                            {:type  :food
                                             :point [0 1]}})
              {[0 0] {:type :living, :value 42}
               [0 1] {:type :food}})))


(test/run-tests)
