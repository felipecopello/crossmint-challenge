package io.crossmint.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.crossmint.client.ApiClient;
import io.crossmint.models.AstralObject;
import io.crossmint.models.Cometh;
import io.crossmint.models.Polyanet;
import io.crossmint.models.Soloon;
import io.crossmint.utils.Mapper;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class Handler {
  private final Mapper mapper;
  private static final Logger log = LoggerFactory.getLogger(Handler.class);
  private final ApiClient apiClient;
  private ResponseEntity<?> response = null;
  private HttpEntity<String> entity = null;
  private final HttpClientErrorHandler httpErrorHandler;

  public Handler(Mapper mapper, ApiClient apiClient, HttpClientErrorHandler httpErrorHandler1) {
    this.mapper = mapper;
    this.apiClient = apiClient;
    this.httpErrorHandler = httpErrorHandler1;
  }

  public void handleCreation(AstralObject astralObj) throws JsonProcessingException {
    try {
      createObject(astralObj);
    } catch (HttpClientErrorException e) {
      log.error(e.getMessage());
      response = httpErrorHandler.handleHttpError(e, entity, apiClient);
      if ((Objects.nonNull(response))) {
        response.getStatusCode().value();
      }
    }
  }

  public void createObject(AstralObject astralObj) throws JsonProcessingException {

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    String jsonBody;
    byte[] jsonBytes;

    switch (astralObj) {
      case Polyanet p -> {
        jsonBody = mapper.buildJsonBody(p);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        entity = new HttpEntity<>(jsonBody, headers);
        response = apiClient.createPolyanet(entity);
        log.info("Polyanet created");
      }
      case Soloon s -> {
        jsonBody = mapper.buildJsonBody(s);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        log.info("Soloon created");
      }
      case Cometh c -> {
        jsonBody = mapper.buildJsonBody(c);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        log.info("Cometh created");
      }
      default ->
          throw new IllegalArgumentException(
              "Unsupported AstralObject type: " + astralObj.getClass().getSimpleName());
    }
  }
}
