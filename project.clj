(defproject synapses-clj "1.0.0-RC1"
  :description "A neural networks library for Clojure"
  :url "https://github.com/mrdimosthenis/Synapses"
  :license {:name "MIT"
            :url "https://rem.mit-license.org/"}
  :dependencies [[com.github.mrdimosthenis/synapses_3 "8.0.0"]
                 [org.clojure/clojure "1.10.3"]]
  :java-source-paths ["src/synapeses_clj_jvm"]
  :profiles {:test {:resource-paths ["test-resources"]
                   :dependencies [[org.clojure/data.csv "1.0.0"]]}})
