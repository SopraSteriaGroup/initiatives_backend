# Initiatives microservices backend

Backend du project **Initiatives** basé sur une architecture microservices.

## Installation

### Pré-requis

L'utilisation de l'application nécessite les outils suivants :
* [Git](https://git-scm.com/book/fr/v1/D%C3%A9marrage-rapide-Installation-de-Git) pour la récupération des sources
* [Maven](https://maven.apache.org/install.html) pour gérer le cycle de vie de l'application (compilation, build, test, ...)
* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) comme runtime
* Un IDE tel qu'Intellij, NetBeans ou Eclipse (pour les plus courageux)
* [Apache Kafka](https://dzone.com/articles/running-apache-kafka-on-windows-os) pour gérer les appels asynchrones, l'alimentation des 
dashboards et la gestion des circuits breaker

L'installation de Kafka est facultative si vous utiliser *Docker* pour lancer les applications.