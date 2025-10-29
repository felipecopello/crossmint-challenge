package io.crossmint.service;

import io.crossmint.model.AstralObject;
import io.crossmint.model.Goal;
import java.util.List;

public interface ApiService {
  void createObject(AstralObject astralObj);

  Goal retrieveGoal();

  List<AstralObject> extractPattern(Goal goal);
}
