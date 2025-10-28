package io.crossmint.client;

import io.crossmint.config.Properties;
import io.crossmint.models.Cometh;
import io.crossmint.models.Soloon;
import io.crossmint.service.ApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class ApiClient implements ApiService {

  private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);
  private final RestTemplate restTemplate;

  private final Properties properties;
  private String path;

  public ApiClient(RestTemplate restTemplate, Properties properties) {
    this.restTemplate = restTemplate;
    this.properties = properties;
  }

  @Override
  public ResponseEntity<?> createPolyanet(HttpEntity<String> entity) {
    path = properties.getApi().getBaseUrl() + "polyanets";
    ResponseEntity<?> response =
        restTemplate.exchange(path, HttpMethod.POST, entity, Void.class, String.class);
    LOGGER.info("INIT STATUS CODE: " + response.getStatusCode());

    if (response.getStatusCode().is3xxRedirection()) {
      followRedirect(response, entity, HttpMethod.POST);
    }
    return response;
  }

  @Override
  public void deletePolyanet(int row, int column) {}

  @Override
  public Soloon createSoloon(int row, int column, String color) {
    path = properties.getApi().getBaseUrl() + "soloons";
    return null;
  }

  @Override
  public void deleteSoloon(int row, int column) {}

  @Override
  public Cometh createCometh(int row, int column, String direction) {
    return null;
  }

  @Override
  public void deleteCometh(int row, int column) {}

  public void followRedirect(
      ResponseEntity<?> response, HttpEntity<String> entity, HttpMethod method) {
    HttpHeaders responseHeaders = response.getHeaders();
    String url = responseHeaders.getFirst(HttpHeaders.LOCATION);

    if (url != null) {
      response = restTemplate.exchange(url, method, entity, Void.class, String.class);
      LOGGER.info("RETRIED STATUS CODE: " + response.getStatusCode());
    }
  }
}
