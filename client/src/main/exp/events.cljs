(ns exp.events
  (:require
   [re-frame.core :refer [reg-event-db]]
   [exp.db :as db]
   [day8.re-frame.tracing :refer-macros [fn-traced]]
   ))

(reg-event-db
 ::initialize-db
 (fn-traced [_ _]
   db/default-db))

(reg-event-db
 ::set-active-panel
 (fn-traced [db [_ active-panel]]
            (assoc db :active-panel active-panel)))

(reg-event-db
 ::set-active-entity-name
 (fn-traced [db [_ active-entity-name]]
            (assoc db :active-entity-name active-entity-name)))

(defn file-update [payload]
  (reduce #(assoc %1
                  (:name %2)
                  {:id (:name %2)
                   :type :module
                   :content (str "(def name \"" (:name %2) "\")")})
          {}
          payload))

(reg-event-db
 :server-action
 (fn [db [_ data]]
   (let [type (:type data)
         payload (:payload data)]
     (when (= "files-update" type)
       (assoc db :entities (file-update payload))))))
