(ns user
  (:require [clojure.pprint :refer [pprint pp]]
            [clojure.repl :refer :all]
            [clojure.tools.namespace.repl :refer  [refresh]]
            [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.spec.test.alpha :as stest]
            [clojure.test.check :as tc]
            [orchestra.spec.test :as o]
            [xenome.core :as x]
            [xenome.spec :as xs]
            [xenome.graph :as xg]
            [xenome.neural :as xn]))

(o/instrument)
