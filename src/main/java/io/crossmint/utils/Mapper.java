package io.crossmint.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crossmint.config.Properties;
import io.crossmint.models.AstralObject;
import io.crossmint.models.Cometh;
import io.crossmint.models.Polyanet;
import io.crossmint.models.Soloon;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

  private final ObjectMapper objectMapper;
  private final Properties properties;

  public Mapper(ObjectMapper objectMapper, Properties properties) {
    this.objectMapper = objectMapper;
    this.properties = properties;
  }

  public String buildJsonBody(AstralObject astralObj) throws JsonProcessingException {

    return switch (astralObj) {
      case Polyanet p ->
          objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", p.getRow(),
                  "column", p.getColumn()));
      case Soloon s ->
          objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", s.getRow(),
                  "column", s.getColumn(),
                  "color", s.getColor()));
      case Cometh c ->
          objectMapper.writeValueAsString(
              Map.of(
                  "candidateId", properties.getApi().getCandidateId(),
                  "row", c.getRow(),
                  "column", c.getColumn(),
                  "direction", c.getDirection()));
      default ->
          throw new IllegalArgumentException(
              "Unsupported AstralObject type: " + astralObj.getClass().getSimpleName());
    };
  }
}
