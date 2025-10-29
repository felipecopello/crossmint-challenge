package io.crossmint.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Goal {

  @JsonProperty("goal")
  private List<List<String>> goal;
}
