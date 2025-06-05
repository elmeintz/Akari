package com.comp301.a09akari.model;

import java.util.Objects;

public class Lamp {
  private int row;
  private int col;

  public Lamp(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    Lamp lamp = (Lamp) obj;
    return row == lamp.row && col == lamp.col;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, col);
  }
}
