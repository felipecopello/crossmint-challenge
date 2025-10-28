package io.crossmint.models;

public class Soloon extends AstralObject {
  private String color;

  public Soloon(String column, String row, String color) {
    setColumn(column);
    setRow(row);
    this.color = color;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}
