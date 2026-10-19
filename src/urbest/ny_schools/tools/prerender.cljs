(ns urbest.ny-schools.tools.prerender
  "Génère la page statique docs/en/schools/new-york/index.html et les fichiers d'intégration (`npm run prerender`).

  Tout le contenu est déjà dans le HTML (référencement, affichage immédiat) ;
  le bundle navigateur l'« hydrate » ensuite pour le formulaire.
  Par défaut la page porte une balise noindex (prévisualisation) :
  URBEST_INDEX=1 npm run build la retire pour la mise en ligne sur urbest.io,
  et refuse alors de publier des captures de l'interface française."
  (:require ["crypto" :as crypto]
            ["fs" :as fs]
            ["path" :as path]
            [clojure.string :as str]
            [re-frame.core :as rf]
            [reagent.dom.server :as rds]
            [urbest.ny-schools.events]
            [urbest.ny-schools.styles :as styles]
            [urbest.ny-schools.subs]
            [urbest.ny-schools.tools.checks :as checks]
            [urbest.ny-schools.tools.seo :as seo]
            [urbest.ny-schools.views :as views]))

(def out-dir "docs")
(def page-dir (path/join out-dir "en" "schools" "new-york"))
(def integration-dir (path/join out-dir "integration"))

(defn- production? []
  (= "1" (.. js/process -env -URBEST_INDEX)))

(defn- today []
  (.slice (.toISOString (js/Date.)) 0 10))

(defn- versioned
  "Adresse d'une ressource suivie de l'empreinte de son contenu (?v=…) : un navigateur
  ne peut pas associer une page à jour avec un ancien bundle resté en cache."
  [rel]
  (let [content (fs/readFileSync (path/join page-dir "assets" rel))]
    (str "assets/" rel "?v=" (-> (crypto/createHash "sha1") (.update content) (.digest "hex") (.slice 0 10)))))

;; Au pré-rendu, les abonnements re-frame sont lus hors composant réactif :
;; c'est attendu, on masque donc cet avertissement-là.
(rf/set-loggers!
 {:warn (fn [& args]
          (when-not (str/includes? (str (first args)) "outside of a reactive context")
            (apply js/console.warn args)))})

(defn page []
  (rf/dispatch-sync [:init])
  (str "<!doctype html>\n<html lang=\"en-US\">\n<head>\n"
       (seo/head {:css        (versioned "css/ny-schools.css")
                  :js         (versioned "js/main.js")
                  :indexable? (production?)
                  :modified   (today)})
       "\n</head>\n<body>\n"
       "<div id=\"app\">" (rds/render-to-string [views/root]) "</div>\n"
       "</body>\n</html>\n"))

;; Racine du site de prévisualisation : renvoie vers la page.
(def redirect-page
  "<!doctype html>
<html lang=\"en\">
<head>
<meta charset=\"utf-8\">
<meta name=\"robots\" content=\"noindex\">
<meta http-equiv=\"refresh\" content=\"0; url=en/schools/new-york/\">
<title>New York school districts | Urbest</title>
</head>
<body><p><a href=\"en/schools/new-york/\">Urbest for New York school districts</a></p></body>
</html>
")

(defn- write! [file content]
  (fs/mkdirSync (path/dirname file) #js {:recursive true})
  (fs/writeFileSync file content)
  (println "Fichier généré :" file))

(defn write-css!
  "Feuille de style générée depuis styles.cljs (garden)."
  []
  (write! (path/join page-dir "assets" "css" "ny-schools.css") (styles/css)))

(defn run []
  (write-css!)
  (let [html (page)]
    (-> (checks/screen-errors (path/join page-dir "assets" "screens") (production?))
        (.then
         (fn [screen-errors]
           (let [errors (concat (checks/copy-errors) (checks/page-errors html) screen-errors)]
             (if (seq errors)
               (do (doseq [e errors] (println "ERREUR" e))
                   (println "Page non générée.")
                   (set! (.-exitCode js/process) 1))
               (let [modified (today)]
                 (write! (path/join page-dir "index.html") html)
                 (write! (path/join out-dir "index.html") redirect-page)
                 (write! (path/join out-dir ".nojekyll") "")
                 (write! (path/join integration-dir "sitemap-schools.xml") (seo/sitemap modified))
                 (write! (path/join integration-dir "llms-entry.md") (seo/llms-entry))
                 (write! (path/join integration-dir "robots-line.txt") seo/robots-line)
                 (println (if (production?)
                            "Page indexable (URBEST_INDEX=1)."
                            "Page en noindex (prévisualisation). URBEST_INDEX=1 pour la production.")))))))
        (.catch (fn [e]
                  (println "ERREUR" (.-message e))
                  (set! (.-exitCode js/process) 1))))))
