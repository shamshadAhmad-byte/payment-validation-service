package com.example.paymentvalidationservice.filter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.paymentvalidationservice.dto.PaymentRequest;
import com.example.paymentvalidationservice.service.interfaces.HmacSHA256;
import com.example.paymentvalidationservice.utils.JsonUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class HmacFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HmacFilter.class);
    private static final String SECRET = "your_secret_key";

    private final HmacSHA256 hmacSHA256;
    private final ObjectMapper objectMapper;

    public HmacFilter(HmacSHA256 hmacSHA256, ObjectMapper objectMapper) {
        this.hmacSHA256 = hmacSHA256;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.info(">> HmacFilter: Request URI: {}", request.getRequestURI());
        String hmacSignature = request.getHeader("X-Payment-Signature");
        HttpServletRequest requestToContinue = request;
        boolean isVerified = true;

        if (HttpMethod.POST.matches(request.getMethod())) {
            if (hmacSignature == null) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing payment signature");
                return;
            }

            CachedBodyRequest cachedRequest = new CachedBodyRequest(request);
            requestToContinue = cachedRequest;
            PaymentRequest paymentRequest = objectMapper.readValue(cachedRequest.getCachedBody(), PaymentRequest.class);
            try {
                String data = JsonUtil.compactJson(paymentRequest);
                log.info(">> HmacFilter: Data to be signed: {}", data);
                String generatedSignature = hmacSHA256.generateHmac(data, SECRET);
                log.info(">> HmacFilter: Generated signature: {}", generatedSignature);
                isVerified = hmacSHA256.isVerified(generatedSignature, hmacSignature);
            } catch (GeneralSecurityException ex) {
                throw new ServletException("Unable to validate payment signature", ex);
            }
        }

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                isVerified ? "verified" : "unverified",
                null,
                Collections.emptyList()
        );
        request.setAttribute("authentication", authentication);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

        if (isVerified) {
            log.info(">> HmacFilter: Signature verified, proceeding further");
            filterChain.doFilter(requestToContinue, response);
        } else {
            log.warn("HMAC signature verification failed");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

    }

    private static final class CachedBodyRequest extends HttpServletRequestWrapper {

        private final byte[] cachedBody;

        private CachedBodyRequest(HttpServletRequest request) throws IOException {
            super(request);
            cachedBody = StreamUtils.copyToByteArray(request.getInputStream());
        }

        private byte[] getCachedBody() {
            return cachedBody;
        }

        @Override
        public ServletInputStream getInputStream() {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(cachedBody);
            return new ServletInputStream() {
                @Override
                public int read() {
                    return inputStream.read();
                }

                @Override
                public boolean isFinished() {
                    return inputStream.available() == 0;
                }

                @Override
                public boolean isReady() {
                    return true;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                    // Synchronous request processing does not require a listener.
                }
            };
        }

        @Override
        public java.io.BufferedReader getReader() throws IOException {
            return new java.io.BufferedReader(new java.io.InputStreamReader(getInputStream(),
                    getCharacterEncoding() == null ? java.nio.charset.StandardCharsets.UTF_8
                            : java.nio.charset.Charset.forName(getCharacterEncoding())));
        }
    }

}
