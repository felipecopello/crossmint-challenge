package io.crossmint.config;

import jakarta.validation.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "crossmint")
@Validated
public class Properties {

  private final Api api = new Api();

  public Api getApi() {
    return api;
  }

  public static class Api {

    @NotBlank private String baseUrl;

    @NotBlank private String candidateId;

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }

    public String getCandidateId() {
      return candidateId;
    }

    public void setCandidateId(String candidateId) {
      this.candidateId = candidateId;
    }
  }
}
