
## Authorization Server (Spring Boot)
## Section 3.2

### Main Configuration for the Authorization server (CorsConfigurationSource)

### SecurityFilterChain cors
- corsConfigurationSource requires:
  - setAllowCredentials
  - setAllowedOrigins
  - setAllowedHeaders
  - setExposedHeaders
  - setAllowedMethods
  - setMaxAge
  - register registerCorsConfiguration with UrlBasedCorsConfigurationSource

```
  @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        var corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of(""));
        corsConfiguration.setAllowedHeaders(Arrays.asList());
        corsConfiguration.setExposedHeaders(Arrays.asList());
        corsConfiguration.setAllowedMethods(Arrays.asList());
        corsConfiguration.setMaxAge(3600L);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
```