(ns urbest.ny-schools.tools.translate-screens
  "Captures en anglais américain (`npm run screens`) : part des captures françaises de screens/fr/,
  efface chaque texte avec la couleur de fond relevée autour, puis écrit la traduction
  (tools/screens_en.cljs) dans la police de l'application (Circular, voir fonts-dirs), à la même taille et dans la
  même couleur. Sorties : docs/en/schools/new-york/assets/screens/ ; avec DEBUG=1, une planche
  target/screens-debug/ où chaque zone traduite est encadrée."
  (:require ["fs" :as fs]
            ["os" :as os]
            ["path" :as path]
            ["sharp" :as sharp]
            [clojure.string :as str]
            [urbest.ny-schools.tools.screens-en :as screens-en]
            [urbest.ny-schools.tools.text :as text]))

(def source-dir "screens/fr")
;; Police de l'application (Circular), utilisée seulement au build. Elle n'est pas versionnée (licence) :
;; screens/fonts/ si présent, sinon les polices installées sur la machine.
(def fonts-dirs ["screens/fonts" (path/join (os/homedir) "Library" "Fonts")])
(def output-dir "docs/en/schools/new-york/assets/screens")
(def debug-dir "target/screens-debug")

(def font-files
  {:book        "CircularStd-Book.otf"
   :medium      "CircularStd-Medium.otf"
   :bold        "CircularStd-Bold.otf"
   :italic      "CircularStd-BookItalic.otf"
   :bold-italic "CircularStd-BoldItalic.otf"})

(defn- font [k]
  (let [file (font-files (or k :book))]
    (if-let [dir (first (filter #(fs/existsSync (path/join % file)) fonts-dirs))]
      (text/font (path/join dir file))
      (throw (js/Error. (str "police introuvable : " file " (screens/fonts/ ou ~/Library/Fonts/)"))))))

;; ── Couleurs ────────────────────────────────────────────────────────────────

(defn- pixel [{:keys [data width channels]} x y]
  (let [i (* channels (+ x (* y width)))]
    [(aget data i) (aget data (+ i 1)) (aget data (+ i 2))]))

(defn- hex [[r g b]]
  (apply str "#" (map #(.padStart (.toString % 16) 2 "0") [r g b])))

(defn- distance [a b]
  (reduce + (map #(js/Math.abs (- %1 %2)) a b)))

(defn- background
  "Couleur la plus fréquente sur le pourtour de la zone (3 px à l'extérieur)."
  [{:keys [width height] :as img} [x y w h]]
  (let [x0 (max 0 (- x 3)) y0 (max 0 (- y 3))
        x1 (min (dec width) (+ x w 3)) y1 (min (dec height) (+ y h 3))
        ring (concat (for [i (range x0 (inc x1)) j [y0 y1]] [i j])
                     (for [j (range y0 (inc y1)) i [x0 x1]] [i j]))]
    (->> ring
         (map (fn [[i j]] (pixel img i j)))
         frequencies
         (apply max-key val)
         key)))

(defn- foreground
  "Couleur du texte d'origine : parmi les pixels les plus éloignés du fond, le plus représentatif."
  [img [x y w h] bg]
  (let [pixels (for [i (range x (+ x w)) j (range y (+ y h))] (pixel img i j))
        sorted (sort-by #(- (distance % bg)) pixels)]
    (nth sorted (min (dec (count sorted)) (quot (count sorted) 40)))))

;; ── Mise en page d'un texte ─────────────────────────────────────────────────

(defn- size-and-tracking
  "Taille (px) qui redonne au texte d'origine la largeur relevée. Les en-têtes en capitales (:track)
  ont des lettres espacées : leur taille et leur espacement sont fixés (:size, :tracking)."
  [{:keys [box fr fr-w track] :as item}]
  (let [^js f (font (:font item))
        upm   (.-unitsPerEm f)]
    (cond
      track        [(or (:size item) 15) (or (:tracking item) 1.4)]
      (:size item) [(:size item) 0]
      :else        [(/ (* (or fr-w (nth box 2)) upm) (text/width f fr upm)) 0])))

(defn- runs [{:keys [en runs lines]}]
  (cond runs  [runs]
        lines (map (fn [l] [[l {}]]) lines)
        :else [[[en {}]]]))

(defn- draw-item
  "SVG d'un texte traduit : rectangle(s) de fond puis tracés du texte."
  [img {:keys [box align color line-height cover] :as item}]
  (let [[x y w h]        box
        ^js base-font    (font (:font item))
        bg               (background img box)
        fg               (or color (hex (foreground img box bg)))
        [size tracking]  (size-and-tracking item)
        ;; Ligne de base telle que le texte d'origine soit centré verticalement dans sa ligne.
        n-lines          (count (runs item))
        line-h           (- h (* (dec n-lines) (or line-height 0)))
        {:keys [above below]} (text/extent base-font (:fr item))
        baseline0        (+ y (/ line-h 2) (* (/ size (.-unitsPerEm base-font)) (/ (- above below) 2)))
        lines            (runs item)
        rendered
        (for [[i line] (map-indexed vector lines)]
          (let [widths (for [[t opts] line] (text/width (font (or (:font opts) (:font item))) t size tracking))
                total  (reduce + widths)
                x0     (case align
                         :center (+ x (/ (- w total) 2))
                         :right  (- (+ x w) total)
                         x)
                y0     (+ baseline0 (* i (or line-height 0)))]
            {:width total
             :x     x0
             :svg   (loop [[[t opts] & more] line, [wd & wds] widths, cx x0, acc ""]
                      (if t
                        (recur more wds (+ cx wd)
                               (str acc (text/line (font (or (:font opts) (:font item))) t cx y0 size
                                                   (or (:color opts) fg) tracking)))
                        acc))}))
        right            (apply max (+ x w) (map #(+ (:x %) (:width %)) rendered))
        left             (apply min x (map :x rendered))]
    {:bg  (hex bg)
     :svg (str (apply str
                      (for [[cx cy cw ch] (cons [(- left 2) (- y 2) (+ (- right left) 4) (+ h 4)] cover)]
                        (str "<rect x=\"" cx "\" y=\"" cy "\" width=\"" cw "\" height=\"" ch "\" fill=\"" (hex bg) "\"/>")))
               (apply str (map :svg rendered))
               ;; Soulignement de l'onglet actif, effacé avec le texte d'origine.
               (when (:underline item)
                 (let [{lx :x lw :width} (first rendered)]
                   (str "<rect x=\"" lx "\" y=\"" (+ y h -3) "\" width=\"" lw "\" height=\"2\" fill=\"" fg "\"/>"))))
     :frame [left (- y 2) (+ (- right left) 4) (+ h 4)]}))

;; ── Image ───────────────────────────────────────────────────────────────────

(defn- shift-overlays
  "Zones de pixels déplacées : la zone d'origine est effacée, sa copie posée dx px plus loin."
  [img file shifts]
  (js/Promise.all
   (clj->js
    (for [{:keys [region dx]} shifts
          :let [[x y w h] region
                bg (hex (background img region))]]
      (-> (sharp (path/join source-dir file))
          (.extract #js {:left x :top y :width w :height h})
          (.png)
          (.toBuffer)
          (.then (fn [buf]
                   {:cover (str "<rect x=\"" x "\" y=\"" y "\" width=\"" w "\" height=\"" h "\" fill=\"" bg "\"/>")
                    :input buf :left (+ x dx) :top y})))))))

(defn- overlay-svg [img drawn moved debug?]
  (str "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" (:width img) "\" height=\"" (:height img) "\">"
       (apply str (map :cover moved))
       (apply str (map :svg drawn))
       (when debug?
         (apply str (for [[fx fy fw fh] (map :frame drawn)]
                      (str "<rect x=\"" fx "\" y=\"" fy "\" width=\"" fw "\" height=\"" fh
                           "\" fill=\"none\" stroke=\"#ff00aa\" stroke-width=\"1\"/>"))))
       "</svg>"))

(defn- write-image [src out svg moved debug?]
  (let [layers (cons {:input (js/Buffer.from svg) :left 0 :top 0}
                     (map #(select-keys % [:input :left :top]) moved))
        image  (.composite (sharp src) (clj->js layers))]
    (-> (if debug? (.png image) (.webp image #js {:quality 90}))
        (.toFile out)
        (.then #(println "Capture traduite :" out)))))

(defn- read-image [src]
  (-> (.toBuffer (.raw (sharp src)) #js {:resolveWithObject true})
      (.then (fn [^js res]
               (let [info (.-info res)]
                 {:data (.-data res) :width (.-width info) :height (.-height info) :channels (.-channels info)})))))

(defn- translate! [file {:keys [items shifts]} debug?]
  (let [src (path/join source-dir file)
        out (if debug?
              (path/join debug-dir (str/replace file ".webp" ".png"))
              (path/join output-dir file))]
    (-> (read-image src)
        (.then (fn [img]
                 (let [drawn (mapv #(draw-item img %) items)]
                   (-> (shift-overlays img file shifts)
                       (.then (fn [moved]
                                (let [moved (js->clj moved :keywordize-keys true)]
                                  (write-image src out (overlay-svg img drawn moved debug?) moved debug?)))))))))))

(defn run []
  (let [debug? (= "1" (.. js/process -env -DEBUG))]
    (fs/mkdirSync (if debug? debug-dir output-dir) #js {:recursive true})
    (-> (js/Promise.all (clj->js (for [[file spec] screens-en/screens] (translate! file spec debug?))))
        (.catch (fn [e]
                  (println "ERREUR traduction des captures :" (.-message e) (.-stack e))
                  (set! (.-exitCode js/process) 1))))))
