(ns exp.events
  (:require
   [re-frame.core :refer [reg-event-db reg-event-fx dispatch]]
   [exp.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))


(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(defonce *socket (atom nil))

(defn init-socket []
  (swap! *socket
         (fn [_]
           (let [socket     (js/WebSocket. "ws://localhost:8080/ws")
                 on-message (fn [event]
                              (let [data (.. event -data)
                                    data (js/JSON.parse data)
                                    data (js->clj data :keywordize-keys true)
                                    type (:type data)
                                    payload (:payload data)]
                                (dispatch [(keyword type) payload])))]
             (.addEventListener socket "message" on-message)
             socket))))

(reg-event-fx 
 :init-socket
 (fn [corf _]
   (init-socket)
   nil))


(reg-event-db
 ::set-active-panel
 (fn [db [_ active-panel]]
            (assoc db :active-panel active-panel)))

(reg-event-fx
 ::set-active-entity-name
 (fn [cofx [_ v]]
   {:db (assoc (:db cofx) :active-entity-name v)
    :fx [[:dispatch [::get-module v]]]}))

(reg-event-fx
 ::get-module
 (fn [cofx [_ v]]
   (let [v {:type :get-module
            :payload v}
         v (clj->js v)]
     (.send @*socket (js/JSON.stringify v))
   )))

(reg-event-fx
 ::send-module
 (fn [cofx [_ v]]
   (js/console.log "send-module")))

(reg-event-fx
  ::send-code
  (fn [cofx [_ v]]
    (let [v {:type :code
             :payload v}
          v (clj->js v)]
      (.send @*socket (js/JSON.stringify v)))))

(reg-event-fx
  :result
  (fn [cofx [_ v]]
    (js/console.log v)))

(reg-event-db
 :project
 (fn [db [_ payload]]
   (let [func (fn [acc {:keys [name type content]}]
                (-> acc
                    (assoc-in [name :name] name)
                    (assoc-in [name :type] (keyword type))))
         entities (reduce func (:entities db) payload)]
     (assoc db :entities entities))))

(reg-event-db
 :module
 (fn [db [_ payload]]
   (assoc-in db [:entities (:name payload)] payload)))
