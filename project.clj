(defproject com.github.mrdimosthenis/synapses-clj "1.0.0-RC1"
  :description "A neural networks library for Clojure"
  :url "https://github.com/mrdimosthenis/Synapses"
  :license {:name "MIT"
            :url  "https://rem.mit-license.org/"}
  :scm {:name "git" :url "https://github.com/mrdimosthenis/synapses-clj"}
  :dependencies [[com.github.mrdimosthenis/synapses_3 "8.0.0"]
                 [org.clojure/clojure "1.10.3"]]
  :profiles {:test {:resource-paths ["test-resources"]
                    :dependencies   [[org.clojure/data.csv "1.0.0"]]}}
  :deploy-repositories
  [["releases" {:url   "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
                :creds :gpg}
    "snapshots" {:url   "https://oss.sonatype.org/content/repositories/snapshots/"
                 :creds :gpg}]]
  :pom-addition [:developers
                 [:developer
                  [:id "mrdimosthenis"]
                  [:name "Dimos Michailidis"]]])
