(ns urbest.ny-schools.tools.screens-en
  "Traduction en anglais américain des captures de l'interface française (screens/fr/).

  Chaque texte : :box [x y largeur hauteur] relevé par OCR (Vision, macOS) sur la capture d'origine,
  icônes exclues ; :fr le texte d'origine (sert à retrouver la taille de police) ; :en la traduction.
  Options : :font (:book par défaut, :medium, :bold, :italic, :bold-italic), :align (:left, :center,
  :right), :color (sinon la couleur d'origine), :track (lettres espacées, en-têtes en capitales),
  :fr-w (largeur de la première ligne d'origine quand le bloc en a plusieurs),
  :cover (zones supplémentaires à effacer), :lines (traduction sur plusieurs lignes du bloc d'origine).
  :shifts déplace une zone de pixels (pastille de compteur poussée par un texte plus long).")

(def screens
  {"school-work-order-board.webp"
   {:items
    [{:box [640 30 188 16] :fr "Rechercher une deman..." :en "Search requests..."}
     {:box [902 30 48 16] :fr "Filtres" :en "Filters"}
     {:box [1062 28 74 19] :fr "Échéance" :en "Due date"}
     {:box [1395 28 151 21] :fr "Créer une demande" :en "Create request" :font :medium}
     {:box [37 107 51 14] :fr "Toutes" :en "All"}
     {:box [121 107 172 14] :fr "Qui me sont attribuées" :en "Assigned to me"}
     {:box [323 107 109 16] :fr "Que j'ai créées" :en "Created by me"}
     {:box [21 162 98 14] :fr "À AFFECTER" :en "TO ASSIGN" :track true}
     {:box [421 160 77 16] :fr "ATTRIBUÉ" :en "ASSIGNED" :track true}
     {:box [823 160 73 16] :fr "ACCEPTÉ" :en "ACCEPTED" :track true}
     {:box [1223 160 80 16] :fr "EN COURS" :en "IN PROGRESS" :track true}
     ;; Cartes
     {:box [75 213 109 17] :fr "Réglementaire" :en "Compliance"}
     {:box [198 213 88 14] :fr "17 Mar, 09:00" :en "3/17, 9:00 AM" :align :center}
     {:box [44 255 114 16] :fr "Étage: 3 | Espace:" :en "Floor: 3 | Room:"}
     {:box [42 276 188 12] :fr "ID: #549713 | Créé le: 16 Mar" :en "ID: #549713 | Created: Mar 16"}
     {:box [56 306 279 49] :fr "Intervention du prestataire pour" :line-height 28 :fr-w 272
      :lines ["Contractor visit to check fire" "extinguisher pressure and seals"]}
     {:box [75 362 67 16] :fr "À affecter" :en "To assign"}
     {:box [876 211 77 18] :fr "Plomberie" :en "Plumbing"}
     {:box [847 255 112 16] :fr "Étage: 1 | Espace:" :en "Floor: 1 | Room:"}
     {:box [847 274 186 14] :fr "ID: #546514 | Créé le: 10 Mar" :en "ID: #546514 | Created: Mar 10"}
     {:box [856 306 290 49] :fr "Une fuite d'eau est constatée sous" :line-height 28 :fr-w 288
      :lines ["A water leak was found under" "the kitchenette sink."]}
     {:box [1277 210 79 20] :fr "Électricité" :en "Electrical"}
     {:box [1247 255 112 16] :fr "Étage: 1 | Espace:" :en "Floor: 1 | Room:"}
     {:box [1247 276 186 12] :fr "ID: #546513 | Créé le: 10 Mar" :en "ID: #546513 | Created: Mar 10"}
     {:box [1258 306 274 49] :fr "La lumière de la salle de réunion" :line-height 28 :fr-w 272
      :lines ["The meeting room light on the" "2nd floor no longer works"]}
     {:box [77 422 77 17] :fr "Électricité" :en "Electrical"}
     {:box [198 425 88 14] :fr "17 Mar, 09:30" :en "3/17, 9:30 AM" :align :center}
     {:box [44 466 114 16] :fr "Étage: 2 | Espace:" :en "Floor: 2 | Room:"}
     {:box [44 485 186 14] :fr "ID: #549761 | Créé le: 16 Mar" :en "ID: #549761 | Created: Mar 16"}
     {:box [53 517 282 47] :fr "Contrôle du tableau électrique" :line-height 28 :fr-w 260
      :lines ["Inspection of the main electrical" "panel and circuit breakers to"]}
     {:box [75 573 67 17] :fr "À affecter" :en "To assign"}
     {:box [877 425 100 14] :fr "Administratif" :en "Administrative"}
     {:box [847 464 116 16] :fr "Étage: 0 | Espace:" :en "Floor: 0 | Room:"}
     {:box [847 485 186 14] :fr "ID: #546515 | Créé le: 10 Mar" :en "ID: #546515 | Created: Mar 10"}
     {:box [853 517 293 47] :fr "Création d'un badge d'accès pour" :line-height 28 :fr-w 288
      :lines ["New access badge for a staff" "member joining the school"]}]
    :shifts
    [{:region [505 158 22 20] :dx 12}
     {:region [905 158 24 20] :dx 14}
     {:region [1310 158 24 20] :dx 44}]}

   "maintenance-request-detail.webp"
   {:items
    [{:box [23 35 95 23] :fr "Accepté" :en "Accepted" :font :medium}
     {:box [937 30 178 21] :fr "Demande 1 sur 2" :en "Request 1 of 2"}
     {:box [1086 118 95 23] :fr "Activité:" :en "Activity:"}
     {:box [92 179 243 44] :fr "Plomberie" :en "Plumbing" :font :bold}
     {:box [28 256 335 19] :fr "ID: #546514 | Créé le: 10 Mar 2026" :en "ID: #546514 | Created: Mar 10, 2026"}
     {:box [1138 302 216 21] :fr "a créé cette demande" :fr-w 228 :en "created this request" :font :italic}
     {:box [31 337 86 19] :fr "À affecter" :en "To assign"}
     {:box [218 339 68 18] :fr "Attribué" :en "Assigned"}
     {:box [405 339 70 21] :fr "Accepté" :en "Accepted" :font :medium}
     {:box [591 339 75 17] :fr "En cours" :en "In progress"}
     {:box [779 339 56 16] :fr "Résolu" :en "Resolved"}
     {:box [30 392 72 24] :fr "Aperçu" :en "Overview" :underline true}
     {:box [149 390 68 24] :fr "Objets" :en "Items"}
     {:box [1138 453 425 24] :fr "Le statut de la demande a changé: Accepté" :fr-w 440
      :runs [["Request status changed to: " {:font :italic}]
             ["Accepted" {:font :bold-italic :color "#6b4fbb"}]]}
     {:box [84 483 93 19] :fr "Bâtiment:" :en "Building:"}
     {:box [83 555 129 23] :fr "Responsable:" :en "Assignee:"}
     {:box [83 627 66 26] :fr "Statut:" :en "Status:"}
     {:box [83 699 175 21] :fr "Prévue/Échéance:" :en "Planned/Due:"}
     {:box [499 629 83 25] :fr "Accepté" :en "Accepted"}
     {:box [402 701 72 21] :fr "Choisir" :en "Select" :align :center}
     {:box [579 701 72 21] :fr "Choisir" :en "Select" :align :center}
     {:box [30 820 901 54] :fr "Une fuite d'eau est constatée sous l'évier de la kitchenette. Intervention nécessaire pour éviter"
      :line-height 32 :fr-w 898
      :lines ["A water leak was found under the kitchenette sink. Work is needed to prevent" "flooding."]}
     {:box [82 908 142 25] :fr "Pièces jointes:" :en "Attachments:"}
     {:box [740 908 230 24] :fr "Déposer ou télécharger" :en "Drop or upload" :align :right}
     {:box [81 955 721 19] :fr "Toute personne liée à cette demande peut accéder et consulter toutes les pièces jointes"
      :en "Anyone linked to this request can open and view all attachments"}
     {:box [1086 866 265 26] :fr "Écrire un commentaire..." :en "Write a comment..."}]}

   "school-building-inspection-checklist.webp"
   {:items
    [{:box [16 28 96 16] :fr "Check-lists" :en "Checklists"}
     {:box [161 28 262 16] :fr "Tournée sécurité mensuelle" :en "Monthly safety walk-through"}
     {:box [844 28 149 16] :fr "Valider la check-list" :en "Approve checklist"}
     {:box [1086 25 93 24] :fr "Aperçu PDF" :en "PDF preview"}
     {:box [1271 25 148 21] :fr "Ajouter une section" :en "Add a section"}
     {:box [1514 28 61 16] :fr "Notifier" :en "Notify"}
     {:box [142 110 47 18] :fr "TITRE" :en "TITLE" :track true}
     {:box [616 111 62 18] :fr "STATUT" :en "STATUS" :track true}
     {:box [870 111 128 17] :fr "MISE À JOUR LE" :en "UPDATED" :track true}
     {:box [1123 111 81 18] :fr "BÂTIMENT" :en "BUILDING" :track true}
     {:box [1377 111 119 19] :fr "PIÈCES JOIN..." :en "ATTACHMEN..." :track true}
     {:box [153 181 300 19] :fr "Extincteurs : pression et scellés" :en "Fire extinguishers: pressure and seals"}
     {:box [153 261 202 24] :fr "Éclairage de sécurité" :en "Emergency lighting"}
     {:box [151 350 328 16] :fr "Sanitaires: fuites et chasses d'eau" :en "Restrooms: leaks and flush valves"}
     {:box [909 181 98 16] :fr "30 Juin 15:47" :en "Jun 30, 3:47 PM"}
     {:box [909 266 98 14] :fr "30 Juin 15:47" :en "Jun 30, 3:47 PM"}
     {:box [909 349 86 17] :fr "6 Oct 14:52" :en "Oct 6, 2:52 PM"}
     {:box [1121 178 114 24] :fr "Bâtiment A" :en "Building A"}
     {:box [1121 264 114 22] :fr "Bâtiment B" :en "Building B"}
     {:box [1121 347 114 21] :fr "Bâtiment A" :en "Building A"}
     {:box [1372 180 63 20] :fr "Joindre" :en "Attach"}
     {:box [1372 264 63 19] :fr "Joindre" :en "Attach"}
     {:box [1372 349 63 20] :fr "Joindre" :en "Attach"}
     {:box [77 424 123 19] :fr "Ajouter élément" :en "Add item"}]}

   "contractor-quote-approval.webp"
   {:items
    [{:box [30 50 200 32] :fr "Approbations" :en "Approvals"}
     {:box [100 182 587 62] :fr "Une fois la demande envoyée, l'approbateur recevra" :line-height 36 :fr-w 587
      :lines ["Once the request is sent, the approver will get" "access to this request."]}
     {:box [38 326 318 24] :fr "Demander une approbation" :en "Request an approval" :font :medium}
     {:box [94 390 329 26] :fr "Selectionnez un approbateur" :en "Select an approver"}
     {:box [38 498 254 24] :fr "Objet de la demande?" :en "Reason for the request?" :font :medium}
     {:box [58 582 176 22] :fr "Valider le devis" :en "Approve quote" :font :medium :align :center}
     {:box [300 580 174 24] :fr "Travail terminé" :en "Work completed" :font :medium :align :center}
     {:box [539 582 138 24] :fr "Bon à payer" :en "OK to pay" :font :medium :align :center}
     {:box [38 677 276 22] :fr "Ecrire une autre raison…." :en "Write another reason..."}
     {:box [74 779 74 20] :fr "Pièces" :en "Files:" :cover [[68 812 94 30]]}
     {:box [462 793 267 28] :fr "Déposer ou télécharger" :en "Drop or upload" :align :right}
     {:box [88 847 617 52] :fr "Toute personne liée à cette demande peut accéder et consulter" :line-height 30 :fr-w 613
      :lines ["Anyone linked to this request can open and view" "all attachments"]}
     {:box [40 1059 679 22] :fr "DÉPOSEZ LES FICHIERS ICI OU CLIQUEZ POUR LES TÉLÉCHARGER"
      :en "DROP FILES HERE OR CLICK TO UPLOAD" :align :center}
     {:box [292 1257 138 22] :fr "Réinitialiser" :en "Reset" :font :medium :align :center}
     {:box [561 1256 96 28] :fr "Envoyer" :en "Send" :font :medium :align :center}]}})
