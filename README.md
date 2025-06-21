
## Authorization Server (Spring Boot)
## Section 3

### Main Configuration for the Authorization server

### SecurityFilterChain
- Create a bean with order of 1. As there will be more than one security filter in the application to run.
- Configurations required:
  - cors (on the configurationSource)
  - authorizationServer of OAuth2AuthorizationServerConfigurer which requires:
    - tokenGenerator
    - clientAuthentication 
      - authenticationConverter 
      - authenticationProvider
    - oidc 
      - use withDefaults
  - securityMatcher
    - getEndpointMatcher
  -exceptionHandling
    - accessDeniedPage
    - defaultAuthenticationEntryPointFor
      - new LoginAuthenticationEntryPoint
      - new MediaTypeRequestMatcher