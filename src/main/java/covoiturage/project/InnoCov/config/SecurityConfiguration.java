package covoiturage.project.InnoCov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static covoiturage.project.InnoCov.entity.enums.Role.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity

public class SecurityConfiguration {

    public static final String WHITE_LIST_URL = "/auth/**";
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers("/complaint/all-complaints").hasRole(ADMIN.name())
                                .requestMatchers("/complaint/complaints-by-target/**").hasRole(ADMIN.name())
                                .requestMatchers("/route-booking/route-bookings-creation-stats").hasRole(ADMIN.name())
                                .requestMatchers("/route-booking/user-creation-stats").hasRole(ADMIN.name())
                                .requestMatchers("/route/routes-creation-stats").hasRole(ADMIN.name())
                                .requestMatchers("/route/user-creation-stats").hasRole(ADMIN.name())
                                .requestMatchers("/user/activate/**").hasRole(ADMIN.name())
                                .requestMatchers("/user/deactivate/**").hasRole(ADMIN.name())
                                .requestMatchers("/route-booking/new-booking/**").hasRole(PASSENGER.name())
                                .requestMatchers("/api/route/available").hasRole(PASSENGER.name())
                                .requestMatchers("/route/driver-routes/").hasRole(DRIVER.name())
                                .requestMatchers(HttpMethod.PUT, "/route-booking/**").hasAnyRole(DRIVER.name(), PASSENGER.name())
                                .requestMatchers("/complaint/new-complaint").hasAnyRole(PASSENGER.name(), DRIVER.name())
                                .requestMatchers("/").hasRole(DRIVER.name())
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout
                        .logoutUrl("/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

