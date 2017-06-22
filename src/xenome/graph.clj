(ns xenome.graph
  "General directed graph operations."
  (:require [clojure.set :as cset]
            [clojure.spec.alpha :as s]
            [xenome.spec :as xs]))

;""""""""""""""""""""""""""""""""""""""""
;
; Directed xenome operations
;
;""""""""""""""""""""""""""""""""""""""""

(defn indegree
  "Returns the indegree of the given node, where indegree
  is defined as the number of edges that terminate at a given
  node. Returns nil if the given node is not present in the
  xenome."
  [xenome n]
  (when ((:nodes xenome) n)
    (->> (keys (:edges xenome))
         (filter #(= (second %) n))
         count)))

(s/fdef indegree
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret (s/nilable nat-int?))

(defn outdegree
  "Returns the outdegree of the given node, where outdegree
  is defined as the number of edges that originate from a given
  node. Returns nil if the given node is not present in the
  xenome."
  [xenome n]
  (when ((:nodes xenome) n)
    (->> (keys (:edges xenome))
         (filter #(= (first %) n))
         count)))

(s/fdef outdegree
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret (s/nilable nat-int?))

(defn adj?
  "Returns true if x is adjacent to y in the given xenome, where
  adjacent is defined as the existence of an edge originating
  from x and terminating at y."
  [xenome x y]
  (contains? (:edges xenome) [x y]))

(s/fdef adj?
        :args (s/cat :xen ::xs/xenome :x ::xs/node :y ::xs/node)
        :ret boolean?)

(defn adj-edges
  "Returns the set of all edges that n is adjacent to, meaning
  all edges that originate from n."
  [xenome n]
  (into #{} (filter #(= n (first %)) (keys (:edges xenome)))))

(s/fdef adj-edges
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret ::xs/edge-set
        :fn #(= (or (outdegree (-> % :args :xen) (-> % :args :n))
                    0)
                (count (:ret %))))

(defn source?
  "Returns true if the given node is a source, where source
  is defined as having an indegree count of zero, and a non-zero
  outdegree count."
  [xenome n]
  (and (zero? (indegree xenome n))
       (pos? (outdegree xenome n))))

(s/fdef source?
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret boolean?)

(defn sink?
  "Returns true if the given node is a sink, where sink
  is defined as having an outdegree count of zero, and a
  non-zero indegree count."
  [xenome n]
  (and (zero? (outdegree xenome n))
       (pos? (indegree xenome n))))

(s/fdef sink?
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret boolean?)

(defn internal?
  "Returns true if the given node is internal, where
  internal is defined as having a non-zero indegree and outdegree."
  [xenome n]
  (and (pos? (indegree xenome n))
       (pos? (outdegree xenome n))))

(s/fdef internal?
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret boolean?)

(defn stranded?
  "Returns true if the given node is stranded, meaning that
  both its indegree and outdegree are zero."
  [xenome n]
  (and (zero? (indegree xenome n))
       (zero? (outdegree xenome n))))

(s/fdef stranded?
        :args (s/cat :xen ::xs/xenome :n ::xs/node)
        :ret boolean?)

(defn topological-sort
  "Returns a vector of the given xenome's nodes in topological order,
  or nil if the xenome can not be sorted (e.g. it contains a cycle).
  Uses Kahn's algorithm to compute the sort."
  [{:keys [nodes] :as xenome}]
  (loop [l []
         q (into clojure.lang.PersistentQueue/EMPTY
                 (filter #(zero? (indegree xenome %)) nodes))
         x xenome]
    (if (empty? q)
      (when (empty? (:edges x)) l)
      (let [n (peek q)
            [new-q new-x] (reduce (fn [[q x] e]
                                    (let [[_ m] e
                                          x (update x :edges dissoc e)
                                          m-in (indegree x m)]
                                      [(if (zero? m-in) (conj q m) q) x]))
                                  [q x]
                                  (adj-edges x n))]
        (recur (conj l n) (pop new-q) new-x)))))

(s/fdef topological-sort
        :args (s/cat :xen ::xs/xenome)
        :ret (s/nilable (s/coll-of ::xs/node :kind vector?))
        :fn (fn [%] (or (nil? (:ret %))
                        (and (= (count (-> % :args :xen :nodes))
                                (count (:ret %)))
                             (every? true? (for [[u v] (-> % :args :xen :edges keys)]
                                             (< (.indexOf (:ret %) u)
                                                (.indexOf (:ret %) v))))))))
(defn cyclic?
  "Returns true if the given xenome contains a cycle, false otherwise."
  [xenome]
  (nil? (topological-sort xenome)))

(s/fdef cyclic?
        :args (s/cat :xen ::xs/xenome)
        :ret boolean?)
