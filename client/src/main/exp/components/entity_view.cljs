(ns exp.components.entity-view
  (:require
   [re-frame.core :as re-frame]
   [exp.subs :as subs]
   [exp.events :as events]
   [clojure.string :as s]
   [reagent-material-ui.core.box :refer [box]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button-group :refer [button-group]]
   [reagent-material-ui.core.button :refer [button]]
;   [monaco.core :as monaco]
;   [monaco.build :as build]
;   [monaco.js-interop :as j]
;   [mocaco.api.editor :as monaco.editor]
;   [monaco.api.keyboard :as monaco.keyboard]
;   [monaco.api.languages :as monaco.languages]
  ))

(declare view)

(defn my-toolbar [& children]
  (->> children
     (into [button-group
            {:size "small"
             :color "primary"
             :variant "contained"}])))

(defn render-module [entity]
  [box
   [my-toolbar
    [button "run"]]
   [box
     [:pre (:content entity)]]]
  )

(defn render-text [entity]
  [:div (:content entity)])

(defn render-entity [entity]
  (let [render (view entity)
        id (:id entity)]
    ^{:key id}
    [box
     {:border-top 1}
     (render entity)
     ]))

(defn render-org [entity]
  [box
   [my-toolbar   
    [button "run all"]
    [button "+code"]
    [button "+text"]]
    (map render-entity (:entities entity))
   ])

(defmulti view :type)
(defmethod view :module [_] render-module)
(defmethod view :text [_] render-text)
(defmethod view :org [_] render-org)
(defmethod view :default [_] (fn [] [:div ""]))

(defn entity-view-panel []  
  (let [name @(re-frame/subscribe [::subs/active-entity-name])
        entities @(re-frame/subscribe [::subs/entities])
        entity (entities name)]   
    (render-entity entity)))
