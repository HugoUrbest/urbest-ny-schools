(ns urbest.ny-schools.tools.checks
  "Contrôles avant publication : le build échoue plutôt que de publier une page hors charte."
  (:require ["fs" :as fs]
            ["path" :as path]
            ["sharp" :as sharp]
            [clojure.string :as str]
            [urbest.ny-schools.content :as content]))

;; ── Copy core (skill urbest-voice) ──────────────────────────────────────────

(def banned
  "Jargon survendeur et formules de remplissage bannis par le copy core, en anglais."
  [#"(?i)\brevolution\w*" #"(?i)\binnovative\b" #"(?i)\bdisruptive\b" #"(?i)\bgame[- ]?changer\b"
   #"(?i)\bleader\b" #"(?i)\bleading\b" #"(?i)\bturn-?key\b" #"(?i)\bbest-in-class\b" #"(?i)\bworld-class\b"
   #"(?i)\bcutting-edge\b" #"(?i)\bseamless(ly)?\b" #"(?i)\bdo(n't| not) hesitate\b"])

(def competitors
  "Le copy core interdit de dénigrer un concurrent nommé : aucun n'est cité sur la page."
  ["MaintainX" "Brightly" "SchoolDude" "UpKeep" "Limble" "Fiix" "eMaint" "Incident IQ"])

(defn- strings
  "Tous les textes d'une structure de données (clés comprises)."
  [x]
  (filter string? (tree-seq coll? seq x)))

(defn copy-errors []
  (let [texts (strings [content/text content/checks content/faq content/screens content/software-features])]
    (concat
     (for [s texts, re banned :let [m (re-find re s)] :when m]
       (str "mot banni par le copy core « " (if (string? m) m (first m)) " » dans : " s))
     (for [s texts, c competitors :when (str/includes? (str/lower-case s) (str/lower-case c))]
       (str "concurrent nommé (" c ") dans : " s)))))

;; ── Rendu ───────────────────────────────────────────────────────────────────

(defn page-errors
  "Une clé de texte inconnue s'affiche « ⟨clé⟩ » (voir content/t)."
  [html]
  (for [k (distinct (re-seq #"⟨[^⟩]*⟩" html))]
    (str "clé de texte inconnue " k)))

;; ── Captures d'écran ────────────────────────────────────────────────────────

(defn- screen-check
  "Promesse des erreurs d'une capture (vecteur JS de textes)."
  [screens-dir production? [id {:keys [file width height lang]}]]
  (let [f        (path/join screens-dir file)
        lang-err (when (and production? (= :fr lang))
                   (str "capture en français : " file " (" (name id) "). "
                        "Remplacer par une capture de l'interface anglaise, puis :lang :en dans content.cljs."))]
    (if-not (fs/existsSync f)
      (js/Promise.resolve #js [(str "capture absente : " f)])
      (-> (.metadata (sharp f))
          (.then (fn [^js m]
                   (let [size-err (when (not= [width height] [(.-width m) (.-height m)])
                                    (str "capture " file " : " (.-width m) "×" (.-height m)
                                         " px, mais " width "×" height " dans content.cljs"))]
                     (clj->js (remove nil? [size-err lang-err])))))))))

(defn screen-errors
  "Promesse d'une liste d'erreurs : fichier absent, dimensions différentes de content.cljs,
  et, en production, capture encore prise sur l'interface française."
  [screens-dir production?]
  (-> (js/Promise.all (clj->js (map #(screen-check screens-dir production? %) content/screens)))
      (.then #(vec (apply concat (js->clj %))))))
