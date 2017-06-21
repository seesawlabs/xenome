(ns xenome.core-test
  (:require [clojure.test :refer :all]
            [xenome.core :refer :all]))

(deftest sanity-test
  (testing "Our own sanity."
    (is (= 0 0))))
