#TODO : Change the old doc !
#Initiatives microservices backend

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

## Services techniques

L'architecture utilise la stack Netflix OSS qui permet d'intégrer les patterns classiques aux application distribuées.
Les composants Netflix sont intégrés via Spring Cloud.

Les principales briques techniques sont : 

* un [serveur de configuration](config-server), basé sur [Archauis](https://github.com/Netflix/archaius/wiki)
* un [annuaire de service](/registry-server), basé sur [Eureka](https://github.com/Netflix/eureka/wiki)
* une [proxy-server](/proxy-server), basée sur [Zuul](https://github.com/Netflix/zuul/wiki)
* un [dashboard de circuits](/dashboard-hystrix), basé sur [Turbine](https://github.com/Netflix/turbine/wiki)
* un [dashboard de Zipkin](/dashboard-zipkin), basé sur [Zipkin](https://github.com/Netflix/turbine/wiki)
* un [dashboard d'administration](/dashboard-admin), basé sur [Spring Boot Admin](http://codecentric.github.io/spring-boot-admin/1.5.0-SNAPSHOT/)

![Archi technique](https://cloud.githubusercontent.com/assets/3605418/24072976/d74f96d6-0bef-11e7-938b-55c9e48c2824.png)


#### Serveur de configuration ([doc](http://cloud.spring.io/spring-cloud-static/spring-cloud-config/1.2.1.RELEASE/))

Le serveur de configuration permet d'avoir une configuration centralisée pour les systèmes distribués. Il est possible de brancher le 
serveur de configuration sur un gestionnaire de source comme Git ou Subversion.
En développement la configuration est chargée directement depuis le filesystem dans le répertoire 
[classpath:/shared](/config-server/src/main/resources).
La configuration est chargée depuis le repository Github [Sopra inititiatives properties](https://github.com/SopraSteriaGroup/initiatives_properties) 

###### Utilisation côté client

Les applications souhaitant récupérer leur configuration doivent créer un fichier 
[bootstrap.yml](/registry-server/src/main/resources/bootstrap.yml).
Ce fichier chargé avant le démarrage de l'application cliente permet la connexion au serveur de configuration pour récupérer la 
configuration de l'application :

```yml
spring:
    application:
        name: proxy-server
    cloud:
        config:
            uri: ${config.uri:http://localhost:8888}
            fail-fast: true
```

Dans cet exemple, l'application nommée *proxy-server* se connecte au serveur de configuration à l'adresse ``http://localhost:8888`` et 
récupère la configuration depuis un fichier de configuration possédant le même nom 
(ici [proxy-server.yml](https://github.com/SopraSteriaGroup/initiatives_properties/blob/master/proxy-server.yml))

###### Changement de configuration dynamique

Il est possible de mettre à jour dynamiquement la configuration des applications. Pour celà, les beans Spring doivent être annotés de 
``@RefreshScope``.

Pour changer la dynamiquement la configuration, il sera nécessaire :
* de mettre à jour la configuration depuis le serveur de configuration
* d'appeler l'URL de rafraichissement de la configuration (par exemple ``http://localhost:9080/account-service/refresh``)

Ou alors, Il possible de changer les propriétés dynamiquement également depuis l'application d'administration *dashboard-admin* :
![Admin properties](https://cloud.githubusercontent.com/assets/3605418/24073041/1943d510-0bf1-11e7-88a3-8b4034aa9d8c.png)


Il est également possible d'automatiser le processus en utilisant les
 [webhooks GIT](http://cloud.spring.io/spring-cloud-static/spring-cloud-config/1.2.1.RELEASE/#_push_notifications_and_spring_cloud_bus)

#### Annuaire de services ([doc](http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_service_discovery_eureka_clients))

Brique essentielle d'une architecture distribuée, le serveur d'annuaire permet la détection automatique des instances déployées.
Les instances des applications sont accédées via leur nom (par exemple *account-service*) plutôt que par leurs adresses physiques/IPs. Les 
applications n'ont plus besoin de connaitre les adresses des instances.
L'implémentation de l'annuaire de service est [Eureka](https://github.com/Netflix/eureka/wiki) de Netflix.

Eureka démarre en l'annotant l'application serveur ``@EnableEurekaServer``.

Les applications clientes peuvent s'enregistrer sur Eureka avec l'annotation ``@EnableDiscoveryClient``. L'application cliente lors de son 
démarrage s'enregistre sur Eureka qui fournira des metadatas telles que l'URL, le port, le fil de vie (heathcheck), ... de l'instance 
(ou des instances).

Eureka reçoit des messages de 'heartbeat' provenant des applications clientes, si aucun message n'est reçu, en fonction d'une 'timetable'
 configurable, Eureka supprimera l'instance.
 
Par défaut, Eureka démarre à l'adresse ``http://localhost:8761``. 
![Eureka IHM](https://cloud.githubusercontent.com/assets/3605418/24073146/2225478e-0bf3-11e7-940f-90948b99ef1a.png)


#### Gateway ([doc](http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_router_and_filter_zuul))

La gateway de l'architecture microservices est le point d'entrée unique de l'architecture microservices. L'implémentation choisie est 
[Zuul](https://github.com/Netflix/zuul/wiki) de Netflix.

L'affichage d'une page web ou mobile peut nécessiter l'appel à une dizaine de microservices différents. Il n'est pas envisageable pour 
l'application cliente de connaitre l'ensemble des adresses physiques des microservices. Pour répondre à cette problématique, la gateway 
devient la seule adresse à connaitre pour les applications clientes. Par exemple, l'url de *auth-service* 
``http://localhost:9081/api/tokens`` devient ``http://localhost:9080/auth-service/api/tokens``.
 
Une autre utilité de la Gateway Zuul est également la possibilité d'utiliser en front des protocoles web-friendly comme HTTP avec du JSON et
 sur le backend d'autres protocoles comme AMQP, Google Protobuff, etc.

Zuul peut également être utilisé comme pour router les requêtes, authentifier les utilisateurs, visualiser l'utilisation du système, stress 
tester, canary tester, migrer les services, monitorer le traffic ou encore gérer les réponses statiques.

La gateway Zuul démarre à l'aide de l'annotation ``@EnableZuulProxy``. La configuration basique peut être trouvée ci-dessous : 

```yml
zuul:
  host:
    connect-timeout-millis: 2000
    socket-timeout-millis: 2000
  ssl-hostname-validation-enabled: false
  ignoredServices: '*'
  routes:
    auth-service:
      path: /auth-service/**
      sensitiveHeaders:
    my-service-a:
      path: /my-service-a/**
      sensitiveHeaders:
    my-service-b:
      path: /my-service-b/**
      sensitiveHeaders:
```

Cette configuration signifie que toutes les urls commençant par ``/auth-service`` seront routées vers le service *auth-service*.
Les services *my-service-a-service* et *my-service-b-service* étant enregistrés via Eureka, les adresses de ces services seront retrouvées 
via Eureka et seront automatiquement 'load-balancés' via Ribbon.

#### Dashboard Hystrix ([doc](http://cloud.spring.io/spring-cloud-netflix/spring-cloud-netflix.html#_circuit_breaker_hystrix_clients))

Hystrix est l'implémentation du pattern [Circuit breaker](http://martinfowler.com/bliki/CircuitBreaker.html) permettant de controler la 
latence et les erreurs dues à des appels réseaux. L'idée essentielle est d'empêcher les erreurs en cascade dans un environnement distribué.
Hystrix permet de 'fail-fast' mais de se rétablir rapidement créant ainsi une architecture tolérante aux erreurs capable de se rétablir de 
manière autonome (self-heal). Hystrix encapsule les appels extérieurs dans un thread à part permettant de configurer une méthode de fallback
 en cas d'erreur. Dès sa conception, le système prévoit les pannes.
 
De plus, Hystrix remonte des indicateurs concernant le résultat de la requête et le temps de réponse.

Ces métriques peuvent être aggrégées via [Turbine](https://github.com/Netflix/Turbine/wiki). Turbine est un aggrégateur de flux dans notre 
cas utilisant [Apache Kafka](https://kafka.apache.org/) un système de messagerie très performant et hautement distribué.

Ci-dessous, un exemple de fonctionnement d'Hystrix lors d'appels à un service en jouant sur les temps de réponse :

<img width="880" src="https://cloud.githubusercontent.com/assets/6069066/14194375/d9a2dd80-f7be-11e5-8bcc-9a2fce753cfe.png">

<img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127349/21e90026-f628-11e5-83f1-60108cb33490.gif">	| <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127348/21e6ed40-f628-11e5-9fa4-ed527bf35129.gif"> | <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127346/21b9aaa6-f628-11e5-9bba-aaccab60fd69.gif"> | <img width="212" src="https://cloud.githubusercontent.com/assets/6069066/14127350/21eafe1c-f628-11e5-8ccd-a6b6873c046a.gif">
--- |--- |--- |--- |
| `0 ms de latence` | `500 ms de latence` | `800 ms de latence` | `1100 ms de latence`
|Système fonctionnant normalement. 22 requêtes/secondes. Le nombre de threads actifs reste faible. Le temps de réponse médian autour de 50ms. | Le nombre de threads actifs augmente fortement. Le nombre de threads est épuisé dans certains cas et le nombre d'erreurs autour de 30%. Le circuit reste fermé. | État semi-ouvert, le nombre d'erreur dépasse 50% et le circuit s'ouvre, après quelques temps les thread sont libérés et le système répond à nouveau donc le circuit est refermé puis reouvert ... | Le système répond trop lentement et toutes les requêtes tombent en échec. Le circuit ouvert en permanence car toutes les requêtes timeout.

#### Dashboard Zipkin ([doc](http://zipkin.io/))

###### Sleuth

Dans un système distribué une requête provenant du frontend peut résulter en une multitude d'appels dans le backend. Néanmoins, cet ensemble 
d'appels doit être traçable facilement afin de pouvoir en extraire les indicateurs business essentiels.

[Spring Sleuth](http://cloud.spring.io/spring-cloud-static/spring-cloud-sleuth/1.0.10.RELEASE/) implémente le pattern 
[Dapper](http://research.google.com/pubs/pub36356.html) de Google afin tagger les logs. Lorsque le système microservices reçoit une requête 
provenant du frontend celle-ci est taggée avec un identifiant unique. L'ensemble des appels sous-jacents gardera cet identifiant ainsi nous 
pouvons tracer un appel inter-services comme expliqué ci-dessous :

![Sleuth tracing](https://raw.githubusercontent.com/spring-cloud/spring-cloud-sleuth/master/docs/src/main/asciidoc/images/trace-id.png)

A présent, les logs sont taggées et nous pouvons suivre par exemple l'ensemble des appels provoqués par une demande de virement. Les logs 
peuvent être traitées par des outils tels *logstash*, *splunk*, ...

###### Zipkin

Zipkin est projet provenant de Twitter permettant de visualiser les latences des différents services. Le serveur Zipkin enregistre 
l'ensemble des latences dans un store et fourni une interface pour debugger inviduellement chaque appel.

![Zipkin Dashboard](https://cloud.githubusercontent.com/assets/3605418/24073157/4962ba0c-0bf3-11e7-92c6-f3f644d1e268.png)

###### Apache Kafka

*Apache Kafka* est un système de messagerie distribué, en mode publish-subscribe, persistant les données qu’il reçoit, conçu pour facilement
 monter en charge et supporter des débits de données très importants.
 
Kafka conserve les données qu’il reçoit dans des **topics**, correspondant à des catégories de données.
On nomme les systèmes qui publient des données dans des topics Kafka des **producers**.
Les **consumers**, sont les systèmes qui vont lire les données des topics.

![Kafka architecture](https://cloud.githubusercontent.com/assets/3605418/24073505/a2f684cc-0bf8-11e7-98d7-71dc0953676b.png)