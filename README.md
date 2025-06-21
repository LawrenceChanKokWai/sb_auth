
## Authorization Server (Spring Boot)
## Section 3.3

### Main Configuration for the Authorization server (TokenGenerator)

### SecurityFilterChain tokenGenerator
- snippets as below:

```
  @Bean
    public OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator() {
        var jwtGenerator = UserJwtGenerator.init(new NimbusJwtEncoder(jwtConfiguration.jwkSource()));
        jwtGenerator.setJwtCustomizer(customizer());
        OAuth2TokenGenerator<OAuth2RefreshToken> refreshTokenGenerator = new ClientOAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(jwtGenerator, refreshTokenGenerator);
    }
```

1. To create a class that implements OAuth2TokenGenerator interface.
2. Search into the implementations strategy, choose which generator strategy to override with. (This project using JwtGenerator)
3. Create a User class, UserUtils helper class, this will get custom User class principal that will be used in overriding claimsBuilder.
4. customizer snippet:
```
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> customizer() {
        return context -> {
            if(ACCESS_TOKEN.equals(context.getTokenType())) {
                context.getClaims().claims(claims -> claims.put("authorities", getAuthorities(context)));
            }
        };
    }
```
5. create a private return String type getAuthorities helper method in return a map of claims.
```
  private String getAuthorities(JwtEncodingContext context) {
        return context.getPrincipal().getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }
```
6. create KeyUtils component class for getting the RSAKeyPair private key and public key
7. create a JwtConfiguration class
   - inject KeyUtils component class 
   - create bean for JwtDecoder by using keyUtils.
   - create a JWKSet to return to the JWTGenerator encoder.
8. create a component ClientOAuth2RefreshTokenGenerator class that implements `OAuth2TokenGenerator<OAuth2RefreshToken>`.
   - override the generate method, check if the token type is equals to the context token type. Not equals(ignore, return null) else create the refresh token.