(ns xenome.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.test.check :as tc]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [xenome.spec :as xs]
            [xenome.validations :as v]))

(defspec xenome-spec 50
  (prop/for-all
   [x (xs/xenome-gen)]
   (and
    (s/valid? ::xs/xenome x)
    (v/edges-are-a-subset-of-nodes? x)
    (v/sum-in-out-count-lte-node-count? x)
    (v/innovations-are-distinct? x))))
