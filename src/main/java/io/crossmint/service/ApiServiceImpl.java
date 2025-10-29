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
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ApiServiceImpl implements ApiService {
  private final ApiClient apiClient;
  private final Mapper mapper;
  private HttpEntity<String> entity = null;
  private static final Logger LOGGER = LoggerFactory.getLogger(ApiServiceImpl.class);

  public ApiServiceImpl(ApiClient apiClient, Mapper mapper) {
    this.apiClient = apiClient;
    this.mapper = mapper;
  }

  @Override
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
        apiClient.postPolyanet(entity);
        LOGGER.info("Polyanet created");
      }
      case Soloon s -> {
        jsonBody = mapper.buildJsonBody(s);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        entity = new HttpEntity<>(jsonBody, headers);
        apiClient.postSoloon(entity);
        LOGGER.info("Soloon created");
      }
      case Cometh c -> {
        jsonBody = mapper.buildJsonBody(c);
        jsonBytes = jsonBody.getBytes();
        headers.set("Content-Length", String.valueOf(jsonBytes.length));
        entity = new HttpEntity<>(jsonBody, headers);
        apiClient.postCometh(entity);
        LOGGER.info("Cometh created");
      }
      default ->
          throw new IllegalArgumentException(
              "Unsupported AstralObject type: " + astralObj.getClass().getSimpleName());
    }
  }

  @Override
  public Goal retrieveGoal() {
    entity = new HttpEntity<>(getHeaders());
    ResponseEntity<?> response = apiClient.getGoal(entity);

    LOGGER.info("GOAL RETRIEVED");
    return (Goal) response.getBody();
  }

  @Override
  public List<AstralObject> extractPattern(Goal goal) {
    List<AstralObject> astralObjects = new ArrayList<>();

    if (goal == null || goal.getGoal() == null) {
      return astralObjects;
    }

    List<List<String>> grid = goal.getGoal();

    Set<String> validColors = Set.of("blue", "red", "purple", "white");
    Set<String> validDirections = Set.of("up", "down", "left", "right");

    for (int row = 0; row < grid.size(); row++) {
      List<String> columns = grid.get(row);

      for (int col = 0; col < columns.size(); col++) {
        String cell = columns.get(col);
        if (cell == null || cell.isBlank()) continue;

        String normalized = cell.trim().toUpperCase();

        if ("POLYANET".equals(normalized)) {
          astralObjects.add(
              new Polyanet.Builder().column(String.valueOf(col)).row(String.valueOf(row)).build());
        } else if (normalized.endsWith("_SOLOON")) {
          String color = normalized.replace("_SOLOON", "").toLowerCase();

          if (validColors.contains(color)) {
            astralObjects.add(
                new Soloon.Builder()
                    .column(String.valueOf(col))
                    .row(String.valueOf(row))
                    .color(color)
                    .build());
          } else {
            LOGGER.warn("Invalid color '{}' at row {}, col {}", color, row, col);
          }
        } else if (normalized.endsWith("_COMETH")) {
          String direction = normalized.replace("_COMETH", "").toLowerCase();

          if (validDirections.contains(direction)) {
            astralObjects.add(
                new Cometh.Builder()
                    .column(String.valueOf(col))
                    .row(String.valueOf(row))
                    .direction(direction)
                    .build());
          } else {
            LOGGER.warn("Invalid direction '{}' at row {}, col {}", direction, row, col);
          }
        }
      }
    }

    LOGGER.info("ASTRAL OBJECTS EXTRACTED — total: {}", astralObjects.size());
    return astralObjects;
  }

  public HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return headers;
  }
}
