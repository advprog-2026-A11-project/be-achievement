package id.ac.ui.cs.advprog.beachievement.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${auth.service.url}")
    private String authServiceUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            HttpRequest authRequest = HttpRequest.newBuilder()
                    .uri(URI.create(authServiceUrl + "/api/auth/me"))
                    .header("Authorization", "Bearer " + token)
                    .GET()
                    .build();

            HttpResponse<String> authResponse = httpClient.send(
                    authRequest, HttpResponse.BodyHandlers.ofString());

            if (authResponse.statusCode() != 200) {
                filterChain.doFilter(request, response);
                return;
            }

            JsonNode body = objectMapper.readTree(authResponse.body());
            JsonNode profile = body.get("profile");

            if (profile == null || profile.isNull()) {
                filterChain.doFilter(request, response);
                return;
            }

            JsonNode profileId = profile.get("id");
            JsonNode profileRole = profile.get("role");
            JsonNode profileIsActive = profile.get("isActive");

            if (profileId == null || profileRole == null || profileIsActive == null
                    || !profileIsActive.asBoolean()) {
                filterChain.doFilter(request, response);
                return;
            }

            String userId = profileId.asText();
            String role = profileRole.asText();


            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_" + role)));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            logger.warn("Failed to validate token with auth service: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}