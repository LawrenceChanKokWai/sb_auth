
## Authorization Server (Spring Boot)
## Section 3.1

### Main Configuration for the Authorization server (clientAuthentication)

### SecurityFilterChain clientAuthentication
- clientAuthentication requires:
  - authenticationConverter
  - authenticationProvider

```
  .clientAuthentication(authentication -> {
    authentication.authenticationConverter(null);
    authentication.authenticationProvider(null);
  })
```