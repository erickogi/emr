package com.kogi.emr.security;

import com.kogi.emr.security.jwt.AuthEntryPointJwt;
import com.kogi.emr.security.jwt.AuthTokenFilter;
import com.kogi.emr.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }
  /**
   * This method is used to configure the authentication provider.
   * @return an authentication provider.
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
  /**
   * This method is used to configure the authentication manager.
   * @param authConfig
   * @return an authentication manager.
   * @throws Exception
   */
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }
  /**
   * This method is used to configure the password encoder.
   * @return a password encoder.
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  /**
   * This method is used to configure the security filter chain.
   * @param http
   * @return a security filter chain.
   * @throws Exception
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth ->
          auth.requestMatchers("/api/auth/**").permitAll()
                  .requestMatchers("/api/items/**").permitAll()
                  .requestMatchers("/api/categories/**").permitAll()
                  .requestMatchers("/api/orders/**").permitAll()
              .requestMatchers("/swagger-ui/**").permitAll()
              .requestMatchers("/api-docs/**").permitAll()
              .anyRequest().authenticated()
        );

    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
//  @Override
//  public void addCorsMappings(CorsRegistry registry) {
//
//    registry.addMapping("/**")
//            .allowedOrigins("http://localhost:3000/")
//            .allowedMethods("GET", "POST")
//            .allowedHeaders("header1", "header2", "header3")
//            .exposedHeaders("header1", "header2")
//            .allowCredentials(true).maxAge(3600);
//
//
//  }
}
