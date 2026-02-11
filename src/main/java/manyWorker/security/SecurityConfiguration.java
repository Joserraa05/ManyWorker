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
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter JWTAuthenticationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- CONFIGURACIÓN DE SEGURIDAD HTTP ---
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 1. ACTIVAR CORS (Esto usa el Bean corsConfigurationSource definido abajo)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // Rutas públicas
                .requestMatchers("/actor/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/trabajador").permitAll()
                .requestMatchers(HttpMethod.POST, "/cliente").permitAll()                

                // Rutas ADMINISTRADOR
                .requestMatchers("/admin/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/trabajador").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/cliente").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/banear/**").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/desbanear/**").hasAuthority("ADMINISTRADOR")

                // Rutas TRABAJADOR
                .requestMatchers("/trabajador/**").hasAuthority("TRABAJADOR")
                .requestMatchers(HttpMethod.PUT, "/trabajador").hasAuthority("TRABAJADOR")
                .requestMatchers(HttpMethod.DELETE, "/trabajador").hasAuthority("TRABAJADOR")

                // Rutas CLIENTE
                .requestMatchers("/cliente/**").hasAnyAuthority("CLIENTE", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.PUT, "/cliente").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/cliente").hasAuthority("CLIENTE")
                .requestMatchers("/cliente/miPerfil").hasAuthority("CLIENTE")

                // Endpoints compartidos
                .requestMatchers("/perfilSocial/**").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                
                // Endpoints Solicitud
                .requestMatchers(HttpMethod.GET, "/solicitudes/tarea/**").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.POST, "/solicitudes").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.GET, "/solicitudes").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.GET, "/solicitudes/{id}").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.PUT, "/solicitudes/{id}").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.DELETE, "/solicitudes/{id}").hasAnyAuthority("ADMINISTRADOR", "TRABAJADOR")
                .requestMatchers("/solicitudes/*/asignar").hasAuthority("TRABAJADOR")
                .requestMatchers("/solicitudes/*/finalizar").hasAuthority("TRABAJADOR")
                .requestMatchers("/solicitudes/*/comenzar").hasAuthority("TRABAJADOR")
                
                // Operaciones específicas de cliente
                .requestMatchers("/solicitudes/*/aceptar").hasAuthority("CLIENTE")
                .requestMatchers("/solicitudes/*/rechazar").hasAuthority("CLIENTE")
                .requestMatchers("/solicitudes/*/cancelar").hasAuthority("CLIENTE")
                .requestMatchers("/solicitudes/*/valorar").hasAuthority("CLIENTE")

                
                // Rutas MENSAJE
                .requestMatchers(HttpMethod.POST, "/mensajes/enviar").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/mensajes/**").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.DELETE, "/mensajes/**").hasAnyAuthority("CLIENTE", "TRABAJADOR", "ADMINISTRADOR")
                .requestMatchers(HttpMethod.POST, "/mensajes/broadcast").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/mensajes").hasAuthority("ADMINISTRADOR")
                
                // Rutas TUTORIALES
                .requestMatchers(HttpMethod.GET, "/tutoriales/**").permitAll() 
                .requestMatchers(HttpMethod.POST, "/tutoriales").hasAuthority("TRABAJADOR")
                .requestMatchers(HttpMethod.PUT, "/tutoriales/**").hasAuthority("TRABAJADOR")
                .requestMatchers(HttpMethod.DELETE, "/tutoriales/**").hasAnyAuthority("TRABAJADOR", "ADMINISTRADOR")
                
                // Rutas PERFIL SOCIAL
                .requestMatchers(HttpMethod.POST, "/perfilSocial").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.PUT, "/perfilSocial/**").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.DELETE, "/perfilSocial/**").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                .requestMatchers(HttpMethod.GET, "/perfilSocial").hasAuthority("ADMINISTRADOR")
                .requestMatchers(HttpMethod.GET, "/perfilSocial/**").hasAuthority("ADMINISTRADOR")
                
                // Endpoints de Categoría
                .requestMatchers(HttpMethod.GET, "/categorias/**").permitAll()
                .requestMatchers("/categorias/**").hasAuthority("ADMINISTRADOR")
                
                // Reglas para Tarea
                .requestMatchers("/tareas/**").hasAnyAuthority("CLIENTE", "TRABAJADOR")
                
                // Rutas SWAGGER
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
                
                // Resto de rutas requieren autenticación
                .anyRequest().authenticated())
                
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(JWTAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // --- 2. BEAN DE CONFIGURACIÓN CORS (Permitir TODO) ---
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Usamos allowedOriginPatterns("*") en lugar de allowedOrigins("*") 
        // porque allowCredentials(true) no funciona con el wildcard simple.
        configuration.setAllowedOriginPatterns(List.of("*")); 
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}