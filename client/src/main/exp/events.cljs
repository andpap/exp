(ns exp.events
  (:require
   [re-frame.core :as re-frame]
   [exp.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
            (assoc db :active-panel active-panel)))

(re-frame/reg-event-db
 ::set-active-entity-name
 (fn-traced [db [_ active-entity-name]]
            (assoc db :active-entity-name active-entity-name)))
