(ns urbest.ny-schools.brand
  "Charte Urbest (skill urbest-brand, issu du brandbook) : palette, police et bulle de dialogue.
  Seule source des couleurs et de la police pour les vues, la feuille de style et l'image de partage.")

;; ── Palette officielle ───────────────────────────────────────────────────────
;; Fond par défaut : blanc ou gris très clair ; texte bleu nuit sur fond clair, blanc sur fond sombre.

(def palette
  {:navy  "#130046"   ; texte principal, titres, fonds sombres
   :blue  "#1d6fff"   ; éléments principaux ; 4,40:1 sur blanc, réservé aux grands chiffres et aux décors
   :cyan  "#38d1dd"   ; appels à l'action, soulignements
   :mint  "#5ae5b7"   ; éléments secondaires, icônes
   :green "#a9f7ae"   ; accents, fonds doux
   :white "#ffffff"
   :gray  "#f5f5f5"})

(defn color [k] (get palette k))

;; ── Typographie ─────────────────────────────────────────────────────────────
;; Proxima Nova, seule police de la charte : Black 900 pour les grands titres, Bold 700 pour les
;; sous-titres et libellés, Regular 400 pour le texte. Les polices système ne servent que pendant
;; le chargement des fichiers ou s'ils échouent.

(def font-family "\"Proxima Nova\"")

(def font-stack
  (str font-family ", -apple-system, BlinkMacSystemFont, \"Segoe UI\", Roboto, \"Helvetica Neue\", Arial, sans-serif"))

(def font-files
  {400 "proxima-nova-regular.woff2"
   700 "proxima-nova-bold.woff2"
   900 "proxima-nova-black.woff2"})

;; ── Bulle de dialogue ───────────────────────────────────────────────────────
;; Élément graphique issu du logo déconstruit : boîte de 100 × 92, pointe en bas à gauche.

(def bubble-path "M54 92A46 46 0 1 0 8 46C8 66 6 81 0 90Q-1.5 92 2 92Z")
