package io.crossmint.handler;

import io.crossmint.client.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class HttpClientErrorHandler {

  private static final int MAX_RETRIES = 5;
  private static final long RETRY_DELAY_MS = 4000;
  private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientErrorHandler.class);

  public ResponseEntity<?> handleHttpError(
      HttpClientErrorException err, HttpEntity<String> entity, ApiClient apiClient) {

    if (err.getStatusCode().is4xxClientError()) {
      return handleClientError(err, entity, apiClient);
    }

    if (err.getStatusCode().is5xxServerError()) {
      LOGGER.info("Internal server error — check candidate ID.");
    }

    return new ResponseEntity<>(err.getStatusCode());
  }

  private ResponseEntity<?> handleClientError(
      HttpClientErrorException err, HttpEntity<String> entity, ApiClient apiClient) {

    if (err.getStatusCode().value() == 429) {
      LOGGER.info("Too many requests — will retry.");

      for (int i = 1; i <= MAX_RETRIES; i++) {
        try {
          System.out.printf("Retry #%d after %d ms...%n", i, RETRY_DELAY_MS);
          Thread.sleep(RETRY_DELAY_MS);

          ResponseEntity<?> response = apiClient.createPolyanet(entity);
          if (response.getStatusCode().is2xxSuccessful()) {
            LOGGER.info("Retry succeeded.");
            return response;
          }

        } catch (InterruptedException e) {
          Thread.currentThread().interrupt();
          LOGGER.info("Retry interrupted: " + e.getMessage());
          return ResponseEntity.internalServerError().build();
        } catch (HttpClientErrorException e) {
          LOGGER.info("Retry failed: " + e.getMessage());
        }
      }

      LOGGER.info("Max retry attempts reached for 429 error.");
    } else {
      LOGGER.info("Client error — check the request URL, body, or headers.");
    }

    return new ResponseEntity<>(err.getStatusCode());
  }
}
