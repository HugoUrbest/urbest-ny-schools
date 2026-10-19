(ns urbest.ny-schools.core
  (:require [re-frame.core :as rf]
            [reagent.dom.client :as rdc]
            [urbest.ny-schools.events]
            [urbest.ny-schools.subs]
            [urbest.ny-schools.views :as views]))

;; La page publiée est pré-rendue (npm run build) : on « hydrate » le HTML existant.
;; La page de dev (dev.html) est vide : on rend tout côté navigateur.

(defonce root (atom nil))

(defn- mount! []
  (let [el (js/document.getElementById "app")]
    (reset! root (if (.hasChildNodes el)
                   (rdc/hydrate-root el [views/root])
                   (doto (rdc/create-root el) (rdc/render [views/root]))))))

(defn ^:dev/after-load reload! []
  (rf/clear-subscription-cache!)
  (rdc/render @root [views/root]))

(defn init []
  (rf/dispatch-sync [:init])
  (mount!))
