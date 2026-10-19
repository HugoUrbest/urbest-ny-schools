(ns urbest.ny-schools.tools.seo
  "Balises <head>, JSON-LD et fichiers d'intégration (sitemap, entrée llms.txt).
  Tout est tiré de content.cljs : la FAQ du JSON-LD est celle de la page, mot pour mot."
  (:require [clojure.string :as str]
            [urbest.ny-schools.config :as config]
            [urbest.ny-schools.content :as content :refer [t]]))

(def og-image
  {:path "img/og-new-york-schools.png" :width 1200 :height 630})

(def organization-id (str config/site-url "/#organization"))

(defn esc [s]
  (-> (str s)
      (str/replace "&" "&amp;")
      (str/replace "\"" "&quot;")
      (str/replace "<" "&lt;")
      (str/replace ">" "&gt;")))

;; ── JSON-LD ─────────────────────────────────────────────────────────────────

(defn json-ld
  "modified : date de génération (AAAA-MM-JJ)."
  [modified]
  {"@context" "https://schema.org"
   "@graph"
   [{"@type"     "Organization"
     "@id"       organization-id
     "name"      "Urbest"
     "legalName" "Urbest SAS"
     "url"       (config/site-page "/")
     "address"   {"@type"           "PostalAddress"
                  "streetAddress"   "17 rue Claude Bloch"
                  "postalCode"      "14000"
                  "addressLocality" "Caen"
                  "addressCountry"  "FR"}
     "sameAs"    [config/capterra-url]}

    {"@type"              "WebPage"
     "@id"                config/page-url
     "url"                config/page-url
     "name"               (t :og-title)
     "description"        (t :meta-description)
     "inLanguage"         "en-US"
     "dateModified"       modified
     "isPartOf"           {"@type" "WebSite" "name" "Urbest" "url" (str config/site-url "/")}
     "publisher"          {"@id" organization-id}
     "primaryImageOfPage" (config/asset-url (:path og-image))
     ;; Uniquement des pages qui existent : /en/schools/ n'existe pas (404).
     "breadcrumb"         {"@type"           "BreadcrumbList"
                           "itemListElement" [{"@type" "ListItem" "position" 1 "name" "Urbest"
                                               "item" (config/site-page "/")}
                                              {"@type" "ListItem" "position" 2 "name" "Education"
                                               "item" (config/site-page "/solutions/education/")}
                                              {"@type" "ListItem" "position" 3 "name" "New York school districts"
                                               "item" config/page-url}]}}

    {"@type"                  "SoftwareApplication"
     "@id"                    (str config/page-url "#software")
     "name"                   "Urbest"
     "applicationCategory"    "BusinessApplication"
     "applicationSubCategory" "Work order and maintenance management (CMMS)"
     "operatingSystem"        "Web, iOS, Android"
     "description"            (-> content/faq first :a)
     "publisher"              {"@id" organization-id}
     "audience"               {"@type" "Audience" "audienceType" "School district buildings & grounds teams"}
     "featureList"            content/software-features}

    {"@type"      "FAQPage"
     "@id"        (str config/page-url "#faq")
     "mainEntity" (for [{:keys [q a]} content/faq]
                    {"@type"          "Question"
                     "name"           q
                     "acceptedAnswer" {"@type" "Answer" "text" a}})}]})

(defn json-ld-script [modified]
  (str "<script type=\"application/ld+json\">\n"
       (-> (clj->js (json-ld modified))
           (js/JSON.stringify nil 2)
           ;; Un « </script> » dans un texte fermerait la balise.
           (str/replace "</" "<\\/"))
       "\n</script>"))

;; ── <head> ──────────────────────────────────────────────────────────────────

(defn head
  "css, js : adresses versionnées ; indexable? : false pour la prévisualisation (noindex)."
  [{:keys [css js indexable? modified]}]
  (let [image (config/asset-url (:path og-image))]
    (str/join
     "\n"
     ["<meta charset=\"utf-8\">"
      "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
      (str "<title>" (esc (t :meta-title)) "</title>")
      (str "<meta name=\"description\" content=\"" (esc (t :meta-description)) "\">")
      (if indexable?
        "<meta name=\"robots\" content=\"index, follow, max-image-preview:large\">"
        "<meta name=\"robots\" content=\"noindex\">")
      (str "<link rel=\"canonical\" href=\"" config/page-url "\">")
      "<meta name=\"theme-color\" content=\"#ffffff\">"
      "<meta property=\"og:type\" content=\"website\">"
      "<meta property=\"og:site_name\" content=\"Urbest\">"
      "<meta property=\"og:locale\" content=\"en_US\">"
      (str "<meta property=\"og:title\" content=\"" (esc (t :og-title)) "\">")
      (str "<meta property=\"og:description\" content=\"" (esc (t :meta-description)) "\">")
      (str "<meta property=\"og:url\" content=\"" config/page-url "\">")
      (str "<meta property=\"og:image\" content=\"" image "\">")
      (str "<meta property=\"og:image:width\" content=\"" (:width og-image) "\">")
      (str "<meta property=\"og:image:height\" content=\"" (:height og-image) "\">")
      (str "<meta property=\"og:image:alt\" content=\"" (esc (t :og-image-alt)) "\">")
      "<meta name=\"twitter:card\" content=\"summary_large_image\">"
      (str "<link rel=\"icon\" href=\"" (config/asset "img/favicon.svg") "\" type=\"image/svg+xml\">")
      (str "<link rel=\"preload\" href=\"" (config/asset "fonts/proxima-nova-black.woff2") "\" as=\"font\" type=\"font/woff2\" crossorigin>")
      (str "<link rel=\"preload\" href=\"" (config/asset "fonts/proxima-nova-regular.woff2") "\" as=\"font\" type=\"font/woff2\" crossorigin>")
      (str "<link rel=\"preload\" href=\"" (config/asset "fonts/proxima-nova-bold.woff2") "\" as=\"font\" type=\"font/woff2\" crossorigin>")
      (str "<link rel=\"stylesheet\" href=\"" css "\">")
      (str "<script src=\"" js "\" defer></script>")
      (json-ld-script modified)])))

;; ── Fichiers d'intégration à urbest.io ──────────────────────────────────────

(defn sitemap [modified]
  (str "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
       "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n"
       "  <url>\n"
       "    <loc>" config/page-url "</loc>\n"
       "    <lastmod>" modified "</lastmod>\n"
       "  </url>\n"
       "</urlset>\n"))

(defn llms-entry
  "Ligne à ajouter dans la section « ## Solutions » du llms.txt du site (même format)."
  []
  (str "- [" (t :meta-title) "](" config/page-url "): " (t :meta-description) "\n"))

(def robots-line
  (str "Sitemap: " config/site-url "/sitemap-schools.xml\n"))
