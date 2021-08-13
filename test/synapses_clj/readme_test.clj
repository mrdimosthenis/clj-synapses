(ns synapses-clj.readme-test
  (:require [clojure.test :refer :all]
            [synapses-clj.net :as net]
            [synapses-clj.fun :as fun]
            [synapses-clj.codec :as codec]
            [synapses-clj.stats :as stats]))

(def rand-network
  (net/->net
    [2 3 1]))

(net/->json
  rand-network)
#_(println
    (net/->json
      rand-network))

(def network
  (net/json->
    "[[{\"activationF\" : \"sigmoid\", \"weights\" : [-0.5,0.1,0.8]},
       {\"activationF\" : \"sigmoid\", \"weights\" : [0.7,0.6,-0.1]},
       {\"activationF\" : \"sigmoid\", \"weights\" : [-0.8,-0.1,-0.7]}],
      [{\"activationF\" : \"sigmoid\", \"weights\" : [0.5,-0.3,-0.4,-0.5]}]]"))

(net/predict
  network
  [0.2 0.6])
#_(println
    (net/predict
      network
      [0.2 0.6]))

(net/fit
  network
  0.1
  [0.2 0.6]
  [0.9])

(reduce
  (fn [acc [xs ys]]
    (net/fit acc 0.1 xs ys))
  network
  [[[0.2 0.6] [0.9]]
   [[0.1 0.8] [0.2]]
   [[0.5 0.4] [0.6]]])

(net/->net
  [2 3 1]
  1000)

(defn activation-f
  [layer-index]
  (condp = layer-index
    0 fun/sigmoid
    1 fun/identity
    2 fun/leaky-re-lu
    3 fun/tanh))

(defn weight-init-f
  [layer-index]
  (* (inc layer-index)
     (- 1 (* 2.0 (rand)))))

(def custom-network
  (net/->net
    [4 6 8 5 3]
    activation-f
    weight-init-f))

(net/->svg
  custom-network)

(def exp-and-pred-vals
  [[[0.0 0.0 1.0] [0.0 0.1 0.9]]
   [[0.0 1.0 0.0] [0.8 0.2 0.0]]
   [[1.0 0.0 0.0] [0.7 0.1 0.2]]
   [[1.0 0.0 0.0] [0.3 0.3 0.4]]
   [[0.0 0.0 1.0] [0.2 0.2 0.6]]])

(stats/rmse
  exp-and-pred-vals)
#_(println
    (stats/rmse
      exp-and-pred-vals))

(stats/score
  exp-and-pred-vals)
#_(println
    (stats/score
      exp-and-pred-vals))

(def setosa
  {"petal_length" "1.5"
   "petal_width"  "0.1"
   "sepal_length" "4.9"
   "sepal_width"  "3.1"
   "species"      "setosa"})

(def versicolor
  {"petal_length" "3.8"
   "petal_width"  "1.1"
   "sepal_length" "5.5"
   "sepal_width"  "2.4"
   "species"      "versicolor"})

(def virginica
  {"petal_length" "6.0"
   "petal_width"  "2.2"
   "sepal_length" "5.0"
   "sepal_width"  "1.5"
   "species"      "virginica"})

(def dataset
  [setosa
   versicolor
   virginica])

(def preprocessor
  (codec/->codec
    [["petal_length" false]
     ["petal_width" false]
     ["sepal_length" false]
     ["sepal_width" false]
     ["species" true]]
    dataset))

(codec/->json
  preprocessor)
#_(println
    (codec/->json
      preprocessor))

(codec/json->
  "[{\"Case\" : \"SerializableContinuous\",
     \"Fields\" : [{\"key\" : \"petal_length\",\"min\" : 1.5,\"max\" : 6.0}]},
    {\"Case\" : \"SerializableContinuous\",
     \"Fields\" : [{\"key\" : \"petal_width\",\"min\" : 0.1,\"max\" : 2.2}]},
    {\"Case\" : \"SerializableContinuous\",
     \"Fields\" : [{\"key\" : \"sepal_length\",\"min\" : 4.9,\"max\" : 5.5}]},
    {\"Case\" : \"SerializableContinuous\",
     \"Fields\" : [{\"key\" : \"sepal_width\",\"min\" : 1.5,\"max\" : 3.1}]},
    {\"Case\" : \"SerializableDiscrete\",
     \"Fields\" : [{\"key\" : \"species\",\"values\" : [\"virginica\",\"versicolor\",\"setosa\"]}]}]")

(def encoded-setosa
  (codec/encode
    preprocessor
    setosa))
#_(println
    (codec/encode
      preprocessor
      setosa))

(codec/decode
  preprocessor
  encoded-setosa)
#_(println
    (codec/decode
      preprocessor
      encoded-setosa))
