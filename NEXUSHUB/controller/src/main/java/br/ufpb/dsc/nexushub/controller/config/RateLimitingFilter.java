package br.ufpb.dsc.nexushub.controller.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitingFilter extends OncePerRequestFilter {

    private static final Set<String> PROTECTED_ENDPOINTS = Set.of(
            "/api/usuarios/login",
            "/api/usuarios/cadastro",
            "/api/usuarios/esqueci-senha",
            "/api/lgpd/solicitacoes",
            "/api/lgpd/meus-dados"
    );

    private final int maxRequests;
    private final Duration windowDuration;
    private final Clock clock;
    private final Map<String, ArrayDeque<Instant>> requestCounts = new ConcurrentHashMap<>();

    public RateLimitingFilter() {
        this(10, Duration.ofMinutes(1), Clock.systemUTC());
    }

    public RateLimitingFilter(int maxRequests, Duration windowDuration, Clock clock) {
        this.maxRequests = maxRequests;
        this.windowDuration = windowDuration;
        this.clock = clock;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (isProtectedEndpoint(path)) {
            String clientIp = getClientIp(request);
            if (!isAllowed(clientIp)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write("""
                        {"status":429,"error":"Too Many Requests","message":"Muitas requisicoes efetuadas. Tente novamente em 1 minuto."}
                        """);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isProtectedEndpoint(String path) {
        return PROTECTED_ENDPOINTS.stream().anyMatch(path::equalsIgnoreCase);
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr() != null ? request.getRemoteAddr() : "unknown";
    }

    public boolean isAllowed(String key) {
        Instant now = clock.instant();
        Instant cutoff = now.minus(windowDuration);
        ArrayDeque<Instant> timestamps = requestCounts.computeIfAbsent(key, ignored -> new ArrayDeque<>());

        synchronized (timestamps) {
            while (!timestamps.isEmpty() && timestamps.peekFirst().isBefore(cutoff)) {
                timestamps.removeFirst();
            }
            if (timestamps.size() >= maxRequests) {
                return false;
            }
            timestamps.addLast(now);
            return true;
        }
    }
}
