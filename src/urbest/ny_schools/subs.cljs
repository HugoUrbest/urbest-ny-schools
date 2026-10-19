(ns urbest.ny-schools.subs
  (:require [re-frame.core :as rf]))

(rf/reg-sub
 :demo-status
 (fn [db _] (get-in db [:demo :status])))

(rf/reg-sub
 :demo-ready?
 (fn [db _] (get-in db [:demo :ready?])))
