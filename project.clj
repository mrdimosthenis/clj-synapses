(defproject org.clojars.mrdimosthenis/clj-synapses "1.0.4"
  :description "A neural networks library for Clojure"
  :url "https://github.com/mrdimosthenis/Synapses"
  :license {:name "MIT"
            :url  "https://rem.mit-license.org/"}
  :scm {:name "git" :url "https://github.com/mrdimosthenis/clj-synapses"}
  :dependencies [[com.github.mrdimosthenis/synapses_3 "8.0.0"]
                 [org.clojure/clojure "1.10.3"]]
  :profiles {:test {:resource-paths ["test-resources"]
                    :dependencies   [[org.clojure/data.csv "1.0.0"]]}}
  :pom-addition [:developers
                 [:developer
                  [:id "mrdimosthenis"]
                  [:name "Dimos Michailidis"]]]
  :deploy-repositories [["clojars" {:url           "https://clojars.org/repo"
                                    :username      :env/clojars_username
                                    :password      :env/clojars_password
                                    :sign-releases false}]])
