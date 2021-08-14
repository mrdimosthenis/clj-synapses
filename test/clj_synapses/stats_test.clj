(ns clj-synapses.stats-test
  (:require [clojure.test :refer :all]
            [clj-synapses.stats :as stats]))

(deftest test-rmse
  (is (= 0.7071067811865476
         (stats/rmse
           [[[0.0, 0.0, 1.0] [0.0, 0.0, 1.0]]
            [[0.0, 0.0, 1.0] [0.0, 1.0, 1.0]]]))))

(deftest test-score
  (is (= 0.6
         (stats/score
           [[[0.0, 0.0, 1.0], [0.0, 0.1, 0.9]]
            [[0.0, 1.0, 0.0], [0.8, 0.2, 0.0]]
            [[1.0, 0.0, 0.0], [0.7, 0.1, 0.2]]
            [[1.0, 0.0, 0.0], [0.3, 0.3, 0.4]]
            [[0.0, 0.0, 1.0], [0.2, 0.2, 0.6]]]))))
