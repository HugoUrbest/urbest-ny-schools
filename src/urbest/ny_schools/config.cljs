(ns urbest.ny-schools.config)

;; ── Formulaire de démo ──────────────────────────────────────────────────────
;; Même script Google Apps Script que les autres pages d'urbest.io (landings.js).
;; Le build de dev le remplace par "" : le formulaire n'envoie alors rien.

(goog-define DEMO_ENDPOINT
  "https://script.google.com/macros/s/AKfycbyGeF4GqNzD54UgRsnse5oPaETxMV-cARolW62yQLaWMox1oAkxWGWamP2hXAb7141OCg/exec")

;; Au-delà, l'envoi est abandonné et un message d'erreur s'affiche
;; (le script Google peut mettre plusieurs secondes à répondre à froid).
(goog-define DEMO_TIMEOUT_MS 20000)

;; ── Adresses ────────────────────────────────────────────────────────────────

(def site-url "https://urbest.io")

;; Page anglaise uniquement : pas d'équivalent dans les autres langues du site.
(def lang "en")

;; Chemin de la page une fois intégrée au site (canonical, JSON-LD, sitemap).
(def page-path "/en/schools/new-york/")

(def page-url (str site-url page-path))

;; Ressources rangées à côté de la page : le dossier en/schools/new-york/ se copie tel quel.
(def assets-path "assets/")

(defn asset [& parts]
  (apply str assets-path parts))

(defn asset-url
  "Adresse absolue d'une ressource (image de partage, JSON-LD)."
  [& parts]
  (str page-url (apply asset parts)))

(defn site-page [path]
  (str site-url "/" lang path))

(def demo-success-url (site-page "/demo-request-success/"))

(def capterra-url "https://www.capterra.com/p/193375/Urbest/reviews/")
