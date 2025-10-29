package io.crossmint.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crossmint.config.Properties;
import io.crossmint.model.AstralObject;
import io.crossmint.model.Cometh;
import io.crossmint.model.Polyanet;
import io.crossmint.model.Soloon;
import java.util.Map;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Mapper {

  private final ObjectMapper objectMapper;
  private final Properties properties;

  public Mapper(ObjectMapper objectMapper, Properties properties) {
    this.objectMapper = objectMapper;
    this.properties = properties;
  }

  public String buildJsonBody(AstralObject astralObj) {

    return switch (astralObj) {
      case Polyanet p -> {
        try {
          yield objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", p.getRow(),
                  "column", p.getColumn()));
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
      case Soloon s -> {
        try {
          yield objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", s.getRow(),
                  "column", s.getColumn(),
                  "color", s.getColor()));
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
      case Cometh c -> {
        try {
          yield objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", c.getRow(),
                  "column", c.getColumn(),
                  "direction", c.getDirection()));
        } catch (JsonProcessingException e) {
          throw new RuntimeException(e);
        }
      }
      default ->
          throw new IllegalArgumentException(
              "Unsupported AstralObject type: " + astralObj.getClass().getSimpleName());
    };
  }
}
