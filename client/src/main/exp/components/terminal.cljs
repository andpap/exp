(ns exp.components.terminal
  (:require
   [re-frame.core :as re-frame]
   [reagent.core :as r]
   [exp.subs :as subs]
   [exp.events :as events]
   [clojure.string :as s]
   [reagent-material-ui.core.box :refer [box]]
   [reagent-material-ui.lab.tree-item :refer [tree-item]]
   [reagent-material-ui.lab.tree-view :refer [tree-view]]
   ))

(def templates
  [{:name "--------"
    :value ""}
   {:name "test"
    :value "(+ 1 2 3)"}
   {:name "create-func"
    :value "(defn say-hello [name] (str \"hello from server \" name) )"}
   {:name "exec-func"
    :value "(say-hello \"Andrei\")"}
   ])

(defn index []
  (let [*state (r/atom {:code ""})
        get-code-from-template (fn [e]
                   (let [name (-> e .-target .-value)
                         pred? (comp #{name} :name)
                         value (->> templates
                                    (filter pred?)
                                    (first)
                                    :value)]
                     value))]
    (fn []
      [box
        {:display "flex"
         :flex-direction "column"}

;       [:select
;        {:on-change #(swap! *state assoc :code (get-code-from-template %))}
;        (->> templates
;             (map :name)
;             (map (fn [name]
;                    ^{:key name}
                                        ;                    [:option name])))]
;       [:textarea]
       
       [box
        {:display "flex"}
        [:textarea
         {:value (-> @*state :code)
          :on-change #(swap! *state assoc :code (-> % .-target .-value))
          :rows 5
          :style { :flex-grow 1 }}]
        [:button
         {:style {:background-color "orange"}
          :on-click #(re-frame.core/dispatch [::events/send-code (-> @*state :code)])}
         "send command!"]]])))
