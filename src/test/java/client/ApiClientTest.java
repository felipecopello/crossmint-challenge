package client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import io.crossmint.client.ApiClient;
import io.crossmint.config.Properties;
import io.crossmint.model.Goal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

class ApiClientTest {

  @Mock private RestTemplate restTemplate;

  @Mock private Properties properties;

  @Mock private Properties.Api apiProperties;

  @InjectMocks private ApiClient apiClient;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    when(properties.getApi()).thenReturn(apiProperties);
    when(apiProperties.getPolyanetUrl()).thenReturn("http://fakeurl.com/polyanet");
    when(apiProperties.getGoalUrl()).thenReturn("http://fakeurl.com/goal");
    when(apiProperties.getSoloonUrl()).thenReturn("http://fakeurl.com/soloon");
    when(apiProperties.getComethUrl()).thenReturn("http://fakeurl.com/cometh");
  }

  @Test
  void testPostPolyanet_NoRedirect() {
    HttpEntity<String> entity = new HttpEntity<>("payload");
    ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.OK);

    when(restTemplate.exchange("http://fakeurl.com/polyanet", HttpMethod.POST, entity, Void.class))
        .thenReturn(expectedResponse);

    ResponseEntity<?> response = apiClient.postPolyanet(entity);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(restTemplate)
        .exchange("http://fakeurl.com/polyanet", HttpMethod.POST, entity, Void.class);
  }

  @Test
  void testGetGoal_WithRedirect() {
    HttpEntity<String> entity = new HttpEntity<>("payload");

    HttpHeaders redirectHeaders = new HttpHeaders();
    redirectHeaders.set(HttpHeaders.LOCATION, "http://redirected.com/goal");
    ResponseEntity<Void> initialResponse = new ResponseEntity<>(redirectHeaders, HttpStatus.FOUND);

    ResponseEntity<Goal> redirectedResponse = new ResponseEntity<>(new Goal(), HttpStatus.OK);

    when(restTemplate.exchange("http://fakeurl.com/goal", HttpMethod.GET, entity, Void.class))
        .thenReturn(initialResponse);

    when(restTemplate.exchange("http://redirected.com/goal", HttpMethod.GET, entity, Goal.class))
        .thenReturn(redirectedResponse);

    ResponseEntity<?> response = apiClient.getGoal(entity);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(restTemplate).exchange("http://fakeurl.com/goal", HttpMethod.GET, entity, Void.class);
    verify(restTemplate).exchange("http://redirected.com/goal", HttpMethod.GET, entity, Goal.class);
  }

  @Test
  void testPostSoloon_NoRedirect() {
    HttpEntity<String> entity = new HttpEntity<>("payload");
    ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.CREATED);

    when(restTemplate.exchange("http://fakeurl.com/soloon", HttpMethod.POST, entity, Void.class))
        .thenReturn(expectedResponse);

    ResponseEntity<?> response = apiClient.postSoloon(entity);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    verify(restTemplate).exchange("http://fakeurl.com/soloon", HttpMethod.POST, entity, Void.class);
  }

  @Test
  void testFollowRedirectIfPresent_Redirects() {
    HttpEntity<String> entity = new HttpEntity<>("payload");

    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.LOCATION, "http://redirected.com");
    ResponseEntity<Void> initialResponse = new ResponseEntity<>(headers, HttpStatus.FOUND);
    ResponseEntity<Void> redirectedResponse = new ResponseEntity<>(HttpStatus.OK);

    when(restTemplate.exchange("http://redirected.com", HttpMethod.GET, entity, Void.class))
        .thenReturn(redirectedResponse);

    ResponseEntity<?> response =
        apiClient.followRedirectIfPresent(initialResponse, entity, HttpMethod.GET, Void.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(restTemplate).exchange("http://redirected.com", HttpMethod.GET, entity, Void.class);
  }
}
