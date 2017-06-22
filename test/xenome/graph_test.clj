(ns xenome.graph-test
  (:require [clojure.test :refer :all]
            [xenome.spec :as xs]
            [xenome.graph :as xg]
            [clojure.spec.alpha :as s]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check :as tc]
            [clojure.test.check.clojure-test :refer [defspec]]
            [clojure.test.check.properties :as prop]))

(defspec degree-sum-property
  (prop/for-all [x (s/gen ::xs/xenome)]
                (let [nodes (:nodes x)
                      indegree-sum (apply + (map (partial xg/indegree x) nodes))
                      outdegree-sum (apply + (map (partial xg/outdegree x) nodes))
                      total-edges (count (:edges x))]
                  (= indegree-sum outdegree-sum total-edges))))

(defspec topological-sort-ordinality-property
  (prop/for-all [x (s/gen ::xs/xenome)]
                (let [topo (xg/topological-sort x)]
                  (for [[u v] (keys (:edges x))]
                    (< (.indexOf topo u)
                       (.indexOf topo v))))))

(defspec adj-edges-count-property
  (prop/for-all [x (s/gen ::xs/xenome)]
                (let [node (first (:nodes x))]
                  (= (count (xg/adj-edges x node))
                     (xg/outdegree x node)))))
