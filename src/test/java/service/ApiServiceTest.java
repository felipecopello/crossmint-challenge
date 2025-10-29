package service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import io.crossmint.client.ApiClient;
import io.crossmint.model.*;
import io.crossmint.service.ApiServiceImpl;
import io.crossmint.utils.Mapper;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

class ApiServiceTest {

  @Mock private ApiClient apiClient;

  @Mock private Mapper mapper;

  @InjectMocks private ApiServiceImpl apiServiceImpl;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testCreatePolyanet() {
    Polyanet polyanet = new Polyanet.Builder().build();
    String jsonBody = "{\"test\":\"data\"}";

    when(mapper.buildJsonBody(polyanet)).thenReturn(jsonBody);
    when(apiClient.postPolyanet(any(HttpEntity.class))).thenReturn(ResponseEntity.ok().build());

    apiServiceImpl.createObject(polyanet);

    verify(mapper).buildJsonBody(polyanet);
    verify(apiClient).postPolyanet(any(HttpEntity.class));
  }

  @Test
  void testCreateSoloon() {
    Soloon soloon = new Soloon.Builder().color("blue").build();
    String jsonBody = "{\"soloon\":\"data\"}";

    when(mapper.buildJsonBody(soloon)).thenReturn(jsonBody);
    when(apiClient.postSoloon(any(HttpEntity.class))).thenReturn(ResponseEntity.ok().build());

    apiServiceImpl.createObject(soloon);

    verify(mapper).buildJsonBody(soloon);
    verify(apiClient).postSoloon(any(HttpEntity.class));
  }

  @Test
  void testCreateCometh() {
    Cometh cometh = new Cometh.Builder().direction("up").build();
    String jsonBody = "{\"cometh\":\"data\"}";

    when(mapper.buildJsonBody(cometh)).thenReturn(jsonBody);
    when(apiClient.postCometh(any(HttpEntity.class))).thenReturn(ResponseEntity.ok().build());

    apiServiceImpl.createObject(cometh);

    verify(mapper).buildJsonBody(cometh);
    verify(apiClient).postCometh(any(HttpEntity.class));
  }

  @Test
  void testCreateObject_Unsupported() {
    AstralObject unsupported = new AstralObject() {}; // anonymous subclass

    IllegalArgumentException exception =
        assertThrows(
            IllegalArgumentException.class, () -> apiServiceImpl.createObject(unsupported));

    assertTrue(exception.getMessage().contains("Unsupported AstralObject type"));
  }

  @Test
  void testRetrieveGoal() {
    Goal goal = new Goal();
    ResponseEntity<Goal> responseEntity = ResponseEntity.ok(goal);

    when(apiClient.getGoal(any(HttpEntity.class))).thenReturn(responseEntity);

    Goal result = apiServiceImpl.retrieveGoal();

    assertEquals(goal, result);
    verify(apiClient).getGoal(any(HttpEntity.class));
  }

  @Test
  void testExtractPattern_NullGoal() {
    List<AstralObject> result = apiServiceImpl.extractPattern(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void testExtractPattern_EmptyGoal() {
    Goal goal = new Goal();
    goal.setGoal(new ArrayList<>());

    List<AstralObject> result = apiServiceImpl.extractPattern(goal);
    assertTrue(result.isEmpty());
  }

  @Test
  void testExtractPattern_WithObjects() {
    List<List<String>> grid =
        List.of(
            List.of("POLYANET", "RED_SOLOON", "UP_COMETH"),
            List.of("BLUE_SOLOON", "DOWN_COMETH", "INVALID_SOLOON", "LEFT_COMETH"));

    Goal goal = new Goal();
    goal.setGoal(grid);

    List<AstralObject> objects = apiServiceImpl.extractPattern(goal);

    assertEquals(6, objects.size());
    assertTrue(objects.stream().anyMatch(o -> o instanceof Polyanet));
    assertTrue(objects.stream().anyMatch(o -> o instanceof Soloon));
    assertTrue(objects.stream().anyMatch(o -> o instanceof Cometh));
  }
}
