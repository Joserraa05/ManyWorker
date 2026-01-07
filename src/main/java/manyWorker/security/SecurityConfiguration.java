package manyWorker.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/actor/login").permitAll()
                .requestMatchers("/tutoriales/**").permitAll()
                
                // Endpoints de ADMINISTRADOR
                .requestMatchers(HttpMethod.GET, "/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/categorias/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers("/mensajes/broadcast").hasAuthority("ADMINISTRADOR")
                
                // Endpoints de CLIENTE
                .requestMatchers("/cliente/**").hasAuthority("CLIENTES")
                .requestMatchers("/tareas/**").hasAuthority("CLIENTES")
                .requestMatchers("/solicitudes/**/aceptar").hasAuthority("CLIENTES")
                .requestMatchers("/solicitudes/**/rechazar").hasAuthority("CLIENTES")
                
                // Endpoints de TRABAJADOR
                .requestMatchers("/trabajador/**").hasAuthority("TRABAJADOR")
                .requestMatchers("/solicitudes", "/solicitudes/**").hasAuthority("TRABAJADOR")
                
                // Endpoints compartidos (CLIENTE y TRABAJADOR)
                .requestMatchers("/mensajes/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
                .requestMatchers("/perfilSocial/**").hasAnyAuthority("CLIENTES", "TRABAJADOR")
                
                // Para desarrollo - permitir acceso a Swagger
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                
                // Cualquier otra petición necesita autenticación
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*")); // En producción, especifica los dominios
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}