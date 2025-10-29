package io.crossmint.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AstralObject {
  private String column;
  private String row;

  public abstract static class Builder<T extends Builder<T, O>, O extends AstralObject> {
    protected final O instance;

    protected Builder(O instance) {
      this.instance = instance;
    }

    @SuppressWarnings("unchecked")
    public T column(String column) {
      instance.setColumn(column);
      return (T) this;
    }

    @SuppressWarnings("unchecked")
    public T row(String row) {
      instance.setRow(row);
      return (T) this;
    }

    public abstract O build();
  }
}
