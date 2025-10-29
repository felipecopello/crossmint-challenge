package io.crossmint.model;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Polyanet extends AstralObject {
  public Polyanet(String column, String row) {
    setColumn(column);
    setRow(row);
  }

  public static class Builder extends AstralObject.Builder<Builder, Polyanet> {
    public Builder() {
      super(new Polyanet());
    }

    @Override
    public Polyanet build() {
      return instance;
    }
  }
}
