package io.crossmint.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Soloon extends AstralObject {
  private String color;

  public Soloon(String column, String row, String color) {
    setColumn(column);
    setRow(row);
    this.color = color;
  }

  public static class Builder extends AstralObject.Builder<Builder, Soloon> {
    public Builder() {
      super(new Soloon());
    }

    public Builder color(String color) {
      instance.setColor(color);
      return this;
    }

    @Override
    public Soloon build() {
      return instance;
    }
  }
}
