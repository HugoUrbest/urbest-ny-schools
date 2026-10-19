(ns urbest.ny-schools.events
  (:require [clojure.string :as str]
            [re-frame.core :as rf]
            [urbest.ny-schools.config :as config]
            [urbest.ny-schools.db :as db]))

;; ── Init ────────────────────────────────────────────────────────────────────

(rf/reg-event-db
 :init
 (fn [_ _]
   db/default-db))

;; ── Formulaire de démo ──────────────────────────────────────────────────────
;; Même fonctionnement que landings.js sur urbest.io : envoi au script Google,
;; puis redirection vers la page de confirmation anglaise.

(rf/reg-event-db
 :demo-ready
 (fn [db _]
   (assoc-in db [:demo :ready?] true)))

(rf/reg-event-fx
 :submit-demo
 (fn [{:keys [db]} [_ form-data]]
   (when-not (= :sending (get-in db [:demo :status]))
     {:db               (assoc-in db [:demo :status] :sending)
      :urbest/post-demo {:form-data  form-data
                         :on-success [:demo-sent]
                         :on-failure [:demo-failed]
                         :on-dev     [:demo-dev-sent]}})))

(rf/reg-event-fx
 :demo-sent
 (fn [_ _]
   {:urbest/redirect config/demo-success-url}))

(rf/reg-event-db
 :demo-failed
 (fn [db _]
   (assoc-in db [:demo :status] :error)))

(rf/reg-event-db
 :demo-dev-sent
 (fn [db _]
   (assoc-in db [:demo :status] :dev-sent)))

;; ── Effets ──────────────────────────────────────────────────────────────────

(defn- abort-after
  "AbortController annulé au bout de ms millisecondes (AbortSignal.timeout
  n'existe pas sur les navigateurs plus anciens)."
  [ms]
  (let [controller (js/AbortController.)]
    {:signal (.-signal controller)
     :timer  (js/setTimeout #(.abort controller) ms)}))

(rf/reg-fx
 :urbest/post-demo
 (fn [{:keys [form-data on-success on-failure on-dev]}]
   (if (str/blank? config/DEMO_ENDPOINT)
     (do (js/console.info "Demo (dev): form not sent"
                          (js/Object.fromEntries (.entries form-data)))
         (rf/dispatch on-dev))
     ;; Sans délai maximal, une requête bloquée laisserait le bouton sur
     ;; « Sending… » indéfiniment.
     ;; Les champs partent dans l'adresse, comme dans landings.js : c'est ce que lit
     ;; le script Google aujourd'hui (voir REVUE.md, point A5).
     (let [{:keys [signal timer]} (abort-after config/DEMO_TIMEOUT_MS)]
       (-> (js/fetch (str config/DEMO_ENDPOINT "?" (js/URLSearchParams. form-data))
                     #js {:method  "POST"
                          :headers #js {"Content-type" "application/x-www-form-urlencoded; charset=UTF-8"}
                          :signal  signal})
           (.then #(.json %))
           (.then (fn [^js data]
                    (rf/dispatch (if (= "success" (.-result data)) on-success on-failure))))
           (.catch #(rf/dispatch on-failure))
           (.finally #(js/clearTimeout timer)))))))

(rf/reg-fx
 :urbest/redirect
 (fn [url]
   ;; Suivi de conversion Capterra, présent sur les pages d'urbest.io.
   (when-let [track (.-capterraTrackingListener js/window)]
     (track))
   (set! (.-href js/location) url)))
