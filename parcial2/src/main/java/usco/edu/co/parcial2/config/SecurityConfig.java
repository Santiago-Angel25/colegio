package usco.edu.co.parcial2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/rector/**").hasRole("RECTOR")
                        .requestMatchers("/docente/**").hasRole("DOCENTE")
                        .requestMatchers("/estudiante/**").hasRole("ESTUDIANTE")
                        .requestMatchers("/api/asignaturas/mis", "/api/asignaturas/*/horario").hasRole("DOCENTE")
                        .requestMatchers("/api/asignaturas/**").hasAnyRole("RECTOR", "ESTUDIANTE")
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .formLogin(login -> login
                        .loginPage("/login")
                        .successHandler(successHandler())
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception.accessDeniedPage("/sin-permiso"))
                .build();
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return (request, response, authentication) -> {
            String redirect = authentication.getAuthorities().stream()
                    .map(Object::toString)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .findFirst()
                    .map(authority -> switch (authority) {
                        case "ROLE_RECTOR" -> "/rector";
                        case "ROLE_DOCENTE" -> "/docente";
                        case "ROLE_ESTUDIANTE" -> "/estudiante";
                        default -> "/login";
                    })
                    .orElse("/login");

            response.sendRedirect(redirect);
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
