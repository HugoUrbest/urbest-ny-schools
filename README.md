# Urbest · New York school districts

Landing page anglaise (États-Unis) pour les districts scolaires de l'État de New York, réécrite en
ClojureScript après une revue adversariale de la version HTML d'origine.

- shadow-cljs 2.27, reagent 1.2, re-frame 1.3, garden 1.3, namespaces `urbest.ny-schools.*` (mêmes conventions que les modules Urbest) ;
- page **pré-rendue** en HTML puis hydratée (formulaire de démo commun à urbest.io) ;
- tout est écrit en Clojure : vues (`views.cljs`), feuille de style (`styles.cljs`, garden), charte (`brand.cljs` :
  palette, Proxima Nova seule, bulle de dialogue), textes, FAQ et contrôles réglementaires (`content.cljs`),
  image de partage et captures traduites (`tools/`) ;
- captures en anglais américain générées à partir des captures françaises (`screens/fr/`).

## Commandes

```bash
npm install
npm run dev     # http://localhost:8290/en/schools/new-york/dev.html (le formulaire n'envoie rien ;
                # après une modification de styles.cljs, relancer npm run css)
npm run build   # CSS + captures traduites + image de partage + bundle + page docs/en/schools/new-york/index.html (noindex)
npm run serve   # http://localhost:8291/en/schools/new-york/
```

`URBEST_INDEX=1 npm run build` produit la version indexable. Elle échoue si une capture n'est pas traduite
(`:lang :fr` dans `content.cljs`).

## Captures traduites

`npm run screens` part des captures de l'interface française (`screens/fr/`), efface chaque texte avec la
couleur de fond relevée autour, puis écrit la traduction dans la police de l'application (Circular,
utilisée seulement au build et non versionnée : `screens/fonts/` ou, à défaut, `~/Library/Fonts/`). Positions et traductions : `src/urbest/ny_schools/tools/screens_en.cljs`
(positions relevées par OCR : `osascript -l JavaScript screens/ocr.js screens/fr/<capture>.webp`). `DEBUG=1 node target/tools.js screens` produit dans `target/screens-debug/` des
planches où chaque zone traduite est encadrée. Pour une nouvelle capture : la déposer dans `screens/fr/`, relever
les textes, les ajouter à `screens_en.cljs` et la déclarer dans `content.cljs`.

## Contrôles au build

Le build refuse de générer la page si un texte contient un mot banni par le copy core (« innovative »,
« leader », « game changer »…) ou un concurrent nommé, si une clé de texte manque, si une capture est absente
ou n'a pas les dimensions déclarées, ou, en production, si une capture montre l'interface française.

## Intégration à urbest.io

- Copier le dossier `docs/en/schools/new-york/` à la même adresse (ressources en chemins relatifs).
- Ajouter la page au `sitemap.xml` du site. À défaut, publier `docs/integration/sitemap-schools.xml` et
  ajouter la ligne de `docs/integration/robots-line.txt` au `robots.txt`. **Ne pas remplacer** le
  `robots.txt` ni le `llms.txt` existants.
- Ajouter la ligne de `docs/integration/llms-entry.md` dans la section « Solutions » du `llms.txt`.

## Points ouverts

Suivis dans la revue interne (`REVUE.md`, non versionnée) : usage des bus scolaires à confirmer, chiffres
du lycée Laplace, questions encore ouvertes des acheteurs américains.
