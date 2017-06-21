(ns xenome.validations
  "Functions that validate xenomes."
  (:require [clojure.set :refer [subset?]]))

(defn edges-are-a-subset-of-nodes?
  "Checks that for all edge in :edges, the nodes that make
  up that edge exist in :nodes."
  [x]
  (subset? (into #{} (apply concat (keys (:edges x))))
           (:nodes x)))

(defn sum-in-out-count-lte-node-count?
  "Checks that the sum of the input and output counts are less
  than or equal to the node count."
  [{:keys [input-count output-count nodes]}]
  (<= (+ input-count output-count)
      (count nodes)))

(defn innovations-are-distinct?
  "Checks that all innovation numbers are distinct within a
  xenome."
  [x]
  (let [inns (map :innovation (vals (:edges x)))]
    (or (empty? inns)
        (apply distinct? inns))))
