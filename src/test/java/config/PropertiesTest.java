package config;

import static org.junit.jupiter.api.Assertions.*;

import io.crossmint.config.Properties;
import org.junit.jupiter.api.Test;

class PropertiesTest {

  @Test
  void testApiProperties() {
    Properties properties = new Properties();
    Properties.Api api = properties.getApi();

    // Set values
    api.setBaseUrl("http://base-url");
    api.setPolyanetUrl("http://polyanet");
    api.setSoloonUrl("http://soloon");
    api.setComethUrl("http://cometh");
    api.setGoalUrl("http://goal");
    api.setCandidateId("candidate-123");

    // Verify getters
    assertEquals("http://base-url", api.getBaseUrl());
    assertEquals("http://polyanet", api.getPolyanetUrl());
    assertEquals("http://soloon", api.getSoloonUrl());
    assertEquals("http://cometh", api.getComethUrl());
    assertEquals("http://goal", api.getGoalUrl());
    assertEquals("candidate-123", api.getCandidateId());
  }

  @Test
  void testRetryProperties() {
    Properties properties = new Properties();
    Properties.Retry retry = properties.getRetry();

    // Set values
    retry.setMaxAttempts(5);
    retry.setDelay(1000L);

    // Verify getters
    assertEquals(5, retry.getMaxAttempts());
    assertEquals(1000L, retry.getDelay());
  }

  @Test
  void testPropertiesDefaultInstances() {
    Properties properties = new Properties();

    // Verify that api and retry are not null
    assertNotNull(properties.getApi());
    assertNotNull(properties.getRetry());
  }
}
