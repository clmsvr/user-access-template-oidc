

### Demonstração de aplicação Web Java Spring usando o Aws Cognito como Pool de Usuários Federados.

Recursos usados:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [Spring MVC](https://docs.spring.io/spring-framework/reference/web.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Security](https://docs.spring.io/spring-boot/3.4.0/reference/web/spring-security.html)
* [Spring Security OAuth 2.0 Login](https://docs.spring.io/spring-security/reference/servlet/oauth2/login/index.html)
* [Spring Security OAuth 2.0 Client](https://docs.spring.io/spring-security/reference/servlet/oauth2/client/index.html)
* [Aws Cognito](https://aws.amazon.com/pt/cognito/)
* [Aws Cognito Java Sdk 2.0](https://docs.aws.amazon.com/pt_br/sdk-for-java/latest/developer-guide/java_cognito-identity-provider_code_examples.html)
* [Thymeleaf](https://docs.spring.io/spring-boot/3.4.0/reference/web/servlet.html#web.servlet.spring-mvc.template-engines)
* [Herança de Templates - Thymeleaf](https://www.treinaweb.com.br/blog/thymeleaf-heranca-de-templates)
* [Database Initialization](https://docs.spring.io/spring-boot/how-to/data-initialization.html)
* [Validation](https://docs.spring.io/spring-boot/3.4.0/reference/io/validation.html)
* [MySQL](https://www.mysql.com/)
* [Lombok](https://projectlombok.org/)

### Funções

* Controle de acesso.
* Usuário com acesso controlado por "Roles" que são também um conjunto de "Authorities" cadastradas no banco de dados.
* Usuarios "Federados" via Aws Cognito.
* Interface para acesso e atualização de dados do usuário.

#### Comece por:

* Vai ser preciso uma conta no **Amazon** 
* Criar um [User Pool](https://docs.aws.amazon.com/cognito/latest/developerguide/what-is-amazon-cognito.html#what-is-amazon-cognito-user-pools) no **Aws Cognito**.

* Anotar para nossa configuração o **User Pool ID**. ex: us-east-1_FSSjl3xir

* Acessar da url de **"Discovery"** do Pool :  https://cognito-idp.us-east-1.amazonaws.com/**{{User Pool ID}}**/.well-known/openid-configuration
    * Ex: [https://cognito-idp.us-east-1.amazonaws.com/us-east-1_FSSjl3xir/.well-known/openid-configuration](https://cognito-idp.us-east-1.amazonaws.com/us-east-1_FSSjl3xir/.well-known/openid-configuration)

* Identificar para a nossa configuração as URLs:
    * **end_session_endpoint** . Ex: https://us-east-1fssjl3xir.auth.us-east-1.amazoncognito.com/logout
    * **issuer** . Ex: https://cognito-idp.us-east-1.amazonaws.com/us-east-1_FSSjl3xir

* Criar um [App Client](https://docs.aws.amazon.com/cognito/latest/developerguide/user-pool-settings-client-apps.html)
* Anotar o **Client Id** e o **Client Secret** para uso em nossa configuração.
* Na sessão **Login Pages** do **App Client** vai ser preciso adicionar as **Allowed callback URLs** :
    * http://localhost:8080/login/oauth2/code/cognito 

* Ainda na pagina de configuração do **App Client**, em **OAuth 2.0 grant types**, adicionar o fluxo **"Authorization Code Grant"**



#### Fedração de Usuários (Usuários Sociais)

Na página de **Overview** do User Pool, em **"Add sign-in with social providers"**, é possivel configurar e integrar outros povedores de identidade como o Google, Facbook e Microsoft.

Mas será preciso configurar estes provedores de identidade, criando **App Clients** em cada provedor, de forma semelhante ao que fizemos no **Cognito**.

Segue alguns links para ajudar neste propósito:
* **Google** : [OAuth Clients](https://console.cloud.google.com/auth/clients) [Doc.](https://developers.google.com/identity/openid-connect/openid-connect)
* **Microsoft** : [Micros. Entra](https://entra.microsoft.com/) [Doc.](https://learn.microsoft.com/pt-br/entra/identity-platform/v2-protocols-oidc)





