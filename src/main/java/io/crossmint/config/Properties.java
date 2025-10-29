package io.crossmint.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "crossmint")
@Validated
public class Properties {

  private final Api api = new Api();
  private final Retry retry = new Retry();

  @Getter
  @Setter
  public static class Api {
    private String baseUrl;
    private String polyanetUrl;
    private String soloonUrl;
    private String comethUrl;
    private String goalUrl;
    private String candidateId;
  }

  @Getter
  @Setter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class Retry {
    private int maxAttempts;
    private long delay;
  }
}
