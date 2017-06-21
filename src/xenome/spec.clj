(ns xenome.spec
  "clojure.spec defintions for xenomes."
  (:require [clojure.spec.alpha :as s]
            [clojure.spec.gen.alpha :as gen]
            [clojure.test.check.generators :as gens]
            [clojure.set :as cset]
            [xenome.validations :as v]))

;""""""""""""""""""""""""""""""""""""""""
;
; Generators
;
;""""""""""""""""""""""""""""""""""""""""
(defn innovation-gen
  "Generator for innovations, guaranteed to be unique."
  []
  (let [inn-atom (atom 0)]
    (gen/fmap #(swap! % inc)
              (gen/return inn-atom))))

(defn edge-gen
  "Given a collection of nodes, returns a generator for edges
  between those nodes."
  [nodes]
  (gen/one-of [(s/gen ::edges {::node #(gen/elements nodes)
                               ::innovation innovation-gen})
               (gen/return {})]))

(defn count-gen
  "Given a collection of nodes, returns a generator that generates
  valid input/output counts for those nodes."
  [nodes]
  (gen/fmap (fn [nodes]
              (-> (count nodes)
                  (/ 2)
                  int
                  rand-int
                  inc))
            (gen/return nodes)))

(defn xenome-gen
  "Generator for genomes."
  []
  (gens/let [nodes (s/gen ::nodes)
             edges (edge-gen nodes)
             input-count (count-gen nodes)
             output-count (count-gen nodes)]
    {:nodes nodes
     :edges edges
     :input-count input-count
     :output-count output-count}))

;""""""""""""""""""""""""""""""""""""""""
;
; Specifications
;
;""""""""""""""""""""""""""""""""""""""""

(s/def ::node pos-int?)
(s/def ::node-set (s/coll-of ::node :kind set? :into #{}))
(s/def ::nodes (s/coll-of ::node :kind sorted? :into (sorted-set) :min-count 2))
(s/def ::source-node ::node)
(s/def ::target-node ::node)
(s/def ::edge (s/tuple ::source-node ::target-node))
(s/def ::weight (s/double-in :NaN? false))
(s/def ::innovation pos-int?)
(s/def ::enabled? boolean?)
(s/def ::edge-data (s/keys :req-un [::weight ::innovation ::enabled?]))
(s/def ::edges (s/map-of ::edge ::edge-data))
(s/def ::node-count pos-int?)
(s/def ::input-count ::node-count)
(s/def ::output-count ::node-count)
(s/def ::xenome (s/with-gen (s/and (s/keys :req-un [::nodes
                                                    ::edges
                                                    ::input-count
                                                    ::output-count])
                                   v/edges-are-a-subset-of-nodes?
                                   v/sum-in-out-count-lte-node-count?
                                   v/innovations-are-distinct?)
                  xenome-gen))
