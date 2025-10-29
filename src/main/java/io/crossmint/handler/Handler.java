package io.crossmint.handler;

import io.crossmint.model.AstralObject;
import io.crossmint.model.Goal;
import io.crossmint.service.ApiService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Handler {
  private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);

  private final ApiService apiService;

  public Handler(ApiService apiService) {
    this.apiService = apiService;
  }

  public void handleTaskOne() {
    LOGGER.info("Handling task 1");
    Goal goal = apiService.retrieveGoal();
    List<AstralObject> gridPattern = apiService.extractPolyanets(goal);
    for (AstralObject astralObject : gridPattern) {
      apiService.createObject(astralObject);
    }
  }
}
