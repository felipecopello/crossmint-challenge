package io.crossmint.client;

import io.crossmint.config.Properties;
import io.crossmint.model.Goal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);
  private final RestTemplate restTemplate;
  private final Properties properties;

  public ApiClient(RestTemplate restTemplate, Properties properties) {
    this.restTemplate = restTemplate;
    this.properties = properties;
  }

  @Retryable(
      retryFor = HttpClientErrorException.class,
      maxAttemptsExpression = "#{@properties.retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "#{@properties.retry.delay}"))
  public ResponseEntity<?> postPolyanet(HttpEntity<String> entity) {
    LOGGER.info("POST request to /polyanets");
    ResponseEntity<?> response =
        restTemplate.exchange(
            properties.getApi().getPolyanetUrl(), HttpMethod.POST, entity, Void.class);

    followRedirectIfPresent(response, entity, HttpMethod.POST, Void.class);
    return response;
  }

  @Retryable(
      retryFor = HttpClientErrorException.class,
      maxAttemptsExpression = "#{@properties.retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "#{@properties.retry.delay}"))
  public ResponseEntity<?> getGoal(HttpEntity<String> entity) {
    LOGGER.info("GET request to /goal");
    ResponseEntity<?> response =
        restTemplate.exchange(properties.getApi().getGoalUrl(), HttpMethod.GET, entity, Void.class);

    return followRedirectIfPresent(response, entity, HttpMethod.GET, Goal.class);
  }

  @Retryable(
      retryFor = HttpClientErrorException.class,
      maxAttemptsExpression = "#{@properties.retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "#{@properties.retry.delay}"))
  public ResponseEntity<?> postSoloon(HttpEntity<String> entity) {
    LOGGER.info("POST request to /soloons");
    ResponseEntity<?> response =
        restTemplate.exchange(
            properties.getApi().getSoloonUrl(), HttpMethod.POST, entity, Void.class);

    followRedirectIfPresent(response, entity, HttpMethod.POST, Void.class);
    return response;
  }

  @Retryable(
      retryFor = HttpClientErrorException.class,
      maxAttemptsExpression = "#{@properties.retry.maxAttempts}",
      backoff = @Backoff(delayExpression = "#{@properties.retry.delay}"))
  public ResponseEntity<?> postCometh(HttpEntity<String> entity) {
    LOGGER.info("POST request to /soloons");
    ResponseEntity<?> response =
        restTemplate.exchange(
            properties.getApi().getComethUrl(), HttpMethod.POST, entity, Void.class);

    followRedirectIfPresent(response, entity, HttpMethod.POST, Void.class);
    return null;
  }

  public ResponseEntity<?> followRedirectIfPresent(
      ResponseEntity<?> response,
      HttpEntity<String> entity,
      HttpMethod method,
      Class<?> responseType) {

    if (response.getStatusCode().is3xxRedirection()) {
      HttpHeaders responseHeaders = response.getHeaders();
      String url = responseHeaders.getFirst(HttpHeaders.LOCATION);

      if (url != null) {
        response = restTemplate.exchange(url, method, entity, responseType);
        LOGGER.info("REDIRECTED STATUS CODE: " + response.getStatusCode());
      }
    }
    return response;
  }
}
