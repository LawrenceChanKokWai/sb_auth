
## Authorization Server (Spring Boot)
## Section 2

### Setting up communication on authorization server to the front-end client

### security package
- AuthorizationServerConfig Class
  - To tell the authorization server, the user information from the client. By creating a bean, using the existing client provided by spring.
    - Using RegisteredClientRepository interface.
    - Returning a new JdbcRegisteredClientRepository object by using JdbcTemplate.
    - existing in-build table used at oauth2_registered_client.
  
  - registered client holds required fields and more as below:
    - clientId
    - clientSecret
    - clientAuthenticationMethod
    - authorizationGrantTypes (type of flow)
    - scopes (from the resource owner)
    - redirectUri
    - postLogoutRedirectUri
    - clientSettings (if user is required for Authorization Consent)
    - tokenSettings (for refresh tokens duration, access token time to live, etc)

  - Test creating client on runner
    ```
        IF findByClientId of client is null
            create a registered client
    ``` 