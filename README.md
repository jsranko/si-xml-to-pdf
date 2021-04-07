# si-xml-to-pdf
si-xml-to-pdf ist ein Opensource Projekt, womit Daten (im XML/JSON Format) auf Basis von [Apache FreeMarker](https://freemarker.apache.org "Apache FreeMarker") Templates über Rest WebServices ins HTML/PDF umgewandelt werden können.

## Vorbeiretung
Opensource Pakete **git**, **maven** müssen installiert sein.

### Install git und maven
For installation execute the following command:
```
yum install git, maven
```

### Set PATH variable
Extend the environment variable PATH, so that the OpenSource packages do not have to enter qualified:
```
export PATH=/QOpenSys/pkgs/bin:$PATH
```

## Installation

### Clone project
A local copy of the project must be created:
```
git -c http.sslVerify=false clone https://github.com/jsranko/si-xml-to-pdf.git
```

## Build project

```
cd si-xml-to-pdf/si-xml-to-pdf
mvn package
```

## Build example

```
cd si-xml-to-pdf/si-xml-to-pdf
gmake
```

## Project Links
.... to Freemarkter
.... to Openhtmlpdf
