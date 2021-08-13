(ns synapses-clj.codec
  "A codec can encode and decode every data point.

  One hot encoding is a process that turns discrete attributes into a list of 0.0 and 1.0.
  Minmax normalization scales continuous attributes into values between 0.0 and 1.0.

  ```clojure
  (require '[synapses-clj.codec :as codec])
  ```

  There are two ways to create a codec:
  
  1. By providing a list of pairs that define the name and the type of each attribute:

  ```clojure
  (def preprocessor
    (codec/->codec
      [[\"petal_length\" false]
       [\"species\" true]]
      [{\"petal_length\" \"1.5\"
        \"species\"      \"setosa\"}
       {\"petal_length\" \"3.8\"
        \"species\"      \"versicolor\"}]))
  ```

  2. By providing its JSON representation.

  ```clojure
  (def preprocessor
    (codec/json->
      \"[{\"Case\" : \"SerializableContinuous\",
          \"Fields\" : [{\"key\" : \"petal_length\",\"min\" : 1.5,\"max\" : 3.8}]},
         {\"Case\" : \"SerializableDiscrete\",
          \"Fields\" : [{\"key\" : \"species\",\"values\" : [\"setosa\",\"versicolor\"]}]}]\"))
  ```

  EXAMPLES

  Encode a data point:

  ```clojure
  (codec/encode
    preprocessor
    {\"petal_length\" \"1.5\"
     \"species\" \"setosa\"})
  ;;=> [0.0, 1.0, 0.0]
  ```

  Decode a data point:

  ```clojure
  (codec/decode
    preprocessor
    [0.0, 1.0, 0.0])
  ;;=> {\"petal_length\" \"1.5\", \"species\" \"setosa\"}
  ```

  Get the JSON representation of the codec:

  ```clojure
  (codec/->json
    preprocessor)
  ```"
  (:import (synapses.custom AttributeWithFlag)
           (synapses.jvm CodecJ)))

(defn ->codec
  "Returns a codec that can encode and decode every data point.
  `attributes` is a vector of pairs that define the name and the type (discrete or not) of each attribute.

  ```clojure
  (codec/->codec
    [[\"petal_length\" false]
     [\"species\" true]]
    [{\"petal_length\" \"1.5\"
      \"species\"      \"setosa\"}
     {\"petal_length\" \"3.8\"
      \"species\"      \"versicolor\"}])
  ```"
  [attributes data-points]
  (let [attrs (into-array
                (map
                  (fn [[attr flag]]
                    (AttributeWithFlag. attr flag))
                  attributes))
        stream (.stream data-points)]
    (CodecJ/apply attrs stream)))

(defn json->
  "Returns a codec that can encode and decode every data point.
  `json` is the JSON representation of a codec.

  ```clojure
  (codec/json->
    \"[{\"Case\" : \"SerializableContinuous\",
        \"Fields\" : [{\"key\" : \"petal_length\",\"min\" : 1.5,\"max\" : 3.8}]},
       {\"Case\" : \"SerializableDiscrete\",
        \"Fields\" : [{\"key\" : \"species\",\"values\" : [\"setosa\",\"versicolor\"]}]}]\")"
  [json]
  (CodecJ/apply json))

(defn ->json
  "Returns the JSON representation of the codec."
  [codec]
  (.json codec))

(defn encode
  "Accepts the `data-point` as a map of strings
  and returns the encoded data point as a vector of numbers between 0.0 and 1.0."
  [codec data-point]
  (vec
    (.encode codec data-point)))

(defn decode
  "Accepts the `encoded-values` as a vector of numbers between 0.0 and 1.0
  and returns the decoded data point as a map of strings."
  [codec encoded-values]
  (->> encoded-values
       double-array
       (.decode codec)
       (into {})))
