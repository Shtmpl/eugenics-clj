(ns eugenics.application-test
  (:require [clojure.test :as test]
            [eugenics.application :as application]))

#_(test/deftest initiate-test
  (test/is (= (-> {:configuration {:points    {:living #{[0 0]}
                                               :food   #{}}
                                :action-count 1
                                :actions      #{:idle}
                                :health       45}
                   :result        {}}
                  (application/initiate))
              {:configuration {:points    {:living #{[0 0]}
                                           :food   #{}}
                            :action-count 1
                            :actions      #{:idle}
                            :health       45}
               :result        #{{:type   :living
                              :behaviour (->> :idle (repeat 1) (vec))
                              :health    45
                              :point     [0 0]}}}))
  (test/is (= (-> {:configuration {:points    {:living #{[0 0]}
                                               :food   #{}}
                                :action-count 10
                                :actions      #{:idle}
                                :health       45}
                   :result        {}}
                  (application/initiate))
              {:configuration {:points    {:living #{[0 0]}
                                           :food   #{}}
                            :action-count 10
                            :actions      #{:idle}
                            :health       45}
               :result        #{{:type   :living
                              :behaviour (->> :idle (repeat 10) (vec))
                              :health    45
                              :point     [0 0]}}})))

#_(test/deftest step-test
  (test/is (= (-> {:configuration {:points    {:living #{[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0] [7 0]}
                                               :food   #{}}
                                :action-count 2
                                :actions      #{:idle}
                                :health       45}
                   :result        #{{:type   :living
                                  :behaviour [:idle :idle]
                                  :health    45
                                  :point     [0 0]}}}
                  (application/step true?))
              {:configuration {:points    {:living #{[0 0] [1 0] [2 0] [3 0] [4 0] [5 0] [6 0] [7 0]}
                                           :food   #{}}
                            :action-count 2
                            :actions      #{:idle}
                            :health       45}
               :result        #{{:type   :living
                              :behaviour [:idle :idle]
                              :health    44
                              :point     [0 0]}}})))


(test/run-tests)