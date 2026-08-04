package br.ufpb.dsc.nexushub.controller.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RateLimitingFilterTest {

    private RateLimitingFilter filter;
    private Instant now;

    @BeforeEach
    void setUp() {
        now = Instant.now();
        Clock clock = Clock.fixed(now, ZoneId.systemDefault());
        filter = new RateLimitingFilter(3, Duration.ofMinutes(1), clock);
    }

    @Test
    void allowsRequestsUnderLimit() {
        assertTrue(filter.isAllowed("192.168.1.1"));
        assertTrue(filter.isAllowed("192.168.1.1"));
        assertTrue(filter.isAllowed("192.168.1.1"));
        assertFalse(filter.isAllowed("192.168.1.1"));
    }

    @Test
    void blocksProtectedEndpointWhenLimitExceeded() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(request.getRequestURI()).thenReturn("/api/usuarios/login");
        when(request.getRemoteAddr()).thenReturn("10.0.0.1");
        when(response.getWriter()).thenReturn(printWriter);

        filter.doFilterInternal(request, response, chain);
        filter.doFilterInternal(request, response, chain);
        filter.doFilterInternal(request, response, chain);

        // 4th request exceeds limit of 3
        filter.doFilterInternal(request, response, chain);

        verify(response).setStatus(429);
        assertTrue(stringWriter.toString().contains("Too Many Requests"));
    }

    @Test
    void ignoresUnprotectedEndpoints() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        when(request.getRequestURI()).thenReturn("/api/projetos");

        filter.doFilterInternal(request, response, chain);
        verify(chain).doFilter(request, response);
        verifyNoInteractions(response);
    }
}
