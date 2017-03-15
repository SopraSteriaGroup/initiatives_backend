# Token generation

Application that provides basic OAuth2 authentication with JWT

*Tokengen* using an H2 in-memory database to store users and authorities

## Getting started

Starting *Tokengen* only requires **Maven** and **Java 8**.

In project root use the following commands to start the application :

If *Maven* is installed : `mvn package`

then : `java -jar target/tokengen-1.0.0.jar`

If *Maven* isn't installed use : `java -jar CHEMIN_VERS_MON_JAR.jar`

## Usage

### Obtain an access token with a simple request

Call : `http://localhost:9081/api/tokens` only with username and password don't provide standard OAuth2 parameters or headers :

![swagger_simple_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027254/020f98d8-f464-11e6-85ec-e697b97388a2.png)

### Obtain an access token using OAuth2 standard request

Call : `http://localhost:9081/oauth/token` using method **POST** and provide (**Authorization**) header with the following value : 
`Basic ZGlnaWJhbms6ZGlnaWJhbmtzZWNyZXQ=`.

Request body must contain the following parameters :

| Param      | Value         |
|------------|---------------|
| username   | YOUR_USERNAME |
| password   | YOUR_PASSWORD |
| grant_type | password      |
| scope      | openid        |
| client_id  | digibank      |
| secret     | digibanksecret|

Like that : ![postman_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027531/d350d4fc-f464-11e6-9a76-8fa2d21761f6.png)

### Handling users and authorities

The application provides a way to manage users and authories. The API is documented using **Swagger** : `http://localhost:9081/swagger-ui.html`

![swagger_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027575/f682eb40-f464-11e6-8f87-1085fac60a7a.png)

### Token lifetime

Refresh token and access token lifetime are configurable using **application.yml** file, this way :

![applicationyml_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027758/a30e0458-f465-11e6-80b7-c443d05a71f4.png)

### Customize application

All parameters are configurable as explained [in the Spring Boot's documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).

For example, the lifetime of the access token is configurable this way : `java -jar target/tokengen-1.0.0.jar --auth.token.accessValiditySeconds=3600`

### Default users

The application creates out of box the following users and authorities :

| username     | password | roles                                      |
|--------------|----------|--------------------------------------------|
| jntakpe      | test     | ROLE_USER,ROLE_ADMIN                       |
| cbarillet    | test     | ROLE_USER,ROLE_ADMIN                       |
| sberger      | test     | ROLE_USER                                  |
| smaitre      | test     | ROLE_USER,ROLE_ADMIN                       |
| ccavelier    | test     | ROLE_USER,ROLE_ADMIN                       |
| tmarchandise | test     |                                            |

## Changelog
