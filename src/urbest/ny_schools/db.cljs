(ns urbest.ny-schools.db)

;; ── État initial de la page ─────────────────────────────────────────────────

(def default-db
  {:demo {:status :idle    ;; :idle | :sending | :error | :dev-sent
          ;; false dans le HTML pré-rendu : le bouton d'envoi reste désactivé
          ;; tant que le JavaScript n'a pas pris la main sur le formulaire.
          :ready? false}})
