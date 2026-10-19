// Relevé des textes d'une capture (OCR Vision de macOS), pour remplir src/urbest/ny_schools/tools/screens_en.cljs.
// Usage : osascript -l JavaScript screens/ocr.js screens/fr/<capture>.webp
// Sortie : x y largeur hauteur<TAB>texte, en pixels de l'image d'origine.
ObjC.import('Vision'); ObjC.import('AppKit'); ObjC.import('Foundation');
function run(argv) {
  var out = [];
  argv.forEach(function (p) {
    var img = $.NSImage.alloc.initWithContentsOfFile(p);
    var rep = $.NSBitmapImageRep.imageRepWithData(img.TIFFRepresentation);
    var cg = rep.CGImage;
    var w = rep.pixelsWide, h = rep.pixelsHigh;
    var req = $.VNRecognizeTextRequest.alloc.init;
    req.recognitionLevel = 0; // accurate
    req.usesLanguageCorrection = false;
    req.recognitionLanguages = $(['fr-FR', 'en-US']);
    var handler = $.VNImageRequestHandler.alloc.initWithCGImageOptions(cg, $({}));
    handler.performRequestsError($([req]), null);
    out.push('== ' + p.split('/').pop() + ' ' + w + 'x' + h);
    var res = req.results;
    for (var i = 0; i < res.count; i++) {
      var o = res.objectAtIndex(i);
      var c = o.topCandidates(1).objectAtIndex(0);
      var b = o.boundingBox;
      out.push([Math.round(b.origin.x * w), Math.round((1 - b.origin.y - b.size.height) * h), Math.round(b.size.width * w), Math.round(b.size.height * h)].join(' ') + '\t' + c.string.js);
    }
  });
  return out.join('\n');
}
