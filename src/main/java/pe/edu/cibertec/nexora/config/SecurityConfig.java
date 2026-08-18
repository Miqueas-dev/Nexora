package pe.edu.cibertec.nexora.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            .csrf(csrf ->
                    csrf.disable())

            .cors(
                    Customizer.withDefaults())

            .authorizeHttpRequests(auth -> auth

                // LOGIN
            		.requestMatchers(
            		        HttpMethod.POST,
            		        "/api/auth/login",
            		        "/api/auth/registro-cliente"
            		)
            		.permitAll()

                .requestMatchers(
                        "/api/auth/me",
                        "/api/auth/logout")
                .authenticated()

                // PRODUCTOS: todos consultan
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/productos/**")
                .hasAnyRole(
                        "ADMIN",
                        "VENDEDOR",
                        "CLIENTE")

                // PRODUCTOS: solo admin modifica
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/productos/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/productos/**")
                .hasRole("ADMIN")

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/productos/**")
                .hasRole("ADMIN")

                // CLIENTES disponibles para ventas
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/usuarios/clientes")
                .hasAnyRole(
                        "ADMIN",
                        "VENDEDOR")

                // ADMINISTRA USUARIOS
                .requestMatchers(
                        "/api/usuarios/**")
                .hasRole("ADMIN")

                // CLIENTE: sus comprobantes
                .requestMatchers(
                        "/api/comprobantes/mios/**")
                .hasRole("CLIENTE")

                // VENDEDOR: sus ventas
                .requestMatchers(
                        "/api/comprobantes/vendedor/**")
                .hasRole("VENDEDOR")

                // VENDEDOR registra ventas
                .requestMatchers(
                        HttpMethod.POST,
                        "/api/comprobantes")
                .hasRole("VENDEDOR")

                // ADMIN consulta todas las ventas
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/comprobantes/**")
                .hasRole("ADMIN")

                // CATÁLOGOS
                .requestMatchers(
                        HttpMethod.GET,
                        "/api/marcas/**",
                        "/api/estados/**",
                        "/api/tipos/**")
                .hasAnyRole(
                        "ADMIN",
                        "VENDEDOR",
                        "CLIENTE")

                // MODIFICACIÓN CATÁLOGOS
                .requestMatchers(
                        "/api/marcas/**",
                        "/api/estados/**",
                        "/api/tipos/**")
                .hasRole("ADMIN")

                .anyRequest()
                .authenticated()
            )

            .formLogin(
                    form -> form.disable())

            .httpBasic(
                    basic -> basic.disable())

            .exceptionHandling(ex -> ex

                .authenticationEntryPoint(
                    (request,
                     response,
                     exception) ->

                        response.sendError(401)
                )

                .accessDeniedHandler(
                    (request,
                     response,
                     exception) ->

                        response.sendError(403)
                )
            );

        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration
                .getAuthenticationManager();
    }
}