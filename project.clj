(defproject xenome "0.1.0-SNAPSHOT"
  :description "NeuroEvolution of Augmenting Topologies (NEAT) for Clojure"
  :url "https://github.com/seesawlabs/xenome"
  :license {:name "GNU General Public License v3.0"
            :url "http://www.gnu.org/licenses/"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha17"]]
  :monkeypatch-clojure-test false
  :plugins [[lein-auto "0.1.3"]]
  :profiles  {:dev 
              {:dependencies [[org.clojure/test.check "0.9.0"]
                              [org.clojure/tools.namespace "0.2.11"]
                              [orchestra "0.3.0"]]
               :source-paths ["src" "dev"]}})
