(ns urbest.ny-schools.tools.text
  "Texte converti en tracés SVG avec fontkit : le rendu ne dépend d'aucune police installée.
  Utilisé par l'image de partage et par la traduction des captures."
  (:require ["fontkit" :as fontkit]
            [clojure.string :as str]))

(defonce ^:private fonts (atom {}))

(defn font
  "Police chargée une seule fois par fichier."
  [file]
  (or (get @fonts file)
      (let [f (fontkit/openSync file)]
        (swap! fonts assoc file f)
        f)))

(defn- scale [^js f size]
  (/ size (.-unitsPerEm f)))

(defn width
  "Largeur en px de text, avec tracking px ajoutés entre les caractères."
  ([f text size] (width f text size 0))
  ([^js f text size tracking]
   (let [run (.layout f text)]
     (+ (* (scale f size) (.-advanceWidth run))
        (* tracking (max 0 (dec (.-length (.-glyphs run)))))))))

(defn extent
  "Hauteur des glyphes au-dessus et au-dessous de la ligne de base, en unités de la police."
  [^js f text]
  (let [bbox (.-bbox (.layout f text))]
    {:above (.-maxY bbox) :below (- (.-minY bbox))}))

(defn line
  "Tracés SVG d'une ligne ; (x, y) = début de la ligne de base."
  ([f text x y size fill] (line f text x y size fill 0))
  ([^js f text x y size fill tracking]
   (let [s         (scale f size)
         run       (.layout f text)
         glyphs    (.-glyphs run)
         positions (.-positions run)]
     (loop [i 0, pen 0, acc []]
       (if (< i (.-length glyphs))
         (let [^js g (aget glyphs i)
               ^js p (aget positions i)
               gx    (+ x (* s (+ pen (.-xOffset p))) (* i tracking))
               gy    (- y (* s (.-yOffset p)))]
           (recur (inc i)
                  (+ pen (.-xAdvance p))
                  (conj acc (str "<path transform=\"translate(" gx " " gy ") scale(" s " " (- s) ")\" d=\""
                                 (.toSVG (.-path g)) "\"/>"))))
         (str "<g fill=\"" fill "\">" (str/join acc) "</g>"))))))

(defn wrap
  "Découpe text en lignes d'au plus max-width px."
  [f text size max-width]
  (reduce (fn [lines word]
            (let [current   (peek lines)
                  candidate (if current (str current " " word) word)]
              (if (and current (> (width f candidate size) max-width))
                (conj lines word)
                (conj (if current (pop lines) lines) candidate))))
          []
          (str/split text #"\s+")))
