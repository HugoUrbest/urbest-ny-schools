(ns urbest.ny-schools.tools.cli
  "Point d'entrée des outils de build : node target/tools.js (css|screens|og-image|prerender)."
  (:require [urbest.ny-schools.tools.og-image :as og-image]
            [urbest.ny-schools.tools.prerender :as prerender]
            [urbest.ny-schools.tools.translate-screens :as translate-screens]))

(defn main [& [command]]
  (case command
    "css"       (prerender/write-css!)
    "screens"   (translate-screens/run)
    "og-image"  (og-image/run)
    "prerender" (prerender/run)
    (do (println "Usage : node target/tools.js css|screens|og-image|prerender")
        (set! (.-exitCode js/process) 1))))
