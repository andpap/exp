(ns exp.db
  (:require
   [clojure.string :as str]
   [clojure.spec.alpha :as s]))

(s/def ::id string?)
(s/def ::type keyword?)
(s/def ::content string?)
(s/def ::entity
  (s/keys :req-un [::id ::type ::content]))
(s/def ::entities
  (s/map-of string? ::entity))

(def default-db
  {:name "re-frame"
   :entities {}})
