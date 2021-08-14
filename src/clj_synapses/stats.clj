(ns clj-synapses.stats
  "Measure the difference between the values predicted by a neural network and the observed values.

  ```clojure
  (require '[clj-synapses.stats :as stats])
  ```
  
  Calculate the root mean square error:
  
  ```clojure
  (stats/rmse
    [[[0.0, 0.0, 1.0] [0.0, 0.0, 1.0]]
     [[0.0, 0.0, 1.0] [0.0, 1.0, 1.0]]])
  ;;=> 0.7071067811865476
  ```

  Calculate the score of the classification accuracy:

  ```clojure
  (stats/score
    [[[0.0, 0.0, 1.0], [0.0, 0.1, 0.9]]
     [[0.0, 1.0, 0.0], [0.8, 0.2, 0.0]]
     [[1.0, 0.0, 0.0], [0.7, 0.1, 0.2]]
     [[1.0, 0.0, 0.0], [0.3, 0.3, 0.4]]
     [[0.0, 0.0, 1.0], [0.2, 0.2, 0.6]]])
  ;;=> 0.6
  ```"
  (:import (synapses.jvm StatsJ)))

(defn- stream-of-arrays [coll]
  (.stream
    (map (fn [[exp pred]]
           (into-array
             [(double-array exp)
              (double-array pred)]))
         coll)))

(defn rmse
  "Returns the the standard deviation of the prediction errors (root-mean-square-error).
  `exp-and-pred-vals` is a collection of pairs that contain the expected and predicted values.

  ```clojure
  (stats/rmse
    [[[0.0, 0.0, 1.0] [0.0, 0.0, 1.0]]
     [[0.0, 0.0, 1.0] [0.0, 1.0, 1.0]]])
  ;;=> 0.7071067811865476
  ```"
  [exp-and-pred-vals]
  (-> exp-and-pred-vals
      stream-of-arrays
      StatsJ/rmse))

(defn score
  "Returns the ratio of number of correct predictions to the total number of provided pairs.
  For a prediction to be considered as correct, the index of its maximum expected value
  needs to be the same with the index of its maximum predicted value.
  `exp-and-pred-vals` is a collection of pairs that contain the expected and predicted values.

  ```clojure
  (stats/score
    [[[0.0, 0.0, 1.0], [0.0, 0.1, 0.9]]
     [[0.0, 1.0, 0.0], [0.8, 0.2, 0.0]]
     [[1.0, 0.0, 0.0], [0.7, 0.1, 0.2]]
     [[1.0, 0.0, 0.0], [0.3, 0.3, 0.4]]
     [[0.0, 0.0, 1.0], [0.2, 0.2, 0.6]]])
  ;;=> 0.6
  ```"
  [exp-and-pred-vals]
  (-> exp-and-pred-vals
      stream-of-arrays
      StatsJ/score))
