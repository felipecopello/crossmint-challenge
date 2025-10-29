package io.crossmint.handler;

import io.crossmint.model.AstralObject;
import io.crossmint.model.Goal;
import io.crossmint.service.ApiServiceImpl;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Handler {
  private static final Logger LOGGER = LoggerFactory.getLogger(Handler.class);
  private final ApiServiceImpl apiServiceImpl;

  public Handler(ApiServiceImpl apiServiceImpl) {
    this.apiServiceImpl = apiServiceImpl;
  }

  public void handleTask() {
    LOGGER.info("Handling task");
    Goal goal = apiServiceImpl.retrieveGoal();
    List<AstralObject> gridPattern = apiServiceImpl.extractPattern(goal);
    for (AstralObject astralObject : gridPattern) {
      apiServiceImpl.createObject(astralObject);
    }
  }
}
