package io.pivotal.workshop.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.actuate.trace.TraceProperties;
import org.springframework.boot.actuate.trace.TraceRepository;
import org.springframework.boot.actuate.trace.WebRequestTraceFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Payload;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
public class RequestResponseTraceFilter extends WebRequestTraceFilter {
    private static final Logger log = LoggerFactory.getLogger(RequestResponseTraceFilter.class);


    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("application/*"),
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML
    );

    private final String[] excludedEndpoints = new String[]{"/css/**", "/js/**", "/trace"};
    
    public RequestResponseTraceFilter(TraceRepository repository, TraceProperties properties) {
        super(repository, properties);

    }

    @Override
    protected boolean shouldNotFilter(final HttpServletRequest request) throws ServletException {
        return Arrays.stream(excludedEndpoints)
                .anyMatch(e -> new AntPathMatcher().match(e, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
            cleanMDCfields();
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        }
        finally {
            afterRequest(request, response);
            response.copyBodyToResponse();


        }
    }


    protected void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
            logRequestHeader(request);
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
            logRequestBody(request);
            logResponse(response);
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request) {
        String queryString = request.getQueryString();
        MDC.put("HttpMethod", request.getMethod());
        MDC.put("Address", request.getRequestURI());
        /*Collections.list(request.getHeaderNames()).forEach(headerName ->
                Collections.list(request.getHeaders(headerName)).forEach(headerValue ->
                        log.info(" {}: {}", headerName, headerValue))); */
    }

    private static void logRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content.length > 0) {
            MDC.put("PayloadType", "REQUEST");
            logContent(content, request.getContentType(), request.getCharacterEncoding());
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response) {
        String status =String.valueOf(response.getStatus());
        MDC.put("HttpCode", status );
        MDC.put("PayloadType", "RESPONSE");
       // log.info(" {} {}",  status, HttpStatus.valueOf(status).getReasonPhrase());
        response.getHeaderNames().forEach(headerName ->
                response.getHeaders(headerName).forEach(headerValue ->
                        log.info(" {}: {}", headerName, headerValue)));
        byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(content, response.getContentType(), response.getCharacterEncoding());
        }
    }

    private static void logContent(byte[] content, String contentType, String contentEncoding) {
        MediaType mediaType = MediaType.valueOf(contentType);
        boolean visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        if (visible) {
            try {
                 String contentString = new String(content, contentEncoding);
                MDC.put("Payload",contentString.replaceAll("\r\n|\r|\n", ""));
                log.info("{} {}", "Payload", contentString);
              //  Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> log.info("{}", line));

            } catch (UnsupportedEncodingException e) {
                log.debug("[{} bytes content]", content.length);
            }
        } else {
            log.debug(" [{} bytes content]",  content.length);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        if (request instanceof ContentCachingRequestWrapper) {
            return (ContentCachingRequestWrapper) request;
        } else {
            return new ContentCachingRequestWrapper(request);
        }
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        if (response instanceof ContentCachingResponseWrapper) {
            return (ContentCachingResponseWrapper) response;
        } else {
            return new ContentCachingResponseWrapper(response);
        }
    }

    private static void cleanMDCfields() {
        MDC.remove("Payload");
        MDC.remove("PayloadType");
        MDC.remove("PayloadFormat");
        MDC.remove("ResponseTimeInMilliseconds");


    }

}