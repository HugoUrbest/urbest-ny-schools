(ns urbest.ny-schools.content
  "Tous les textes de la page (anglais américain), la FAQ, les contrôles réglementaires et les captures.

  Une seule source pour le HTML, le JSON-LD, l'entrée llms.txt et l'image de partage.
  Règles du copy core Urbest (skill urbest-voice) :
  - chiffres de résultat autorisés : 15 % de temps administratif gagné, demandes traitées 2 à 3 fois plus vite ;
  - pas de concurrent nommé, pas de mots survendeurs (contrôlés au build, voir tools/checks.cljs) ;
  - un seul appel à l'action : la demande de démo.")

;; ── Textes ──────────────────────────────────────────────────────────────────

(def text
  {:meta-title          "School Work Order & Inspection Software for NY | Urbest"
   :meta-description    "Work orders, building checks, equipment and school buses for New York school districts, in one simple app your whole team will actually use."
   :og-title            "Work orders your whole district will actually use"
   :og-image-alt        "Urbest: work orders your whole district will actually use. For New York school districts."

   :skip-link           "Skip to content"
   :header-cta          "Request a demo"
   :home-label          "Urbest home page"

   :hero-eyebrow        "Work orders & building checks for New York school districts"
   :hero-title          "Work orders your whole district will actually use."
   :hero-lead           "For buildings & grounds teams in New York school districts: staff report repairs from their phone, and your team tracks every job, check, piece of equipment and school bus in one place."
   :hero-cta            "Request a 15-minute demo"
   :hero-cta-note       "Includes a free 30-day trial."

   :proof-label         "Urbest in numbers"
   :proof               [{:value "300+" :label "schools run their maintenance on Urbest"}
                         {:value "3" :label "French regions use Urbest in their public high schools"}
                         {:value "4.8/5" :label "rating on Capterra" :link :capterra}]
   :proof-source        "Sources: Urbest; Capterra, October 2026."

   :problem-eyebrow     "School facilities today"
   :problem-title       "Your team does the work. Proving it is the hard part."
   :problem-cards       [{:icon  :leak
                          :title "The same leak, reported three times."
                          :text  "Requests arrive by phone, by email and in the hallway. Nobody has the full list of open work orders."}
                         {:icon  :inspection
                          :title "An inspection is due. Last year's report is in someone's inbox."
                          :text  "Fire, asbestos, lead and bus inspections each need dated proof, building by building, bus by bus."}
                         {:icon  :budget
                          :title "Budget season, and no numbers on where B&G time went."
                          :text  "Every line gets questioned before the May budget vote. Without records, a year of work is hard to show."}]

   :product-eyebrow     "How Urbest works"
   :product-title       "Three jobs your team does every day. One place to do them."
   :features            [{:id     :work-orders
                          :tag    "Work orders"
                          :title  "Reported from a phone. Tracked to the finish."
                          :text   "Staff scan the QR code in the room or open the app, add a photo and send. The request lands on your team's board with its building and floor. You assign it, everyone sees where it stands, and the person who reported it hears back when it's done."
                          :screen :request}
                         {:id     :checks
                          :tag    "Building checks"
                          :title  "Inspections that end in a dated report."
                          :text   "Build each checklist once and schedule it to come back when it's due. Mark every item green, orange or red, add a photo or a comment, then share the PDF report with the people concerned."
                          :screen :checklist}
                         {:id     :contractors
                          :tag    "Contractors & costs"
                          :title  "Quotes, work and payments approved by the right person."
                          :text   "Ask for approval on the quote, confirm the work is done, give the OK to pay, all on the request itself. Contractors only see their own sites. Costs add up by category, and requests export to Excel or CSV for budget season."
                          :screen :approvals}]

   :extras-title        "School buses and equipment, in the same place."
   :extras              [{:icon  :bus
                          :title "School buses"
                          :text  "Each bus has its own record. Drivers report a defect from their phone with a photo, you schedule inspections and servicing, and every repair and cost stays in the bus's history."}
                         {:icon  :equipment
                          :title "Equipment"
                          :text  "Boilers, elevators, fire extinguishers, ventilation: each piece of equipment has its documents, a maintenance schedule that creates the work on time, and its full history."}]

   :checks-title        "Recurring checks for New York public schools, for example"
   :checks-note         "Examples, not legal advice: your district's health and safety plan sets the full list."
   :checks-verified     "Sources checked on October 19, 2026."

   :results-eyebrow     "Results"
   :results-title       "Less chasing. Faster repairs."
   :results-text        "Staff see where their request stands. Technicians know what to do. You have the record your board asks for."
   :results-stats       [{:value "15%" :label "less time spent on admin"}
                         {:value "2–3×" :label "faster request resolution"}]
   :results-source      "Source: interviews with Urbest users."
   :quote               "I can show that our team is responsive."
   :quote-who           "Baptiste M., site manager at a public high school of 1,200 students"
   :quote-link          "Read the case study"
   :references-text     "Urbest runs in the public high schools of Normandy, Centre-Val de Loire and Auvergne-Rhône-Alpes, and in public administrations such as the Préfecture des Hauts-de-Seine."

   :faq-eyebrow         "FAQ"
   :faq-title           "Questions from facilities directors"

   :demo-eyebrow        "Next step"
   :demo-title          "See your own school in Urbest, in 15 minutes."
   :demo-includes       "The demo includes:"
   :demo-items          ["Answers to your questions"
                         "Your first building set up in Urbest"
                         "A free 30-day trial"]
   :form-title          "Request a demo"
   :form-full-name      "Full name"
   :form-job            "Job title"
   :form-organization   "School district"
   :form-email          "Work email"
   :form-email-hint     "Please use your district address. Gmail, Outlook, Yahoo and Hotmail addresses aren't accepted."
   :form-phone          "Phone number"
   :form-required       "Required"
   :form-privacy        "We don't share this information with third parties, as stated in our "
   :form-privacy-link   "Privacy Policy"
   :form-submit         "Request my demo"
   :form-sending        "Sending…"
   :form-error          "Something went wrong. Please try again in a moment."
   :form-dev-sent       "Development mode: the request was not sent."

   :footer-tagline      "Collaborative software for maintenance and facility services."
   :footer-label        "Urbest links"
   :footer-education    "Urbest for education"
   :footer-contact      "Contact"
   :footer-privacy      "Privacy"
   :footer-terms        "Terms"
   :footer-company      "Urbest SAS · 17 rue Claude Bloch, 14000 Caen, France"})

(defn t
  "Texte de la page. Une clé absente s'affiche « ⟨clé⟩ » : le build refuse alors de publier."
  [k]
  (get text k (str "⟨" (name k) "⟩")))

;; ── Contrôles réglementaires (vérifiés le 19 octobre 2026, voir REVUE.md) ──────

(def checks
  [{:id     :fire
    :title  "Fire safety inspection"
    :when   "Every year"
    :text   "In each school building, before the State renews its certificate of occupancy."
    :source {:label "8 NYCRR §155.8"
             :href  "https://www.law.cornell.edu/regulations/new-york/8-NYCRR-155.8"}}
   {:id     :asbestos
    :title  "Asbestos surveillance"
    :when   "Every six months"
    :text   "In buildings with asbestos-containing material, recorded in your AHERA management plan."
    :source {:label "40 CFR §763.92"
             :href  "https://www.ecfr.gov/current/title-40/chapter-I/subchapter-R/part-763/subpart-E/section-763.92"}}
   {:id     :lead
    :title  "Lead in drinking water"
    :when   "Every three years"
    :text   "Outlets above 5 ppb stay out of service until they are fixed and retested."
    :source {:label "NYS Public Health Law §1110"
             :href  "https://www.health.ny.gov/environmental/water/drinking/lead/lead_testing_of_school_drinking_water.htm"}}
   {:id     :bus
    :title  "School bus inspection"
    :when   "Every six months"
    :text   "The State Department of Transportation inspects each bus. A bus that fails stays off the road until it passes."
    :source {:label "NYSDOT inspection (NYC Public Schools)"
             :href  "https://infohub.nyced.org/in-our-schools/working-with-the-doe/contracts-and-purchasing/transportation-vendors/vehicle-inspection-standards-and-safety"}}])

;; ── FAQ ─────────────────────────────────────────────────────────────────────
;; Affichée sur la page ET reprise telle quelle dans le JSON-LD (FAQPage).

(def faq
  [{:q "What is Urbest?"
    :a "Urbest is a work order and maintenance platform (a CMMS) for organizations with many buildings. School districts use it to collect repair requests, run recurring building checks, maintain equipment and school buses, manage contractors and report costs."}
   {:q "Do teachers and staff need training?"
    :a "No. People who report a repair use it right away, from the app, the web or a QR code. For your technicians and supervisors, setting up their access and starting to handle requests takes 5 to 30 minutes."}
   {:q "Can contractors use Urbest?"
    :a "Yes. Each contractor gets their own access, limited to their sites and service categories. Quotes, completed work and payments go through your approval."}
   {:q "How is Urbest priced?"
    :a "$3 per student, per year, with unlimited users: a school of 1,000 students costs $3,000 a year. You get a written quote before anything starts, and the demo includes a free 30-day trial."}
   {:q "Do we need to go out to bid?"
    :a "It depends on the amount. New York's General Municipal Law §103 requires competitive bidding for purchase contracts over $20,000, counting what the district expects to spend over twelve months. At $3 per student, that threshold is about 6,600 students a year. Below it, your district's own purchasing policy applies."
    :source {:label "NYS Comptroller: Seeking Competition in Procurement"
             :href  "https://www.osc.ny.gov/files/local-government/academy/pdf/seeking-competition-procurement-062624.pdf"}}
   {:q "Is Urbest present in the United States?"
    :a "Yes. Urbest has a presence in New York, alongside its teams in Paris and London, and support is available in English."}
   {:q "Where is our data hosted? Can staff use single sign-on?"
    :a "Urbest is hosted on Amazon Web Services (AWS). Single sign-on (SSO) is available."}
   {:q "Who uses Urbest today?"
    :a "More than 300 schools, including the public high schools of three French regions (Normandy, Centre-Val de Loire and Auvergne-Rhône-Alpes), and public administrations such as the Préfecture des Hauts-de-Seine. Urbest is rated 4.8 out of 5 on Capterra."}])

;; ── Fonctions du produit (JSON-LD) ──────────────────────────────────────────
;; Uniquement ce que montrent les captures ou le site urbest.io.

(def software-features
  ["Repair requests from the web, the iOS and Android apps or a QR code in each room, with photos"
   "Work order board by status, with due dates and notifications"
   "Recurring checklists with a green, orange or red status per item and PDF reports"
   "Approval requests for quotes, completed work and payment"
   "Equipment and school bus records with documents, maintenance schedules and history"
   "Contractor access limited to their sites and service categories"
   "Costs by category, with Excel and CSV exports"])

;; ── Captures d'écran ─────────────────────────────────────────────────────────
;; Captures traduites en anglais américain par `npm run screens` à partir des captures françaises
;; (screens/fr/, traductions dans tools/screens_en.cljs). :lang :fr signale une capture non traduite :
;; son texte alternatif le dit, et le build de production (URBEST_INDEX=1) refuse de la publier.

(def screens
  {:board     {:file   "school-work-order-board.webp"
               :width  1600 :height 624
               :lang   :en
               :alt    "Urbest work order board: requests in columns by status (to assign, assigned, accepted, in progress), each with its category, building, floor and due date."}
   :request   {:file   "maintenance-request-detail.webp"
               :width  1600 :height 999
               :lang   :en
               :alt    "A plumbing request in Urbest: status steps from 'to assign' to 'resolved', building and floor, person responsible, description and activity history."}
   :checklist {:file   "school-building-inspection-checklist.webp"
               :width  1600 :height 475
               :lang   :en
               :alt    "A monthly safety walk-through checklist in Urbest: fire extinguishers, emergency lighting and restrooms, each marked green, orange or red, with buttons to preview the PDF and notify people."}
   :approvals {:file   "contractor-quote-approval.webp"
               :width  757 :height 1325
               :lang   :en
               :alt    "Asking for an approval in Urbest: choose an approver and a reason (approve the quote, work completed, OK to pay), then attach files."}})

(defn screen-alt
  "Texte alternatif fidèle à ce que montre réellement la capture."
  [{:keys [alt lang]}]
  (if (= :fr lang) (str alt " Interface shown in French.") alt))
