(ns synapses-clj.net
  "This namespace contains all the functions that are related to the neural networks.

  ```clojure
  (require '[synapses-clj.net :as net])
  ```

  There are four ways to create a neural network:

  1. By providing its layer sizes, a random sigmoid neural network is created.

  ```clojure
  (def network
    (net/->net
      [3 4 2]))
  ```
  2. By providing its layer sizes and a seed, a non-random sigmoid neural network is created.

  ```clojure
  (net/->net
    [2 3 1]
    1000)
  ```

  3. By providing its JSON representation.

  ```clojure
  (net/json->
    \"[[{\"activationF\":\"sigmoid\",\"weights\":[-0.4,-0.1,-0.8]}]]\")
  ```

  4. By providing the size, the activation function and the weights for each layer.

  ```clojure
  (require '[synapses-clj.fun :as fun])

  (net/->net
    [2 3 1]
    (fn [_] fun/sigmoid)
    (fn [_] (rand)))
  ```

  EXAMPLES

  Get the prediction for an input:

  ```clojure
  (net/predict
    network
    [0.4 0.05 0.2])
  ```

  Fit network to a single observation:

  ```clojure
  (net/fit
    network
    0.1
    [0.4 0.05 0.2]
    [0.03 0.8])
  ```

  Get the JSON representation of the network:

  ```clojure
  (net/->json
    rand-network)
  ```"
  (:import (java.util.function IntFunction)
           (synapses.jvm NetJ)))

(defn ->net
  "Returns a neural network by accepting:

  * its layer sizes

  ```clojure
  (net/->net
    [3 4 2])
  ```

  * its layer sizes and a seed

  ```clojure
  (net/->net
    [2 3 1]
    1000)
  ```

  * the size, the activation function and the weights for each layer

  ```clojure
  (net/->net
    [2 3 1]
    (fn [_] fun/sigmoid)
    (fn [_] (rand)))
  ```"
  ([layer-sizes]
   (-> layer-sizes
       int-array
       NetJ/apply))
  ([layer-sizes seed]
   (NetJ/apply
     (int-array layer-sizes)
     seed))
  ([layer-sizes activation-f weight-init-f]
   (NetJ/apply (int-array layer-sizes)
               (reify IntFunction
                 (apply [_ i]
                   (activation-f i)))
               (reify IntFunction
                 (apply [_ i]
                   (weight-init-f i))))))

(defn ->json
  "Returns the JSON representation of the neural network."
  [network]
  (.json network))

(defn json->
  "Parses and returns a neural network.

  ```clojure
  (net/json->
    \"[[{\"activationF\":\"sigmoid\",\"weights\":[-0.4,-0.1,-0.8]}]]\")
  ```"
  [json]
  (NetJ/apply json))

(defn predict
  "Returns a prediction made for the provided input.
  The size of the returned vector should be equal to the size of the output layer.
  `input-values` is the values of the features.
  The size of the accepted vector should be equal to the size of the input layer.

  ```clojure
  (net/predict
    network
    [0.2 0.6])
  ```"
  [network input-values]
  (->> input-values
       double-array
       (.predict network)
       vec))

(defn par-predict
  "Returns a prediction made for the provided input.

  The calculation is performed in parallel.
  When the neural network has huge layers, the parallel calculation boosts the performance."
  [network input-values]
  (->> input-values
       double-array
       (.parPredict network)
       vec))

(defn errors [network input-values expected-output in-parallel?]
  (vec
    (.errors network
             (double-array input-values)
             (double-array expected-output)
             in-parallel?)))

(defn fit
  "Returns the neural network with its weights adjusted to the provided observation.

  In order for it to be trained, it should fit with multiple observations,
  usually by reducing over a collection.

  `learning-rate` is a number that controls how much the weights are adjusted to the observation.

  `input-values` is the feature values of the observation
  and its size should be equal to the size of the input layer.

  `expected-output` is the expected output of the observation
  and its size should be equal to the size of the output layer.

  ```clojure
  (net/fit
    network
    0.1
    [0.2 0.6]
    [0.9])
  ```"
  [network learning-rate input-values expected-output]
  (.fit network
        learning-rate
        (double-array input-values)
        (double-array expected-output)))

(defn par-fit
  "Returns a neural network that has the same shape of the original,
  but it has learned from the provided observation.

  The calculation is performed in parallel.
  When the neural network has huge layers, the parallel calculation boosts the performance."
  [network learning-rate input-values expected-output]
  (.fitPar network
           learning-rate
           (double-array input-values)
           (double-array expected-output)))

(defn ->svg
  "Returns the SVG representation of the neural network.

  The color of each neuron depends on its activation function
  while the transparency of the synapses depends on their weight.

  ```clojure
  (net/->svg
    custom-network)
  ```"
  [network]
  (.svg network))
