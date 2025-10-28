package io.crossmint.models;

public class Cometh extends AstralObject {
  private String direction;

  public Cometh(String column, String row, String direction) {
    setColumn(column);
    setRow(row);
    this.direction = direction;
  }

  public String getDirection() {
    return direction;
  }

  public void setDirection(String direction) {
    this.direction = direction;
  }
}
