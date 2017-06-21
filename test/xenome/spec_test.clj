(ns xenome.spec-test
  (:require [clojure.test :refer :all]
            [clojure.spec.alpha :as s]
            [clojure.test.check.properties :as prop]
            [clojure.test.check.clojure-test :refer [defspec]]
            [xenome.spec :as xs]))

(defspec xenome-spec
  (prop/for-all
   [x (xs/xenome-gen)]
   (s/valid? ::xs/xenome x)))
