package com.sflpro.notifier.externalclients.common.http.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * User: Mher Sargsyan
 * Company: SFL LLC
 * Date: 12/25/14
 * Time: 2:50 PM
 */
@Component
public class RestClientImpl implements RestClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestClientImpl.class);

    /* Dependencies */
    private final RestTemplate restTemplate;

    /* Constructors */
    public RestClientImpl(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        LOGGER.info("Initializing Http rest client.");
    }


    /* Getters */
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    /* Public method overrides */
    @Override
    public <T> T getForObject(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForObject(url, responseType, uriVariables);
    }

    @Override
    public <T> T getForObject(URI url, Class<T> responseType) {
        return restTemplate.getForObject(url, responseType);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Object... uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> getForEntity(URI url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }

    @Override
    public HttpHeaders headForHeaders(String url, Object... uriVariables) {
        return restTemplate.headForHeaders(url, uriVariables);
    }

    @Override
    public HttpHeaders headForHeaders(String url, Map<String, ?> uriVariables) {
        return restTemplate.headForHeaders(url, uriVariables);
    }

    @Override
    public HttpHeaders headForHeaders(URI url) {
        return restTemplate.headForHeaders(url);
    }

    @Override
    public URI postForLocation(String url, Object request, Object... uriVariables) {
        return restTemplate.postForLocation(url, request, uriVariables);
    }

    @Override
    public URI postForLocation(String url, Object request, Map<String, ?> uriVariables) {
        return restTemplate.postForLocation(url, request, uriVariables);
    }

    @Override
    public URI postForLocation(URI url, Object request) {
        return restTemplate.postForLocation(url, request);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.postForObject(url, request, responseType, uriVariables);
    }

    @Override
    public <T> T postForObject(URI url, Object request, Class<T> responseType) {
        return restTemplate.postForObject(url, request, responseType);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Object... uriVariables) {
        return restTemplate.postForEntity(url, request, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(String url, Object request, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.postForEntity(url, request, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> postForEntity(URI url, Object request, Class<T> responseType) {
        return restTemplate.postForEntity(url, request, responseType);
    }

    @Override
    public void put(String url, Object request, Object... uriVariables) {
        restTemplate.put(url, request, uriVariables);
    }

    @Override
    public void put(String url, Object request, Map<String, ?> uriVariables) {
        restTemplate.put(url, request, uriVariables);
    }

    @Override
    public void put(URI url, Object request) {
        restTemplate.put(url, request);
    }

    @Override
    public <T> T patchForObject(String s, Object o, Class<T> aClass, Object... objects) {
        return restTemplate.patchForObject(s, o, aClass, objects);
    }

    @Override
    public <T> T patchForObject(String s, Object o, Class<T> aClass, Map<String, ?> map) {
        return restTemplate.patchForObject(s, o, aClass, map);
    }

    @Override
    public <T> T patchForObject(URI uri, Object o, Class<T> aClass) {
        return restTemplate.patchForObject(uri, o, aClass);
    }

    @Override
    public void delete(String url, Object... uriVariables) {
        restTemplate.delete(url, uriVariables);
    }

    @Override
    public void delete(String url, Map<String, ?> uriVariables) {
        restTemplate.delete(url, uriVariables);
    }

    @Override
    public void delete(URI url) {
        restTemplate.delete(url);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(String url, Object... uriVariables) {
        return restTemplate.optionsForAllow(url, uriVariables);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(String url, Map<String, ?> uriVariables) {
        return restTemplate.optionsForAllow(url, uriVariables);
    }

    @Override
    public Set<HttpMethod> optionsForAllow(URI url) {
        return restTemplate.optionsForAllow(url);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Object... uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType, Map<String, ?> uriVariables) {
        return restTemplate.exchange(url, method, requestEntity, responseType, uriVariables);
    }

    @Override
    public <T> ResponseEntity<T> exchange(URI url, HttpMethod method, HttpEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(url, method, requestEntity, responseType);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, Class<T> responseType) {
        return restTemplate.exchange(requestEntity, responseType);
    }

    @Override
    public <T> ResponseEntity<T> exchange(RequestEntity<?> requestEntity, ParameterizedTypeReference<T> responseType) {
        return restTemplate.exchange(requestEntity, responseType);
    }

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Object... uriVariables) {
        return restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> T execute(String url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor, Map<String, ?> uriVariables) {
        return restTemplate.execute(url, method, requestCallback, responseExtractor, uriVariables);
    }

    @Override
    public <T> T execute(URI url, HttpMethod method, RequestCallback requestCallback, ResponseExtractor<T> responseExtractor) {
        return restTemplate.execute(url, method, requestCallback, responseExtractor);
    }
}
