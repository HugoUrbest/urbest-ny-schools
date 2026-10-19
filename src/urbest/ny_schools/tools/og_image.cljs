(ns urbest.ny-schools.tools.og-image
  "Image de partage 1200 × 630 en PNG (`npm run og-image`) : fond bleu nuit, bulles de la charte,
  logo blanc officiel et titre de la page en Proxima Nova.

  Le texte est converti en tracés avec fontkit, à partir des fichiers WOFF2 de la page :
  le rendu ne dépend d'aucune police installée sur la machine. Pas de capture d'écran,
  pour ne pas montrer l'interface française en vignette."
  (:require ["fs" :as fs]
            ["path" :as path]
            ["sharp" :as sharp]
            [clojure.string :as str]
            [urbest.ny-schools.brand :as brand]
            [urbest.ny-schools.content :refer [t]]
            [urbest.ny-schools.tools.seo :as seo]
            [urbest.ny-schools.tools.text :as text]))

(def assets-dir "docs/en/schools/new-york/assets")

(def bubble-path brand/bubble-path)

(defn- bubble [{:keys [x y size fill flip? opacity] :or {opacity 1}}]
  (let [s (/ size 100)]
    (str "<path d=\"" bubble-path "\" fill=\"" fill "\" opacity=\"" opacity "\" transform=\""
         (if flip?
           (str "translate(" x " " y ") scale(" (- s) " " s ") translate(-100 0)")
           (str "translate(" x " " y ") scale(" s ")"))
         "\"/>")))

;; ── Texte ─────────────────────────────────────────────────────────────────

(defn- font [weight]
  (text/font (path/join assets-dir "fonts" (str "proxima-nova-" weight ".woff2"))))

;; ── Logo ────────────────────────────────────────────────────────────────────

(defn- logo
  "Logo blanc officiel, inclus tel quel (couleurs et typographie inchangées)."
  [x y width]
  (let [svg        (str (fs/readFileSync (path/join assets-dir "img" "urbest-blanc.svg")))
        [_ vb]     (re-find #"viewBox=\"([^\"]+)\"" svg)
        [_ _ w h]  (map js/parseFloat (str/split vb #"\s+"))
        inner      (-> svg
                       (str/replace #"(?s)^.*?<svg[^>]*>" "")
                       (str/replace #"(?s)</svg>\s*$" "")
                       (str/replace #"(?s)<title>.*?</title>" ""))]
    (str "<svg x=\"" x "\" y=\"" y "\" width=\"" width "\" height=\"" (* width (/ h w)) "\" viewBox=\"" vb "\">"
         inner "</svg>")))

;; ── Image ───────────────────────────────────────────────────────────────────

(defn svg []
  (let [black    (font "black")
        bold     (font "bold")
        size     70
        leading  78
        x        80
        lines    (text/wrap black (t :og-title) size 640)
        top      262]
    (str "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"1200\" height=\"630\" viewBox=\"0 0 1200 630\">"
         "<rect width=\"1200\" height=\"630\" fill=\"" (brand/color :navy) "\"/>"
         (bubble {:x 700 :y -90 :size 600 :fill (brand/color :blue) :opacity 0.55 :flip? true})
         (bubble {:x 780 :y 380 :size 200 :fill (brand/color :cyan)})
         (bubble {:x 1050 :y 430 :size 120 :fill (brand/color :mint) :flip? true})
         (bubble {:x 720 :y 96 :size 64 :fill (brand/color :green)})
         (logo x 64 210)
         (str/join (map-indexed (fn [i l] (text/line black l x (+ top (* i leading)) size (brand/color :white))) lines))
         (text/line bold "For New York school districts" x (+ top (* (count lines) leading) 26) 32 (brand/color :green))
         "</svg>")))

(defn run []
  (let [out (path/join assets-dir (:path seo/og-image))]
    (fs/mkdirSync (path/dirname out) #js {:recursive true})
    (-> (sharp (js/Buffer.from (svg)))
        (.png #js {:compressionLevel 9})
        (.toFile out)
        (.then #(println "Image de partage générée :" out))
        (.catch (fn [e]
                  (println "ERREUR image de partage :" (.-message e))
                  (set! (.-exitCode js/process) 1))))))
