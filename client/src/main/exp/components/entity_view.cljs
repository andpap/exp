(ns exp.components.entity-view
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as reagent]
   [exp.subs :as subs]
   [exp.events :as events]
   [clojure.string :as s]
   [reagent-material-ui.core.box :refer [box]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.button-group :refer [button-group]]
   [reagent-material-ui.core.button :refer [button]]
  ))

(declare view)

(defn monaco []
  (let [editor (.. js/window -monaco -editor)
        id (gensym "ed")
        *ed (reagent/atom nil)
        update-layout (fn []
                        (let [element (. js/document getElementById id)
                              width (.. element -offsetWidth)
                              height (.. element -offsetHeight)
                              dimentions (clj->js {:width width :height (- height 0)})]
                        (. ^js/monaco.editor @*ed layout dimentions)))]
    (reagent/create-class
     {:display-name "monaco"

      :component-will-unmount
      (fn [this]
        (. js/window removeEventListener "resize" update-layout))
      
      :component-did-mount
      (fn [this]
        (let [[value] (rest (reagent/argv this))              
              element (. js/document getElementById id)
              options {:value value
                       :fixedOverflowWidgets true
                       ;:lineNumbers "off"
                       :folding false
                       :readOnly false
                       :scrollBeyondLastLine false
                       :roundedSelection false
                       :language "clojure"}
              ed (.create editor element (clj->js options))]
          (reset! *ed ed)
          (. js/window addEventListener "resize" update-layout)
          (. js/window setTimeout update-layout)
        ))

      :reagent-render
      (fn []
        [:div
         {:id id
          :style {:position "absolute"
                  :width "100%"
                  :height "100%"}}])
      })))

(defn my-toolbar [& children]
  (->> children
     (into [button-group
            {:size "small"                          
             :color "primary"
             :variant "contained"}])))

(defn render-module [entity]
  [:<>
;   [my-toolbar
;    [button "run"]]   
     [monaco (:content entity)]]
  )

(defn render-text [entity]
  [:div (:content entity)])

(defn render-entity [entity]
  (let [render (view entity)
        id (:id entity)]
    ^{:key id}
     (render entity)
     ))


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
