package id.ac.ui.cs.advprog.beachievement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF (pakai JWT, bukan cookie)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Tidak
                                                                                                              // ada
                                                                                                              // session
                .authorizeHttpRequests(auth -> auth
                        // Endpoint admin — hanya ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        // Endpoint student progress — harus login (STUDENT atau ADMIN)
                        .requestMatchers("/api/student-progress/**").authenticated()
                        // Endpoint achievements (me) — harus login
                        .requestMatchers("/api/achievements/me").authenticated()
                        // Endpoint achievements public — bebas (tidak perlu login untuk lihat profil
                        // publik)
                        .requestMatchers("/api/achievements/*/public").permitAll()
                        // Endpoint event dari be-bacaan — KEPUTUSAN: pakai Opsi A (forward token)
                        // Jadi harus authenticated (be-bacaan forward token user)
                        .requestMatchers("/api/events/**").authenticated()
                        // Semua yang lain — bebas (bisa diubah nanti)
                        .anyRequest().permitAll())
                // Pasang filter kita sebelum filter default Spring Security
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}