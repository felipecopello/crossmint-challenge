package io.crossmint.models;

public abstract class AstralObject {
  private String column;
  private String row;

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public String getRow() {
    return row;
  }

  public void setRow(String row) {
    this.row = row;
  }
}
