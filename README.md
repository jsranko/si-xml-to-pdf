# si-xml-to-pdf

## Install

### Set PATH variable

Extend the environment variable PATH, so that the OpenSource packages do not have to enter qualified:

```
export PATH=/QOpenSys/pkgs/bin:$PATH
```

### Install git

Opensource package **git**, **maven** must be installed. For installation execute the following command:
```
yum install git, maven
```

### Clone project
A local copy of the project must be created:
```
git -c http.sslVerify=false clone https://github.com/jsranko/si-xml-to-pdf.git
```

### Build project

```
cd si-xml-to-pdf/si-xml-to-pdf
mvn package
```
