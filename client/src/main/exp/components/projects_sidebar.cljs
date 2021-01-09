(ns exp.components.projects-sidebar 
  (:require
   [re-frame.core :as re-frame]
   [exp.subs :as subs]
   [exp.events :as events]
   [clojure.string :as s]
   [reagent-material-ui.lab.tree-item :refer [tree-item]]
   [reagent-material-ui.lab.tree-view :refer [tree-view]]
   ))

(defn render-files-tree
  ([tree-data]
   (render-files-tree tree-data ""))
  ([tree-data prefix]
    (when tree-data
      (for [name (keys tree-data)          
            :let [id (str prefix "/" name)
                  sub-tree-data (tree-data name)]]
        ^{:key id}
        [tree-item
         {:node-id id
          :label name}
         (render-files-tree sub-tree-data id)]))))

(defn projects-panel []
  (let [entities @(re-frame/subscribe [::subs/entities])
        paths (keys entities)        
        path->vec (fn [path]
                    (let [v (s/split path #"/")
                          v (filter (complement s/blank?) v)
                          v (vec v)]
                      v))
        tree-data (reduce #(assoc-in %1 (path->vec %2) false) {} paths)]
    [tree-view
     {:on-node-select #(re-frame.core/dispatch [::events/set-active-entity-name %2])}
     (render-files-tree tree-data)]))
