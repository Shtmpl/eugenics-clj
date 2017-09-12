(ns eugenics.main-test
  (:require [clojure.test :as test]))

(def id (atom 0))

(prn (add-watch id :id (fn [key atom old-state new-state]
                         (printf "%s %s => %s%n" key old-state new-state))))

(swap! id inc)