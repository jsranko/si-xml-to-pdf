# si-xml-to-pdf
[![build](https://github.com/jsranko/si-xml-to-pdf/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/jsranko/si-xml-to-pdf/actions/workflows/maven.yml)

si-xml-to-pdf ist ein Opensource Projekt, womit Daten (im XML/JSON Format) auf Basis von [Apache FreeMarker](https://freemarker.apache.org "Apache FreeMarker") Templates über Rest WebServices-Calls ins HTML/PDF umgewandelt werden können.

## Warum si-xml-to-pdf?
si-xml-to-pdf ist für alle geignet die
* DDS basierte Druckausgaben modernisieren, mit Bilder, QR-Codes, Barcodes oder grafischen Elementen anreichen möchten
* eine Druckausgabe (Rechnung, Auftragsbestätigung, ...) im PDF Format benötigen oder kostengünstig generieren möchten 
* ein Beispiel suchen, wie man mit OpenSource eine Modernisierung realisieren könnte

## Vorbeiretung
Opensource Pakete **git**, **maven** müssen installiert sein.

### PATH Variable setzen
IBM i Open Source Pakete in PATH Variable setzen:
```
export PATH=/QOpenSys/pkgs/bin:$PATH
```

### Abhängigkeiten installieren
```
yum install git, maven
```

## Installation

### Clone Projekt
```
git -c http.sslVerify=false clone https://github.com/jsranko/si-xml-to-pdf.git
```

## Build project
Kompletes Projekt erstellen:
```
cd si-xml-to-pdf/si-xml-to-pdf
gmake
```
Nur War-Datei erstellen:
```
gmake war
```
Nur Beispiel erstellen:
```
gmake example
```
War-Datei erstellen und War-Datei installieren/deploy
```
gmake deploy
```

## Credits
si-xml-to-pdf basiert auf
1. [OPEN HTML TO PDF](https://github.com/danfickle/openhtmltopdf)
2. [Apache FreeMarker](https://github.com/apache/freemarker)

Credit goes to the contributors of that project.
