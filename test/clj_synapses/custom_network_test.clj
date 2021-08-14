(ns clj-synapses.custom-network-test
  (:require [clojure.test :refer :all]
            [clj-synapses.net :as net]
            [clj-synapses.fun :as fun])
  (:import (java.util Random)))

(def input-values [1.0, 0.5625, 0.511111, 0.47619])

(def expected-output [0.4, 0.05, 0.2])

(def layer-sizes [4, 6, 8, 5, 3])

(defn activation-f [i]
  (condp = i
    0 fun/sigmoid
    1 fun/identity
    2 fun/leaky-re-lu
    3 fun/tanh))

(def rnd (Random. 1000))

(defn weight-init-f [_]
  (- 1.0 (* 2.0 (.nextDouble rnd))))

(def just-created-network (net/->net layer-sizes activation-f weight-init-f))

(def network-json (slurp "test-resources/network.json"))

(def network-svg (slurp "test-resources/drawing.svg"))

(def network (net/json-> network-json))

(def prediction (net/predict network input-values))

(def learning-rate 0.01)

(def fit-network (net/par-fit network learning-rate input-values expected-output))

(deftest test-network-of-to-json
  (is (= (net/->json just-created-network)
         (-> just-created-network net/->json net/json-> net/->json))))

(deftest test-network-prediction
  (is (= [-0.013959435951885571, -0.16770539176070537, 0.6127887629040738]
         prediction)))

(deftest test-normal-errors
  (is (= [-0.18229373795952453, -0.10254022760223255, -0.09317233470223055, -0.086806455078946]
         (net/errors network input-values expected-output false))))

(deftest test-zero-errors
  (is (= [0.0 0.0 0.0 0.0]
         (net/errors network input-values prediction false))))

(deftest test-fit-network-prediction
  (is (= [-0.006109464554743645, -0.1770428172237149, 0.6087944183600162]
         (net/par-predict fit-network input-values))))

(deftest test-network-svg
  (is (= network-svg
         (net/->svg network))))
