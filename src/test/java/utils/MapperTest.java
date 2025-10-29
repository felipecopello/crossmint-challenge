package utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.crossmint.config.Properties;
import io.crossmint.model.*;
import io.crossmint.utils.Mapper;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

class MapperTest {

  @Mock private ObjectMapper objectMapper;

  @Mock private Properties properties;

  @Mock private Properties.Api apiProperties;

  @InjectMocks private Mapper mapper;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(properties.getApi()).thenReturn(apiProperties);
    when(apiProperties.getCandidateId()).thenReturn("candidate-123");
  }

  @Test
  void testBuildJsonBody_Polyanet() throws JsonProcessingException {
    Polyanet polyanet = new Polyanet.Builder().row("1").column("2").build();
    String expectedJson = "{\"row\":\"1\",\"column\":\"2\",\"candidateId\":\"candidate-123\"}";

    when(objectMapper.writeValueAsString(
            Map.of(
                "candidateId", "candidate-123",
                "row", "1",
                "column", "2")))
        .thenReturn(expectedJson);

    String json = mapper.buildJsonBody(polyanet);

    assertEquals(expectedJson, json);
    verify(objectMapper).writeValueAsString(any(Map.class));
  }

  @Test
  void testBuildJsonBody_Soloon() throws JsonProcessingException {
    Soloon soloon = new Soloon.Builder().row("3").column("4").color("blue").build();
    String expectedJson =
        "{\"row\":\"3\",\"column\":\"4\",\"color\":\"blue\",\"candidateId\":\"candidate-123\"}";

    when(objectMapper.writeValueAsString(
            Map.of(
                "candidateId", "candidate-123",
                "row", "3",
                "column", "4",
                "color", "blue")))
        .thenReturn(expectedJson);

    String json = mapper.buildJsonBody(soloon);

    assertEquals(expectedJson, json);
    verify(objectMapper).writeValueAsString(any(Map.class));
  }

  @Test
  void testBuildJsonBody_Cometh() throws JsonProcessingException {
    Cometh cometh = new Cometh.Builder().row("5").column("6").direction("up").build();
    String expectedJson =
        "{\"row\":\"5\",\"column\":\"6\",\"direction\":\"up\",\"candidateId\":\"candidate-123\"}";

    when(objectMapper.writeValueAsString(
            Map.of(
                "candidateId", "candidate-123",
                "row", "5",
                "column", "6",
                "direction", "up")))
        .thenReturn(expectedJson);

    String json = mapper.buildJsonBody(cometh);

    assertEquals(expectedJson, json);
    verify(objectMapper).writeValueAsString(any(Map.class));
  }

  @Test
  void testBuildJsonBody_Unsupported() {
    AstralObject unsupported = new AstralObject() {}; // anonymous subclass

    IllegalArgumentException exception =
        assertThrows(IllegalArgumentException.class, () -> mapper.buildJsonBody(unsupported));

    assertTrue(exception.getMessage().contains("Unsupported AstralObject type"));
  }
}
