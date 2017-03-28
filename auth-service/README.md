# Token generation

Application that provides basic OAuth2 authentication with JWT

*auth-service* using an H2 in-memory database to store users and authorities

## Getting started

Starting *auth-service* only requires **Maven** and **Java 8**.

In project root use the following commands to start the application :

If *Maven* is installed : `mvn package`

then : `java -jar target/auth-service-1.0.0.jar`

If *Maven* isn't installed use : `java -jar CHEMIN_VERS_MON_JAR.jar`

## Usage

### Obtain an access token with a simple request

Call : `http://localhost:9081/api/tokens` with accessToken, eventually ssoProvider (if different from 'linkedin') and don't provide standard OAuth2 parameters or headers :

<!--TODO : change-->
![swagger_simple_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027254/020f98d8-f464-11e6-85ec-e697b97388a2.png)

### Obtain an access token using OAuth2 standard request

Call : `http://localhost:9081/oauth/token` using method **POST** and provide (**Authorization**) header with the following value : 
`Basic ZGlnaWJhbms6ZGlnaWJhbmtzZWNyZXQ=`.

Request body must contain the following parameters :

| Param         | Value                           |
|---------------|---------------------------------|
| username      | YOUR_EXTERNAL_ACCESS_TOKEN      |
| password      | BLANK                           |
| grant_type    | password                        |
| scope         | openid                          |
| client_id     | initiatives_app                 |
| secret        | initiatives_super_secure_secret |
| ssoProvider   | YOUR_SSO_PROVIDER               |

<!--TODO : change-->
Like that : ![postman_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027531/d350d4fc-f464-11e6-9a76-8fa2d21761f6.png)

### Mocking the SSO provider

Running with the profile 'bouchon' activate the fake SSO provider. 
A usefull endpoint is also activated to create a access token for this fake provider.

1 - Call : `http://localhost:9081/oauth/token` using method **GET** with the following parameters : 

| Param      | Value            |
|------------|------------------|
| id         | YOUR_FAKE_SSO_ID |
| firstName  | YOUR_FIRSTNAME   |
| lastName   | YOUR_LASTNAME    |

2 - Call : `http://localhost:9081/api/tokens` with accessToken and ssoProvider with the following values :
* accessToken : PREVIOUS_ENDPOINT_RESPONSE
* ssoProvider : fakesso
 
 
### Handling users and authorities

The application provides a way to manage users and authories. The API is documented using **Swagger** : `http://localhost:9081/swagger-ui.html`

### Token lifetime

Refresh token and access token lifetime are configurable using **application.yml** file, this way :

<!--TODO : change-->
![applicationyml_tokengen](https://cloud.githubusercontent.com/assets/3605418/23027758/a30e0458-f465-11e6-80b7-c443d05a71f4.png)

### Customize application

All parameters are configurable as explained [in the Spring Boot's documentation](http://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html).


### Default users

The application creates out of box the following users and authorities :



| username            |  roles                 |
|---------------------|------------------------|
| fakesso#jntakpe     |  ROLE_USER,ROLE_ADMIN  |
| fakesso#cbarillet   |  ROLE_USER,ROLE_ADMIN  |
| fakesso#rjansem     |  ROLE_USER,ROLE_ADMIN  |
| fakesso#cegiraud    |  ROLE_USER,ROLE_ADMIN  |
| fakesso#crinfray    |  ROLE_USER,ROLE_ADMIN  |
| fakesso#anycz       |  ROLE_USER,ROLE_ADMIN  |
| fakesso#fleriche    |  ROLE_USER,ROLE_ADMIN  |
| fakesso#mbouhamyd   |  ROLE_USER,ROLE_ADMIN  |
| fakesso#nmpacko     |  ROLE_USER,ROLE_ADMIN  |
| fakesso#ocoulibaly  |  ROLE_USER,ROLE_ADMIN  |
| fakesso#mmarquez    |  ROLE_USER,ROLE_ADMIN  |
| fakesso#tchoteau    |  ROLE_USER,ROLE_ADMIN  |
| fakesso#eparizot    |  ROLE_USER,ROLE_ADMIN  |


## Changelog
