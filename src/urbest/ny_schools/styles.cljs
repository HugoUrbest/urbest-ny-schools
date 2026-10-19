(ns urbest.ny-schools.styles
  "Feuille de style de la page, écrite en Clojure (garden) à partir de la charte (brand.cljs).
  Le build l'écrit dans docs/en/schools/new-york/assets/css/ny-schools.css (`node target/tools.js css`).

  Contrastes (WCAG AA) : le bleu ne fait que 4,40:1 sur blanc ; il est réservé aux grands chiffres et aux
  décors. Texte et liens en bleu nuit, boutons en Cyan avec texte bleu nuit (10,15:1).
  Les déclarations sont regroupées par 8 au plus : au-delà, une map Clojure ne garde pas l'ordre, qui
  compte en CSS (propriété raccourcie puis détaillée)."
  (:require [garden.core :as garden]
            [garden.stylesheet :refer [at-font-face at-media]]
            [urbest.ny-schools.brand :as brand]))

;; ── Polices ─────────────────────────────────────────────────────────────────

(def font-faces
  (for [[weight file] (sort brand/font-files)]
    (at-font-face {:font-family  brand/font-family
                   :src          (str "url(\"../fonts/" file "\") format(\"woff2\")")
                   :font-weight  (str weight)
                   :font-style   "normal"
                   :font-display "swap"})))

;; ── Variables ───────────────────────────────────────────────────────────────

(def root
  [":root"
   (into {} (for [[k v] brand/palette] [(str "--" (name k)) v]))
   {"--text"          "var(--navy)"
    ;; Transparences du bleu nuit : 10,2:1 sur blanc, 9,6:1 sur gris.
    "--text-muted"    "rgb(19 0 70 / 0.78)"
    "--text-on-dark"  "rgb(255 255 255 / 0.82)"
    "--line"          "rgb(19 0 70 / 0.14)"
    "--font"          brand/font-stack}
   {"--container"     "1200px"
    "--gutter"        "clamp(16px, 4vw, 40px)"
    "--section-space" "clamp(72px, 10vw, 128px)"
    "--radius"        "20px"
    ;; Coin inférieur gauche en pointe, comme la bulle de dialogue du logo.
    "--bubble-radius" "28px 28px 28px 6px"
    "--bubble"        (str "url(\"data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='-2 0 102 92'%3E%3Cpath d='"
                           brand/bubble-path "'/%3E%3C/svg%3E\")")}])

;; ── Règles ──────────────────────────────────────────────────────────────────

(def rules
  [;; ── Base ─────────────────────────────────────────────────────────────────
   ["*, *::before, *::after"
    {:box-sizing "border-box"}]
   ["html"
    {:-webkit-text-size-adjust "100%"
     :scroll-behavior "smooth"}]
   ["body"
    {:margin "0"
     :background "var(--white)"
     :color "var(--text)"
     :font-family "var(--font)"
     :font-size "1rem"
     :line-height "1.6"
     :-webkit-font-smoothing "antialiased"
     :-moz-osx-font-smoothing "grayscale"}]
   ;; Titres de section à 3× le corps du texte sur grand écran (52 px pour 17 px).
   (at-media {:min-width "960px"}
     ["body"
      {:font-size "1.0625rem"}])
   ["img, svg"
    {:display "block"
     :max-width "100%"}]
   ["a"
    {:color "inherit"
     :text-decoration-thickness "1px"
     :text-underline-offset "3px"}]
   ["a:hover"
    {:text-decoration-color "var(--cyan)"
     :text-decoration-thickness "2px"}]
   ["h1, h2, h3, h4, h5, p, ol, ul, figure, blockquote"
    {:margin "0"}]
   ["ol, ul"
    {:padding "0"
     :list-style "none"}]
   ["h1, h2, h3, h4, h5"
    {:line-height "1.08"
     :letter-spacing "-0.015em"
     :text-wrap "balance"}]
   [":focus-visible"
    {:outline "3px solid var(--blue)"
     :outline-offset "3px"}]
   ["section[id]"
    {:scroll-margin-top "16px"}]
   [".container"
    {:width "100%"
     :max-width "calc(var(--container) + 2 * var(--gutter))"
     :margin-inline "auto"
     :padding-inline "var(--gutter)"}]
   [".visually-hidden"
    {:position "absolute !important"
     :width "1px"
     :height "1px"
     :margin "-1px"
     :padding "0"
     :overflow "hidden"
     :clip "rect(0 0 0 0)"
     :white-space "nowrap"}
    {:border "0"}]
   [".skip-link"
    {:position "absolute"
     :top "-120px"
     :left "16px"
     :z-index "10"
     :padding "10px 18px"
     :border-radius "12px"
     :background "var(--navy)"
     :color "var(--white)"}
    {:font-weight "700"
     :text-decoration "none"}]
   [".skip-link:focus"
    {:top "16px"}]
   ;; ── Boutons, sur-titres, puces ───────────────────────────────────────────
   ;; Cyan = appel à l'action dans le brandbook ; texte bleu nuit à 10,15:1.
   [".button"
    {:display "inline-flex"
     :align-items "center"
     :justify-content "center"
     :min-height "48px"
     :padding "12px 28px"
     :border "0"
     :border-radius "999px"
     :background "var(--cyan)"}
    {:color "var(--navy)"
     :font "700 1rem/1.2 var(--font)"
     :text-align "center"
     :text-decoration "none"
     :cursor "pointer"
     :transition "background-color 0.2s ease, color 0.2s ease"}]
   [".button:hover"
    {:background "var(--navy)"
     :color "var(--white)"}]
   [".hero .button:hover"
    {:background "var(--white)"
     :color "var(--navy)"}]
   [".button:disabled"
    {:opacity "0.6"
     :cursor "not-allowed"}]
   [".button[aria-busy=\"true\"]"
    {:cursor "progress"}]
   [".button--small"
    {:min-height "42px"
     :padding "9px 20px"
     :font-size "0.9375rem"}]
   [".button--large"
    {:min-height "56px"
     :padding "15px 32px"
     :font-size "1.0625rem"}]
   [".button--block"
    {:width "100%"}]
   [".eyebrow"
    {:display "inline-flex"
     :align-items "center"
     :gap "10px"
     :margin-bottom "18px"
     :color "var(--navy)"
     :font-size "0.8125rem"
     :font-weight "700"
     :line-height "1.35"}
    {:letter-spacing "0.12em"
     :text-transform "uppercase"}]
   [".eyebrow::before"
    {:content "\"\""
     :flex "none"
     :width "15px"
     :height "14px"
     :background "var(--cyan)"
     :-webkit-mask "var(--bubble) center / contain no-repeat"
     :mask "var(--bubble) center / contain no-repeat"}]
   [".bubble-icon"
    {:flex "none"
     :width "18px"
     :height "auto"}]
   [".icon-bubble"
    {:width "60px"
     :height "auto"}]
   ;; ── En-tête ──────────────────────────────────────────────────────────────
   [".site-header"
    {:background "var(--white)"}]
   [".site-header__inner"
    {:display "flex"
     :align-items "center"
     :justify-content "space-between"
     :gap "16px"
     :min-height "76px"}]
   [".site-header__brand img"
    {:width "135px"
     :height "auto"}]
   (at-media {:max-width "420px"}
     [".site-header__brand img"
      {:width "108px"}]
     [".site-header .button--small"
      {:padding-inline "14px"
       :font-size "0.875rem"}])
   ;; ── Introduction ─────────────────────────────────────────────────────────
   ;; Fond bleu nuit qui s'arrête au milieu de la capture : elle déborde sur la section suivante.
   [".hero"
    {"--hero-overlap" "clamp(72px, 13vw, 180px)"
     :position "relative"
     :overflow "hidden"
     :padding-top "clamp(56px, 8vw, 104px)"
     :background "linear-gradient(to bottom, var(--navy) calc(100% - var(--hero-overlap)), var(--white) 0)"
     :color "var(--white)"}]
   [".hero__art"
    {:position "absolute"
     :top "-24px"
     :right "max(-180px, calc(50% - 760px))"
     :width "min(680px, 80vw)"
     :height "auto"
     :pointer-events "none"}]
   ;; Sous 1180 px, le décor passerait sous le texte : on ne garde que la grande bulle bleue
   ;; (texte blanc sur bleu translucide : 9:1).
   (at-media {:max-width "1179px"}
     [".hero__art"
      {:top "-140px"
       :right "-260px"}]
     [".hero__art-light"
      {:display "none"}])
   [".hero__inner"
    {:position "relative"
     :display "grid"
     :gap "clamp(48px, 7vw, 72px)"}]
   [".hero__content"
    {:max-width "780px"}]
   [".hero .eyebrow"
    {:color "var(--green)"}]
   [".hero .eyebrow::before"
    {:background "var(--green)"}]
   [".hero__title"
    {:max-width "15ch"
     :font-size "clamp(2.5rem, 1.6rem + 3.6vw, 4rem)"
     :font-weight "900"}]
   [".hero__lead"
    {:max-width "56ch"
     :margin-top "24px"
     :color "var(--text-on-dark)"
     :font-size "clamp(1.0625rem, 1rem + 0.4vw, 1.25rem)"}]
   [".hero__actions"
    {:display "flex"
     :flex-wrap "wrap"
     :align-items "center"
     :gap "14px 22px"
     :margin-top "36px"}]
   [".hero__note"
    {:color "var(--text-on-dark)"
     :font-size "0.9375rem"}]
   ;; ── Captures d'écran ─────────────────────────────────────────────────────
   [".shot"
    {:min-width "0"
     :padding "10px"
     :border-radius "22px 22px 22px 6px"
     :background "var(--white)"
     :box-shadow "0 24px 60px rgb(19 0 70 / 0.14), 0 2px 6px rgb(19 0 70 / 0.06)"}]
   [".shot img"
    {:width "100%"
     :height "auto"
     :border-radius "14px 14px 14px 4px"}]
   [".shot--hero"
    {:box-shadow "0 30px 80px rgb(19 0 70 / 0.22), 0 2px 6px rgb(19 0 70 / 0.08)"}]
   [".shot--approvals"
    {:justify-self "center"
     :width "100%"
     :max-width "360px"}]
   ;; ── Preuves ──────────────────────────────────────────────────────────────
   [".proof"
    {:padding-top "clamp(40px, 6vw, 64px)"}]
   [".proof__list"
    {:display "grid"
     :grid-template-columns "repeat(auto-fit, minmax(min(100%, 230px), 1fr))"
     :gap "28px 40px"}]
   [".proof__item"
    {:position "relative"
     :padding-left "32px"}]
   [".proof__item .bubble-icon"
    {:position "absolute"
     :top "6px"
     :left "0"}]
   [".proof__value"
    {:display "block"
     :color "var(--blue)"
     :font-size "clamp(2.25rem, 1.8rem + 1.6vw, 3rem)"
     :font-weight "900"
     :line-height "1"
     :letter-spacing "-0.02em"}]
   [".proof__label"
    {:display "block"
     :max-width "30ch"
     :margin-top "8px"
     :color "var(--text-muted)"
     :font-size "0.9375rem"
     :line-height "1.4"}]
   [".proof__source"
    {:margin-top "24px"
     :color "var(--text-muted)"
     :font-size "0.8125rem"}]
   ;; ── Sections ─────────────────────────────────────────────────────────────
   [".section"
    {:padding-block "var(--section-space)"}]
   [".section__head"
    {:max-width "780px"
     :margin-bottom "clamp(36px, 5vw, 60px)"}]
   [".section__title"
    {:font-size "clamp(2rem, 1.3rem + 2.6vw, 3.25rem)"
     :font-weight "900"}]
   ;; ── Problème ─────────────────────────────────────────────────────────────
   [".problem"
    {:margin-top "clamp(64px, 9vw, 112px)"
     :background "var(--gray)"}]
   [".cards"
    {:display "grid"
     :grid-template-columns "repeat(auto-fit, minmax(min(100%, 280px), 1fr))"
     :gap "20px"}]
   [".card"
    {:padding "32px 28px 34px"
     :border-radius "var(--bubble-radius)"
     :background "var(--white)"}]
   [".card .icon-bubble"
    {:margin-bottom "22px"}]
   [".card__title"
    {:font-size "1.25rem"
     :font-weight "700"
     :line-height "1.3"
     :letter-spacing "0"}]
   [".card__text"
    {:margin-top "10px"
     :color "var(--text-muted)"}]
   ;; ── Produit ──────────────────────────────────────────────────────────────
   [".features"
    {:display "grid"
     :gap "clamp(64px, 9vw, 112px)"}]
   [".feature"
    {:display "grid"
     :gap "36px"
     :align-items "center"}]
   (at-media {:min-width "960px"}
     [".feature"
      {:grid-template-columns "minmax(0, 1fr) minmax(0, 1.3fr)"
       :gap "40px 64px"}]
     [".feature--reverse"
      {:grid-template-columns "minmax(0, 1.3fr) minmax(0, 1fr)"}]
     [".feature--reverse .feature__text"
      {:order "2"}]
     [".feature--reverse .shot"
      {:order "1"}]
     [".feature > .checks"
      {:order "3"
       :grid-column "1 / -1"}])
   [".feature__tag"
    {:display "inline-flex"
     :align-items "center"
     :gap "10px"
     :padding "7px 16px 7px 12px"
     :border-radius "999px"
     :background "rgb(90 229 183 / 0.24)"
     :font-size "0.875rem"
     :font-weight "700"}
    {:line-height "1.3"}]
   [".feature__tag .bubble-icon"
    {:width "16px"}]
   [".feature__title"
    {:margin-top "20px"
     :font-size "clamp(1.625rem, 1.25rem + 1.3vw, 2.25rem)"
     :font-weight "900"}]
   [".feature__body"
    {:max-width "52ch"
     :margin-top "16px"
     :color "var(--text-muted)"}]
   [".checks"
    {:padding "clamp(28px, 4vw, 48px)"
     :border-radius "var(--bubble-radius)"
     :background "var(--gray)"}]
   [".checks__title"
    {:font-size "1.25rem"
     :font-weight "700"
     :line-height "1.3"
     :letter-spacing "0"}]
   [".checks__list"
    {:display "grid"
     :grid-template-columns "repeat(auto-fit, minmax(min(100%, 220px), 1fr))"
     :gap "32px 36px"
     :margin-top "30px"}]
   [".check"
    {:position "relative"
     :padding-top "32px"}]
   [".check .bubble-icon"
    {:position "absolute"
     :top "0"
     :left "0"
     :width "22px"}]
   [".check__when"
    {:font-size "0.8125rem"
     :font-weight "700"
     :letter-spacing "0.08em"
     :text-transform "uppercase"}]
   [".check__title"
    {:margin-top "6px"
     :font-size "1.125rem"
     :font-weight "700"
     :line-height "1.3"
     :letter-spacing "0"}]
   [".check__text"
    {:margin-top "8px"
     :color "var(--text-muted)"
     :font-size "0.9375rem"
     :line-height "1.5"}]
   [".check__source"
    {:margin-top "10px"
     :font-size "0.8125rem"
     :font-weight "700"}]
   [".checks__note"
    {:margin-top "28px"
     :padding-top "20px"
     :border-top "1px solid var(--line)"
     :color "var(--text-muted)"
     :font-size "0.8125rem"}]
   [".extras"
    {:margin-top "clamp(64px, 9vw, 112px)"}]
   [".extras__title"
    {:font-size "clamp(1.625rem, 1.25rem + 1.3vw, 2.25rem)"
     :font-weight "900"}]
   [".extras__list"
    {:display "grid"
     :grid-template-columns "repeat(auto-fit, minmax(min(100%, 320px), 1fr))"
     :gap "20px"
     :margin-top "32px"}]
   [".extra"
    {:padding "32px 28px 34px"
     :border-radius "var(--bubble-radius)"
     :background "var(--gray)"}]
   [".extra .icon-bubble"
    {:margin-bottom "22px"}]
   [".extra__title"
    {:font-size "1.25rem"
     :font-weight "700"
     :line-height "1.3"
     :letter-spacing "0"}]
   [".extra__text"
    {:margin-top "10px"
     :color "var(--text-muted)"}]
   ;; ── Résultats ────────────────────────────────────────────────────────────
   [".results"
    {:background "var(--gray)"}]
   [".results__panel"
    {:position "relative"
     :overflow "hidden"
     :display "grid"
     :gap "40px"
     :padding "clamp(32px, 5vw, 64px)"
     :border-radius "40px 40px 40px 8px"
     :background "var(--navy)"
     :color "var(--white)"}]
   (at-media {:min-width "960px"}
     [".results__panel"
      {:grid-template-columns "minmax(0, 1.1fr) minmax(0, 1fr)"
       :gap "64px"
       :align-items "center"}])
   [".results__intro, .quote"
    {:position "relative"}]
   [".results .eyebrow"
    {:color "var(--green)"}]
   [".results .eyebrow::before"
    {:background "var(--green)"}]
   [".results__text"
    {:max-width "46ch"
     :margin-top "18px"
     :color "var(--text-on-dark)"}]
   [".results__stats"
    {:display "flex"
     :flex-wrap "wrap"
     :gap "24px 56px"
     :margin-top "34px"}]
   [".results__stat strong"
    {:display "block"
     :color "var(--mint)"
     :font-size "clamp(2.75rem, 2rem + 2.6vw, 3.75rem)"
     :font-weight "900"
     :line-height "1"
     :letter-spacing "-0.02em"}]
   [".results__stat span"
    {:display "block"
     :margin-top "8px"
     :color "var(--text-on-dark)"
     :font-size "0.9375rem"}]
   [".results__source"
    {:margin-top "20px"
     :color "rgb(255 255 255 / 0.72)"
     :font-size "0.8125rem"}]
   [".quote"
    {:padding "clamp(28px, 4vw, 44px)"
     :border-radius "var(--bubble-radius)"
     :background "rgb(255 255 255 / 0.07)"
     :box-shadow "inset 0 0 0 1px rgb(255 255 255 / 0.14)"}]
   [".quote__text"
    {:font-size "clamp(1.5rem, 1.2rem + 1.2vw, 2.125rem)"
     :font-weight "700"
     :line-height "1.25"
     :text-wrap "balance"}]
   [".quote__caption"
    {:display "grid"
     :gap "4px"
     :margin-top "24px"
     :color "var(--text-on-dark)"
     :font-size "0.9375rem"
     :line-height "1.45"}]
   [".quote__caption strong"
    {:color "var(--white)"}]
   [".quote__caption a"
    {:justify-self "start"
     :margin-top "12px"
     :color "var(--white)"
     :font-weight "700"}]
   [".results__references"
    {:max-width "72ch"
     :margin-top "28px"
     :color "var(--text-muted)"}]
   [".pattern"
    {:position "absolute"
     :inset "0"
     :width "100%"
     :height "100%"
     :max-width "none"
     :opacity "0.045"
     :pointer-events "none"}]
   ;; ── FAQ ──────────────────────────────────────────────────────────────────
   [".faq__inner"
    {:max-width "calc(940px + 2 * var(--gutter))"}]
   [".faq__list"
    {:display "grid"
     :gap "12px"}]
   [".faq__item"
    {:border-radius "20px 20px 20px 6px"
     :background "var(--gray)"}]
   [".faq__question"
    {:display "flex"
     :align-items "center"
     :justify-content "space-between"
     :gap "20px"
     :padding "22px 26px"
     :border-radius "inherit"
     :font-size "1.125rem"
     :font-weight "700"}
    {:line-height "1.35"
     :list-style "none"
     :cursor "pointer"}]
   [".faq__question::-webkit-details-marker"
    {:display "none"}]
   [".faq__question::after"
    {:content "\"+\""
     :flex "none"
     :color "var(--blue)"
     :font-size "1.75rem"
     :font-weight "900"
     :line-height "1"}]
   [".faq__item[open] > .faq__question::after"
    {:content "\"–\""}]
   [".faq__answer"
    {:padding "0 26px 24px"
     :color "var(--text-muted)"}]
   [".faq__source"
    {:margin-top "10px"
     :font-size "0.875rem"}]
   [".faq__source a"
    {:color "var(--navy)"
     :font-weight "700"}]
   ;; ── Démo ─────────────────────────────────────────────────────────────────
   [".demo"
    {:padding-block "var(--section-space)"
     :background "var(--gray)"}]
   [".demo__inner"
    {:display "grid"
     :gap "24px"}]
   [".demo__intro"
    {:position "relative"
     :overflow "hidden"
     :padding "clamp(36px, 5vw, 64px)"
     :border-radius "40px 40px 40px 8px"
     :background "var(--navy)"
     :color "var(--white)"}]
   [".demo__intro .pattern"
    {:opacity "0.06"}]
   [".demo__intro .eyebrow"
    {:color "var(--green)"}]
   [".demo__intro .eyebrow::before"
    {:background "var(--green)"}]
   [".demo__intro-content"
    {:position "relative"}]
   [".demo__title"
    {:max-width "15ch"
     :font-size "clamp(2rem, 1.3rem + 2.4vw, 3rem)"
     :font-weight "900"}]
   [".demo__includes"
    {:margin-top "30px"
     :font-weight "700"}]
   [".demo__list"
    {:display "grid"
     :gap "10px"
     :margin-top "14px"
     :color "var(--text-on-dark)"}]
   [".demo__list li"
    {:position "relative"
     :padding-left "30px"}]
   [".demo__list li::before"
    {:content "\"\""
     :position "absolute"
     :top "0.4em"
     :left "0"
     :width "17px"
     :height "16px"
     :background "var(--green)"
     :-webkit-mask "var(--bubble) center / contain no-repeat"}
    {:mask "var(--bubble) center / contain no-repeat"}]
   [".demo__form"
    {:position "relative"
     :display "grid"
     :gap "16px"
     :padding "clamp(28px, 4vw, 44px)"
     :border-radius "var(--radius)"
     :background "var(--white)"
     :box-shadow "0 24px 60px rgb(19 0 70 / 0.14), 0 2px 6px rgb(19 0 70 / 0.06)"}]
   (at-media {:min-width "960px"}
     [".demo__inner"
      {:grid-template-columns "minmax(0, 1fr) minmax(0, 1fr)"
       :gap "0"
       :align-items "center"}]
     [".demo__intro"
      {:display "flex"
       :align-items "center"
       :min-height "560px"
       :margin-right "-56px"
       :padding-right "calc(56px + clamp(36px, 5vw, 64px))"}])
   [".demo__form-title"
    {:font-size "clamp(1.5rem, 1.2rem + 1vw, 1.875rem)"
     :font-weight "900"}]
   [".demo__required"
    {:margin-top "-8px"
     :color "var(--text-muted)"
     :font-size "0.8125rem"}]
   [".field"
    {:display "grid"
     :gap "6px"}]
   [".field__label"
    {:font-size "0.875rem"
     :font-weight "700"
     :line-height "1.35"}]
   [".field__input"
    {:width "100%"
     :min-height "48px"
     :padding "10px 14px"
     :border "1.5px solid rgb(19 0 70 / 0.35)"
     :border-radius "12px"
     :background "var(--white)"
     :color "var(--text)"
     :font "400 1rem/1.4 var(--font)"}
    {:transition "border-color 0.15s ease, box-shadow 0.15s ease"}]
   [".field__input:focus"
    {:border-color "var(--blue)"
     :outline "none"
     :box-shadow "0 0 0 4px rgb(29 111 255 / 0.2)"}]
   [".field__hint"
    {:color "var(--text-muted)"
     :font-size "0.8125rem"
     :line-height "1.45"}]
   [".demo__privacy"
    {:color "var(--text-muted)"
     :font-size "0.8125rem"
     :line-height "1.45"}]
   [".demo__message"
    {:padding "12px 16px"
     :border-radius "12px"
     :background "rgb(56 209 221 / 0.16)"
     :font-size "0.9375rem"
     :font-weight "700"}]
   [".demo__message--error"
    {:background "rgb(29 111 255 / 0.1)"
     :box-shadow "inset 4px 0 0 var(--blue)"}]
   ;; ── Pied de page ─────────────────────────────────────────────────────────
   [".site-footer"
    {:padding-block "48px"
     :background "var(--navy)"
     :color "var(--white)"}]
   [".site-footer__inner"
    {:display "flex"
     :flex-wrap "wrap"
     :align-items "center"
     :justify-content "space-between"
     :gap "24px 48px"}]
   [".site-footer__brand img"
    {:width "168px"
     :height "auto"}]
   [".site-footer__tagline"
    {:flex "1 1 260px"
     :color "var(--text-on-dark)"
     :font-size "0.9375rem"}]
   [".site-footer__nav"
    {:display "flex"
     :flex-wrap "wrap"
     :gap "12px 28px"}]
   [".site-footer__nav a"
    {:font-size "0.9375rem"
     :font-weight "700"
     :text-decoration "none"}]
   [".site-footer__nav a:hover"
    {:color "var(--green)"}]
   [".site-footer__company"
    {:flex-basis "100%"
     :padding-top "24px"
     :border-top "1px solid rgb(255 255 255 / 0.14)"
     :color "rgb(255 255 255 / 0.72)"
     :font-size "0.8125rem"}]
   ;; ── Mouvement réduit ─────────────────────────────────────────────────────
   (at-media {:prefers-reduced-motion :reduce}
     ["html"
      {:scroll-behavior "auto"}]
     ["*, *::before, *::after"
      {:transition "none !important"}])])

(def stylesheet
  (concat font-faces [root] rules))

(defn css []
  (str "/* Généré par src/urbest/ny_schools/styles.cljs — ne pas modifier ce fichier. */\n"
       (garden/css {:pretty-print? true} stylesheet)))
