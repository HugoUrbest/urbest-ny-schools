(ns urbest.ny-schools.views
  (:require [re-frame.core :as rf]
            [reagent.core :as r]
            [urbest.ny-schools.brand :as brand]
            [urbest.ny-schools.config :as config]
            [urbest.ny-schools.content :as content :refer [t]]))

;; ── Bulles de dialogue ──────────────────────────────────────────────────────
;; Élément graphique de la charte : la bulle issue du logo déconstruit
;; (tracé dans brand.cljs). Même tracé que la page Références.

(def bubble-path brand/bubble-path)

(def navy (brand/color :navy))
(def blue (brand/color :blue))
(def cyan (brand/color :cyan))
(def mint (brand/color :mint))
(def green (brand/color :green))
(def white (brand/color :white))

(def accents [cyan mint blue])

(defn- bubble
  "Bulle placée en (x, y), de largeur size ; flip? retourne la pointe vers la droite."
  [{:keys [x y size fill flip? opacity class]}]
  (let [s (/ size 100)]
    [:path {:d         bubble-path
            :class     class
            :fill      fill
            :opacity   opacity
            :transform (if flip?
                         (str "translate(" x " " y ") scale(" (- s) " " s ") translate(-100 0)")
                         (str "translate(" x " " y ") scale(" s ")"))}]))

(defn- bubble-icon
  "Petite bulle pleine, utilisée comme puce."
  [fill]
  [:svg.bubble-icon {:view-box "-2 0 102 92" :aria-hidden true :focusable false}
   [:path {:d bubble-path :fill fill}]])

;; Pictogrammes (tracés 24 × 24), dessinés en bleu nuit dans une bulle de couleur.
(def pictograms
  {:leak       ["M12 3.5c-3 3.9-6 7.3-6 10.4a6 6 0 0 0 12 0c0-3.1-3-6.5-6-10.4z"
                "M9.5 15a2.5 2.5 0 0 0 2.5 2.5"]
   :inspection ["M9 3.5h6v3H9z"
                "M15 5h3v15.5H6V5h3"
                "M9 13.5l2 2 4-4"]
   :budget     ["M4 20.5h16"
                "M7 20.5V12"
                "M12 20.5V6"
                "M17 20.5v-5"]
   :bus        ["M6 3.5h12a2 2 0 0 1 2 2V17H4V5.5a2 2 0 0 1 2-2z"
                "M4 11h16"
                "M7 20.5V17"
                "M17 20.5V17"
                "M7.5 14h.01"
                "M16.5 14h.01"]
   :equipment  ["M14.7 6.3a1 1 0 0 0 0 1.4l1.6 1.6a1 1 0 0 0 1.4 0l3.77-3.77a6 6 0 0 1-7.94 7.94l-6.91 6.91a2.12 2.12 0 0 1-3-3l6.91-6.91a6 6 0 0 1 7.94-7.94l-3.76 3.76z"]})

(defn- icon-bubble [pictogram fill]
  [:svg.icon-bubble {:view-box "-2 0 102 92" :aria-hidden true :focusable false}
   [:path {:d bubble-path :fill fill}]
   (into [:g {:transform       "translate(30 22) scale(2)"
              :fill            "none"
              :stroke          navy
              :stroke-width    2
              :stroke-linecap  "round"
              :stroke-linejoin "round"}]
         (for [d (pictograms pictogram)] [:path {:d d}]))])

(defn- hero-art
  "Décor du haut de page. Les petites bulles (claires) sont masquées quand elles
  passeraient sous le texte : du blanc sur Cyan ne fait que 1,85:1."
  []
  [:svg.hero__art {:view-box "0 0 640 560" :aria-hidden true :focusable false}
   [bubble {:x 210 :y -60 :size 460 :fill blue :opacity 0.55 :flip? true}]
   [bubble {:x 120 :y 250 :size 150 :fill cyan :opacity 0.9 :class "hero__art-light"}]
   [bubble {:x 520 :y 330 :size 96 :fill mint :flip? true :class "hero__art-light"}]
   [bubble {:x 92 :y 84 :size 54 :fill green :class "hero__art-light"}]])

(defn- bubble-pattern
  "Motif de bulles en filigrane (bloc résultats, bloc démo)."
  [id]
  [:svg.pattern {:aria-hidden true :focusable false}
   [:defs
    [:pattern {:id id :width 260 :height 240 :pattern-units "userSpaceOnUse"}
     [bubble {:x 8 :y 10 :size 70 :fill white}]
     [bubble {:x 118 :y 4 :size 44 :fill white :flip? true}]
     [bubble {:x 178 :y 40 :size 66 :fill white :flip? true}]
     [bubble {:x 40 :y 92 :size 40 :fill white :flip? true}]
     [bubble {:x 96 :y 76 :size 84 :fill white}]
     [bubble {:x 4 :y 156 :size 58 :fill white}]
     [bubble {:x 196 :y 132 :size 46 :fill white}]
     [bubble {:x 86 :y 170 :size 52 :fill white :flip? true}]
     [bubble {:x 160 :y 188 :size 36 :fill white}]]]
   [:rect {:width "100%" :height "100%" :fill (str "url(#" id ")")}]])

;; ── Éléments communs ────────────────────────────────────────────────────────

(defn- external-link
  "Lien vers une source extérieure, ouvert dans un nouvel onglet."
  [{:keys [href label]}]
  [:a {:href href :rel "noopener" :target "_blank"}
   label [:span.visually-hidden " (opens in a new tab)"]])

(defn- shot
  "Capture d'écran du produit dans un cadre ; eager? pour l'image principale."
  [screen-id {:keys [class eager?]}]
  (let [{:keys [file width height] :as screen} (get content/screens screen-id)]
    [:figure.shot {:class class}
     [:img (cond-> {:src      (config/asset "screens/" file)
                    :alt      (content/screen-alt screen)
                    :width    width
                    :height   height
                    :decoding "async"
                    :loading  (if eager? "eager" "lazy")}
             eager? (assoc :fetchpriority "high"))]]))

(defn- section-head [{:keys [id eyebrow title]}]
  [:div.section__head
   [:p.eyebrow eyebrow]
   [:h2.section__title {:id id} title]])

;; ── En-tête ─────────────────────────────────────────────────────────────────

(defn- site-header []
  [:header.site-header
   [:div.container.site-header__inner
    [:a.site-header__brand {:href (config/site-page "/") :aria-label (t :home-label)}
     [:img {:src (config/asset "img/urbest.svg") :alt "" :width 135 :height 35}]]
    [:a.button.button--small {:href "#demo"} (t :header-cta)]]])

;; ── Introduction ────────────────────────────────────────────────────────────

(defn- hero []
  [:section.hero {:aria-labelledby "hero-title"}
   [hero-art]
   [:div.container.hero__inner
    [:div.hero__content
     [:p.eyebrow (t :hero-eyebrow)]
     [:h1#hero-title.hero__title (t :hero-title)]
     [:p.hero__lead (t :hero-lead)]
     [:div.hero__actions
      [:a.button.button--large {:href "#demo"} (t :hero-cta)]
      [:p.hero__note (t :hero-cta-note)]]]
    [shot :board {:class "shot--hero" :eager? true}]]])

;; ── Preuves ─────────────────────────────────────────────────────────────────

(defn- proof []
  [:section.proof {:aria-label (t :proof-label)}
   [:div.container
    [:ul.proof__list
     (for [[i {:keys [value label link]}] (map-indexed vector (t :proof))]
       ^{:key value}
       [:li.proof__item
        [bubble-icon (nth accents i)]
        [:strong.proof__value value]
        [:span.proof__label
         (if (= :capterra link)
           [external-link {:href config/capterra-url :label label}]
           label)]])]
    [:p.proof__source (t :proof-source)]]])

;; ── Problème ────────────────────────────────────────────────────────────────

(defn- problem []
  [:section.section.problem {:aria-labelledby "problem-title"}
   [:div.container
    [section-head {:id "problem-title" :eyebrow (t :problem-eyebrow) :title (t :problem-title)}]
    [:ul.cards
     (for [[i {:keys [icon title text]}] (map-indexed vector (t :problem-cards))]
       ^{:key title}
       [:li.card
        [icon-bubble icon (nth [cyan mint green] i)]
        [:h3.card__title title]
        [:p.card__text text]])]]])

;; ── Produit ─────────────────────────────────────────────────────────────────

(defn- checks []
  [:div.checks {:role "group" :aria-labelledby "checks-title"}
   [:h4#checks-title.checks__title (t :checks-title)]
   [:ol.checks__list
    (for [[i {:keys [id title when text source]}] (map-indexed vector content/checks)]
      ^{:key id}
      [:li.check
       [bubble-icon (nth [mint cyan green blue] i)]
       [:p.check__when when]
       [:h5.check__title title]
       [:p.check__text text]
       [:p.check__source [external-link source]]])]
   [:p.checks__note (t :checks-note) " " (t :checks-verified)]])

(defn- feature [i {:keys [id tag title text screen]}]
  (let [title-id (str (name id) "-title")]
    [:article.feature {:class           (when (odd? i) "feature--reverse")
                       :aria-labelledby title-id}
     [:div.feature__text
      [:p.feature__tag [bubble-icon (nth accents i)] tag]
      [:h3.feature__title {:id title-id} title]
      [:p.feature__body text]]
     [shot screen {:class (str "shot--" (name screen))}]
     ;; Les contrôles new-yorkais occupent toute la largeur, sous la capture.
     (when (= :checks id) [checks])]))

(defn- extras
  "Bus scolaires et équipements : les mêmes outils (signalement, planification, historique, coûts)."
  []
  [:div.extras {:role "group" :aria-labelledby "extras-title"}
   [:h3#extras-title.extras__title (t :extras-title)]
   [:ul.extras__list
    (for [[i {:keys [icon title text]}] (map-indexed vector (t :extras))]
      ^{:key title}
      [:li.extra
       [icon-bubble icon (nth [mint cyan] i)]
       [:h4.extra__title title]
       [:p.extra__text text]])]])

(defn- product []
  [:section.section.product {:aria-labelledby "product-title"}
   [:div.container
    [section-head {:id "product-title" :eyebrow (t :product-eyebrow) :title (t :product-title)}]
    [:div.features
     (for [[i f] (map-indexed vector (t :features))]
       ^{:key (:id f)} [feature i f])]
    [extras]]])

;; ── Résultats ───────────────────────────────────────────────────────────────

(defn- results []
  [:section.section.results {:aria-labelledby "results-title"}
   [:div.container
    [:div.results__panel
     [bubble-pattern "bubbles-results"]
     [:div.results__intro
      [:p.eyebrow (t :results-eyebrow)]
      [:h2#results-title.section__title (t :results-title)]
      [:p.results__text (t :results-text)]
      [:ul.results__stats
       (for [{:keys [value label]} (t :results-stats)]
         ^{:key value}
         [:li.results__stat [:strong value] [:span label]])]
      [:p.results__source (t :results-source)]]
     [:figure.quote
      [:blockquote.quote__text [:p "“" (t :quote) "”"]]
      [:figcaption.quote__caption
       [:strong (t :quote-who)]
       [:a {:href (config/site-page "/case-studies/education/")} (t :quote-link)]]]]
    [:p.results__references (t :references-text)]]])

;; ── FAQ ─────────────────────────────────────────────────────────────────────

(defn- faq []
  [:section.section.faq {:aria-labelledby "faq-title"}
   [:div.container.faq__inner
    [section-head {:id "faq-title" :eyebrow (t :faq-eyebrow) :title (t :faq-title)}]
    [:div.faq__list
     (for [{:keys [q a source]} content/faq]
       ^{:key q}
       [:details.faq__item
        [:summary.faq__question q]
        [:div.faq__answer
         [:p a]
         (when source [:p.faq__source "Source: " [external-link source]])]])]]])

;; ── Démo ────────────────────────────────────────────────────────────────────
;; Mêmes champs (noms et contrôles) que le formulaire des autres pages d'urbest.io.

(defn- field [{:keys [id label type required? hint attrs]}]
  (let [hint-id (str id "-hint")]
    [:div.field
     [:label.field__label {:for id}
      label
      (when required? [:span.field__required {:aria-hidden true} " *"])]
     [:input.field__input (merge {:id id :name id :type type :required required?}
                                 (when hint {:aria-describedby hint-id})
                                 attrs)]
     (when hint [:p.field__hint {:id hint-id} hint])]))

(defn- submit-button
  "Désactivé dans le HTML pré-rendu ; activé une fois monté dans le navigateur.
  Sans JavaScript, le formulaire ne peut donc pas partir en envoi natif, qui
  perdrait la demande."
  []
  (r/create-class
   {:display-name        "submit-button"
    :component-did-mount #(rf/dispatch [:demo-ready])
    :reagent-render
    (fn []
      (let [status   @(rf/subscribe [:demo-status])
            ready?   @(rf/subscribe [:demo-ready?])
            sending? (= :sending status)]
        [:button.button.button--block {:type      "submit"
                                       :disabled  (or (not ready?) sending?)
                                       :aria-busy sending?}
         (if sending? (t :form-sending) (t :form-submit))]))}))

(defn- demo []
  (let [status @(rf/subscribe [:demo-status])]
    [:section.demo {:id "demo" :aria-labelledby "demo-title"}
     [:div.container.demo__inner
      [:div.demo__intro
       [bubble-pattern "bubbles-demo"]
       [:div.demo__intro-content
        [:p.eyebrow (t :demo-eyebrow)]
        [:h2#demo-title.demo__title (t :demo-title)]
        [:p.demo__includes (t :demo-includes)]
        [:ul.demo__list
         (for [item (t :demo-items)] ^{:key item} [:li item])]]]
      ;; method post : même en cas d'envoi natif, les coordonnées ne partent
      ;; jamais dans l'URL de la page (historique, journaux du serveur).
      [:form.demo__form {:id         "demo-form"
                         :method     "post"
                         :aria-labelledby "form-title"
                         :on-submit  (fn [e]
                                       (.preventDefault e)
                                       (rf/dispatch [:submit-demo (js/FormData. (.-currentTarget e))]))}
       [:h3#form-title.demo__form-title (t :form-title)]
       [:p.demo__required [:span {:aria-hidden true} "* "] (t :form-required)]
       [field {:id "full-name" :label (t :form-full-name) :type "text" :required? true
               :attrs {:auto-complete "name"}}]
       [field {:id "organization-title" :label (t :form-job) :type "text"
               :attrs {:auto-complete "organization-title"}}]
       [field {:id "organization" :label (t :form-organization) :type "text"
               :attrs {:auto-complete "organization"}}]
       [field {:id "email" :label (t :form-email) :type "email" :required? true
               :hint (t :form-email-hint)
               :attrs {:auto-complete "email"
                       :pattern       "^[^@]+@(?!gmail\\.com$|outlook\\.com$|yahoo\\.com$|hotmail\\.com$).+"
                       :title         (t :form-email-hint)}}]
       [field {:id "phone" :label (t :form-phone) :type "tel" :required? true
               :attrs {:auto-complete "tel"}}]
       [:input {:type "hidden" :name "lang" :value config/lang}]
       [:p.demo__privacy
        (t :form-privacy)
        [:a {:href (config/site-page "/privacy-policy/")} (t :form-privacy-link)]
        "."]
       (case status
         :error    [:p.demo__message.demo__message--error {:role "alert"} (t :form-error)]
         :dev-sent [:p.demo__message {:role "status"} (t :form-dev-sent)]
         nil)
       [submit-button]]]]))

;; ── Pied de page ────────────────────────────────────────────────────────────

(defn- site-footer []
  [:footer.site-footer
   [:div.container.site-footer__inner
    [:a.site-footer__brand {:href (config/site-page "/") :aria-label (t :home-label)}
     [:img {:src (config/asset "img/urbest-blanc.svg") :alt "" :width 168 :height 60}]]
    [:p.site-footer__tagline (t :footer-tagline)]
    [:nav.site-footer__nav {:aria-label (t :footer-label)}
     [:a {:href (config/site-page "/solutions/education/")} (t :footer-education)]
     [:a {:href (config/site-page "/contact/")} (t :footer-contact)]
     [:a {:href (config/site-page "/privacy-policy/")} (t :footer-privacy)]
     [:a {:href (config/site-page "/terms-of-service/")} (t :footer-terms)]]
    [:p.site-footer__company (t :footer-company)]]])

;; ── Page ────────────────────────────────────────────────────────────────────

(defn root []
  [:<>
   [:a.skip-link {:href "#main"} (t :skip-link)]
   [site-header]
   [:main#main
    [hero]
    [proof]
    [problem]
    [product]
    [results]
    [faq]
    [demo]]
   [site-footer]])
