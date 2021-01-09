(ns exp.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::entities
 (fn [db]
   (:entities db)))

(re-frame/reg-sub
 ::active-entity-name 
 (fn [db]
   (:active-entity-name db)))

(re-frame/reg-sub
 ::active-panel
 (fn [db _]
   (:active-panel db)))
