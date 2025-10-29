package io.crossmint.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Cometh extends AstralObject {
  private String direction;

  public Cometh(String column, String row, String direction) {
    setColumn(column);
    setRow(row);
    this.direction = direction;
  }
}
