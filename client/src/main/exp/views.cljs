(ns exp.views
  (:require
   [re-frame.core :refer [dispatch subscribe]]
   [exp.subs :as subs]
   [exp.events :as events]
   [clojure.string :as s]
   [reagent-material-ui.core.app-bar :refer [app-bar]]
   [reagent-material-ui.core.toolbar :refer [toolbar]]
   [reagent-material-ui.core.typography :refer [typography]]
   [reagent-material-ui.core.button :refer [button]]
   [reagent-material-ui.core.box :refer [box]]
   [exp.components.projects-sidebar :refer [projects-panel]]
   [exp.components.entity-view :refer [entity-view-panel]]
   [exp.components.terminal :as terminal-panel]
 ))

;; home

(defn home-panel []
  (let [name (subscribe [::subs/name])]
    [:div
     [:h1 (str "Hello from " @name ". This is the Home Page.")]

     [:div
      [:a {:href "#/about"}
       "go to About Page"]
      [:a {:href "#/workspace"}
           "open workspace"]

      ]
     ]))


;; about

(defn about-panel []
  [:div
   [:h1 "This is the About Page."]

   [:div
    [:a {:href "#/"}
     "go to Home Page"]]])

(defn workspace-panel []
  [box
   {:display "flex"
    :flex-grow 1
     :flex-direction "column"}
     [app-bar
       {:position "static"}
      [toolbar
       {:variant "dense"}
        [typography
         {:variant "h6"
          :class-name "header"}
         "Editor"]]]

     [box
      {:display "flex"
       :flex-grow 1
       :flex-direction "row"}
      
       [box
        {:p 1
         :border 1
         :min-width 200}
        [projects-panel]]

      [box
       {:flex 1
        :display "flex"
        :flex-direction "column"}  
       [box
        {:flex 1
         :position "relative"         
        :p 0}
        [entity-view-panel]]
        [terminal-panel/index]
       ]                
      ]
     ]
    )

(defn- panels [panel-name]
  (case panel-name
    :home-panel [home-panel]
    :about-panel [about-panel]
    :workspace-panel [workspace-panel]
    [:div]))

(defn show-panel [panel-name]
  [panels panel-name])

(defn main-panel []
  (let [active-panel (subscribe [::subs/active-panel])]
    (dispatch [:init-socket])
    (fn []
      [show-panel @active-panel]
    )))
