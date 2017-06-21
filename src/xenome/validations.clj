(ns xenome.validations
  "Functions that validate xenomes."
  (:require [clojure.set :as cset]))

(defn edges-are-a-subset-of-nodes?
  "Checks that for every node within :edges, the node exists
  in :nodes."
  [x]
  (cset/subset? (into #{} (apply concat (keys (:edges x))))
                (:nodes x)))

(defn sum-in-out-count-lte-node-count?
  "Checks that the sum of the input and output counts are less
  than or equal to the node count."
  [x]
  (<= (+ (:input-count x) (:output-count x))
      (count (:nodes x))))

(defn innovations-are-distinct?
  "Checks that all innovation numbers are distinct within a
  xenome."
  [x]
  (let [inns (map :innovation (vals (:edges x)))]
    (or (empty? inns)
        (apply distinct? inns))))
