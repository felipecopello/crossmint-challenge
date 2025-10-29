package io.crossmint.service;

import io.crossmint.client.ApiClient;
import io.crossmint.model.AstralObject;
import io.crossmint.model.Cometh;
import io.crossmint.model.Goal;
import io.crossmint.model.Polyanet;
import io.crossmint.model.Soloon;
import io.crossmint.utils.Mapper;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiService {
  private final ApiClient apiClient;
  private final Mapper mapper;
  private ResponseEntity<?> response = null;
  private HttpEntity<String> entity = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(ApiService.class);

  public ApiService(ApiClient apiClient, Mapper mapper) {
    this.apiClient = apiClient;
    this.mapper = mapper;
  }

  public void createObject(AstralObject astralObj) {
    HttpHeaders headers = getHeaders();

    String jsonBody;
    byte[] jsonBytes;

    switch (astralObj) {
      case Polyanet p -> {
        jsonBody = mapper.buildJsonBody(p);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        entity = new HttpEntity<>(jsonBody, headers);
        response = apiClient.postPolyanet(entity);
        LOGGER.info("Polyanet created");
      }
      case Soloon s -> {
        jsonBody = mapper.buildJsonBody(s);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        LOGGER.info("Soloon created");
      }
      case Cometh c -> {
        jsonBody = mapper.buildJsonBody(c);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        LOGGER.info("Cometh created");
      }
      default ->
          throw new IllegalArgumentException(
              "Unsupported AstralObject type: " + astralObj.getClass().getSimpleName());
    }
  }

  public Goal retrieveGoal() {
    entity = new HttpEntity<>(getHeaders());
    response = apiClient.getGoal(entity);

    LOGGER.info("GOAL RETRIEVED");
    return (Goal) response.getBody();
  }

  public List<AstralObject> extractPolyanets(Goal goal) {
    List<AstralObject> polyanets = new ArrayList<>();

    if (goal == null || goal.getGoal() == null) {
      return polyanets;
    }

    List<List<String>> grid = goal.getGoal();

    for (int row = 0; row < grid.size(); row++) {
      List<String> columns = grid.get(row);

      for (int col = 0; col < columns.size(); col++) {
        String cell = columns.get(col);

        if ("POLYANET".equalsIgnoreCase(cell)) {
          polyanets.add(
              new Polyanet.Builder().column(String.valueOf(col)).row(String.valueOf(row)).build());
        }
      }
    }
    LOGGER.info("POLYANETS EXTRACTED");
    return polyanets;
  }

  public HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
