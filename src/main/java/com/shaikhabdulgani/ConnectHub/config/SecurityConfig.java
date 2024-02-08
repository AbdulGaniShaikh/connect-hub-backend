package com.shaikhabdulgani.ConnectHub.config;

import com.shaikhabdulgani.ConnectHub.filter.JwtAuthEntryPoint;
import com.shaikhabdulgani.ConnectHub.filter.JwtFilter;
import com.shaikhabdulgani.ConnectHub.service.PasswordEncoderService;
import com.shaikhabdulgani.ConnectHub.service.BasicUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * The `SecurityConfig` class configures security settings for a web application,
 * including authentication, authorization, and Cross-Origin Resource Sharing (CORS).
 *
 * <p>This configuration class utilizes Spring Security to define access rules,
 * authentication providers, and filters for securing the application. It also
 * includes CORS configuration to allow cross-origin requests from a specified origin.
 * </p>
 *
 * <p>The class defines a security filter chain that manages request authorization
 * and authentication. It configures a JWT authentication entry point, a JWT filter
 * for processing authentication tokens, and sets up a basic authentication provider.
 * Additionally, it defines access rules for specific URLs, session management policies,
 * and CORS settings.</p>
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig{

    @Autowired
    private JwtAuthEntryPoint entryPoint;

    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private PasswordEncoderService encoder;

    @Autowired
    private BasicUserService userService;

    /**
     * Configures the security filter chain for the application.
     * This method defines security policies, access rules, and authentication providers
     * to secure various endpoints and resources in the application.
     *
     * @param http The HttpSecurity instance to configure the security settings.
     * @return The configured SecurityFilterChain for managing security policies.
     * @throws Exception If an error occurs during the configuration process.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        String[] authFreeUrl = new String[]{
                "/api/auth/**",
                "/api/image/**",
                "/swagger-ui/**",
                "/swagger-resources/*",
                "/v3/api-docs/**",
                "/error"
        };
        http
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                ).authorizeHttpRequests(auth -> auth
                        .requestMatchers(authFreeUrl).permitAll()
                ).authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").hasAuthority("USER")
                ).sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        return http.build();
    }

    /**
     * Configures and provides an AuthenticationProvider instance for Dao-based authentication.
     * This method sets up a DaoAuthenticationProvider, assigning a UserDetailsService
     * for retrieving user details and a password encoder for handling password verification.
     *
     * @return The configured DaoAuthenticationProvider for Dao-based authentication.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(encoder.passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Retrieves the configured AuthenticationManager from the provided AuthenticationConfiguration.
     * This method is responsible for obtaining the authentication manager,
     * allowing access to the configured authentication providers.
     *
     * @param config The AuthenticationConfiguration instance.
     * @return The configured AuthenticationManager for managing authentication providers.
     * @throws Exception If an error occurs while obtaining the AuthenticationManager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }


    /**
     * <p>Configures CORS (Cross-Origin Resource Sharing) settings for the application.
     * This allows or restricts cross-origin requests based on the provided configurations.
     * Adds CORS mappings to the provided registry.</p>
     *
     * <p>This method allows specifying allowed origins, methods, and credentials for CORS requests.</p>
     * <p>In this configuration:
     * <ul>
     *     <li>`addMapping("/**")`:  Applies CORS configurations to all endpoints.</li>
     *     <li>`.allowCredentials(true)`:  Allows including credentials such as cookies in CORS requests.</li>
     *     <li>`.allowedMethods("*")`:  Allows all HTTP methods for cross-origin requests.</li>
     *     <li>`.allowedOrigins("<a href="http://localhost:3000">http://localhost:3000</a>")`:  Allows requests only from the specified origin.</li>
     * </ul>
     * </p>
     * @return The configured WebMvcConfigurer for handling CORS settings.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(true)
                        .allowedMethods("*")
                        .allowedOrigins("http://localhost:3000");
            }
        };
    }
}
