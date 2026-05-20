package id.ac.ui.cs.advprog.beachievement.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

class JwtAuthFilterTest {

  private final HttpClient httpClient = org.mockito.Mockito.mock(HttpClient.class);
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final FilterChain filterChain = org.mockito.Mockito.mock(FilterChain.class);
  private final MockHttpServletResponse response = new MockHttpServletResponse();

  @AfterEach
  void tearDown() {
    SecurityContextHolder.clearContext();
  }

  @Test
  void skipsFilteringWhenAuthorizationHeaderMissing() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();

    filter.doFilter(request, response, filterChain);

    verify(httpClient, never()).send(any(HttpRequest.class), any());
    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void skipsFilteringWhenAuthorizationHeaderIsNotBearer() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Basic credentials");

    filter.doFilter(request, response, filterChain);

    verify(httpClient, never()).send(any(HttpRequest.class), any());
    verify(filterChain).doFilter(request, response);
    assertNull(SecurityContextHolder.getContext().getAuthentication());
  }

  @Test
  void authenticatesWhenAuthServiceReturnsActiveProfile() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    whenSuccessfulAuthResponse(
        "{\"profile\":{\"id\":\"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa\","
            + "\"role\":\"STUDENT\",\"isActive\":true}}");

    filter.doFilter(request, response, filterChain);

    var authentication = SecurityContextHolder.getContext().getAuthentication();
    assertEquals("aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa", authentication.getName());
    assertTrue(authentication.getAuthorities().stream()
        .anyMatch(authority -> "ROLE_STUDENT".equals(authority.getAuthority())));
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void skipsAuthenticationWhenAuthServiceReturnsNonOkStatus() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    whenAuthResponse(401, "{}");

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void skipsAuthenticationWhenProfileIsNull() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    whenSuccessfulAuthResponse("{\"profile\":null}");

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void skipsAuthenticationWhenProfileFieldsAreMissing() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    whenSuccessfulAuthResponse("{\"profile\":{\"id\":\"user-id\",\"isActive\":true}}");

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void skipsAuthenticationWhenProfileIsInactive() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    whenSuccessfulAuthResponse(
        "{\"profile\":{\"id\":\"aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa\","
            + "\"role\":\"STUDENT\",\"isActive\":false}}");

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void continuesFilterChainWhenHttpClientThrowsIoException() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    when(httpClient.send(any(HttpRequest.class),
        org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
        .thenThrow(new IOException("boom"));

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  @Test
  void continuesFilterChainWhenHttpClientThrowsInterruptedException() throws Exception {
    JwtAuthFilter filter = buildFilter();
    MockHttpServletRequest request = bearerRequest();
    when(httpClient.send(any(HttpRequest.class),
        org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
        .thenThrow(new InterruptedException("interrupted"));

    filter.doFilter(request, response, filterChain);

    assertNull(SecurityContextHolder.getContext().getAuthentication());
    verify(filterChain).doFilter(request, response);
  }

  private JwtAuthFilter buildFilter() {
    return new JwtAuthFilter(httpClient, objectMapper, "http://auth-service");
  }

  private MockHttpServletRequest bearerRequest() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.addHeader("Authorization", "Bearer token");
    return request;
  }

  private void whenSuccessfulAuthResponse(String body) throws Exception {
    whenAuthResponse(200, body);
  }

  private void whenAuthResponse(int statusCode, String body) throws Exception {
    @SuppressWarnings("unchecked")
    HttpResponse<String> authResponse = org.mockito.Mockito.mock(HttpResponse.class);
    when(authResponse.statusCode()).thenReturn(statusCode);
    when(authResponse.body()).thenReturn(body);
    when(httpClient.send(any(HttpRequest.class),
        org.mockito.ArgumentMatchers.<HttpResponse.BodyHandler<String>>any()))
        .thenReturn(authResponse);
  }
}
