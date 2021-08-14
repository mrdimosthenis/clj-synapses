(ns synapses-clj.fun
  "The activation functions a neuron can have.

  They can be used in the arguments of neural network's creation.

  ```clojure
  (require '[synapses-clj.net :as net]
           '[synapses-clj.fun :as fun])
  ```

  ```clojure
  (net/->net
    [2 3 1]
    (fn [_] fun/sigmoid)
    (fn [_] (rand)))
  ```

  ```clojure
  (net/->net
    [4 6 8 5 3]
    (fn [_] fun/identity)
    (fn [_] (rand)))
  ```

  ```clojure
  (net/->net
    [4 8 3]
    (fn [_] fun/leaky-re-lu)
    (fn [_] (rand)))
  ```

  ```clojure
  (net/->net
    [2 1]
    (fn [_] fun/tanh)
    (fn [_] (rand)))
  ```"
  (:refer-clojure :exclude [identity])
  (:import (synapses.model.net_elems.activation Activation$)))

(def sigmoid
  "Sigmoid takes any value as input and returns values in the range of 0.0 to 1.0.

  ```clojure
  (fn [x] (/ (inc (Math/exp (- x)))))
  ```
  "
  Activation$/Sigmoid)

(def identity
  "Identity is a linear function where the output is equal to the input.

  ```clojure
  (fn [x] x)
  ```"
  Activation$/Identity)

(def tanh
  "Tanh is similar to Sigmoid, but outputs values in the range of -1.0 and 1.0.

  ```clojure
  (fn [x] (Math/tanh x))
  ```"
  Activation$/Tanh)

(def leaky-re-lu
  "Leaky-re-lu gives a small proportion of x if x is negative and x otherwise.

  ```clojure
  (fn [x]
    (if (< x 0.0)
      (* x 0.01)
      x))
  ```"
  Activation$/LeakyReLU)
