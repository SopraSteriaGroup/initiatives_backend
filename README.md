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
dashboards et la gestion des circuits breaker. *Kafka* nécessite d'avoir *Apache Zookeeper* installé, se reférer au lien précédent pour
les instructions d'installation de *Zookeeper*.

L'installation de Kafka est facultative si vous utiliser *Docker* pour lancer les applications.

### Lancer l'application

#### Préparation 

Le JDK8 doit être installé.

Après avoir installé *Git* et demandé les autorisations sur le repository. Se placer dans le répertoire qui contiendra les sources de 
l'application saisir la commande ``git clone https://github.com/SopraSteriaGroup/initiatives_backend``.

La CLI devrait afficher le message : *Checking connectivity... done.*

Après avoir installé *Apache Kafka* (et Zookeeper), démarrer *Zookeeper* puis *Kafka*, la CLI *Kafka* devrait se terminer avec le message : 
*INFO \[Kafka Server 0\], started (kafka.server.KafkaServer)*. 

Après avoir installé Maven, à la racine du projet, exécutez la commande ``mvn clean package``.
 
La CLI devrait afficher *BUILD SUCCESS*.

#### Démarrage des applications

###### Sans Docker

Les applications doivent être démarrées dans l'ordre suivant :

* [registry-server](/registry-server)
* [config-server](/config-server)
* [auth-service](/auth-service)
* puis les différents services métiers (à compléter ici)
* [proxy-server](/proxy-server)

et eventuellement pour visualiser les dashboards

* [dashboard-admin](/dashboard-admin), [dashboard-hystrix](/dashboard-hystrix), [dashboard-zipkin](/dashboard-zipkin)

Les applications peuvent être démarrées depuis l'IDE ou en exécutant la commande ``mvn spring-boot:run``.

###### Avec Docker

A la racine du projet exécuter la commande ``docker-compose up``

###### Visualiser les applications

Les instances des applications démarées sont visibles depuis Eureka (registry-server) à l'adresse ``http://localhost:8761/``.

Les documentations des APIs sont disponibles à l'adresse ``http://localhost:9080/NOM_DU_SERVICE/swagger-ui.html``, par exemple 
``http://localhost:9080/auth-service/swagger-ui.html`` pour le service *auth-service*.

Une application d'administration [dashboard-admin](/dashboard-admin) basée sur 
[Spring Boot Admin](https://github.com/codecentric/spring-boot-admin) démarre à l'adresse ``http://localhost:6363``.
Le *dashboard-admin* permet de visualiser l'ensemble des applications ainsi que leurs propriétés respectives ou encore de modifier à chaud 
certaines propriétés ou niveau de log.