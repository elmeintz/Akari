package com.comp301.a09akari.model;

public class PuzzleImpl implements Puzzle {
  private int[][] board;

  public PuzzleImpl(int[][] board) {
    // You should assume that the first dimension of board represents the
    // rows of the puzzle and the second dimension represents the columns.
    this.board = board;
  }

  /** Getter method for the width of the puzzle (i.e. the number of columns it has) */
  public int getWidth() {
    return board[0].length; // finds the length of the first row
  }

  /** Getter method for the height of the puzzle (i.e. the number of rows it has) */
  public int getHeight() {
    return board.length; // determines how many rows there are
  }

  /**
   * Getter method for the type of the cell located at row r, column c. Throws an IndexOutOfBounds
   * exception if r or c is out of bounds
   */
  public CellType getCellType(int r, int c) {
    if (r >= this.getHeight() || c >= this.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    if (board[r][c] == 5) {
      return CellType.WALL;
    } else if (board[r][c] == 6) {
      return CellType.CORRIDOR;
    } else {
      return CellType.CLUE;
    }
  }

  /**
   * Getter method for the clue number of the cell located at row r, column c. Throws an
   * IndexOutOfBounds exception if r or c is out of bounds, or an IllegalArgumentException if the
   * cell is not type CLUE
   */
  public int getClue(int r, int c) {
    if (r > this.getHeight() || c > this.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    if (this.getCellType(r, c) == CellType.CLUE) {

      if (board[r][c] == 0) {
        return 0;
      } else if (board[r][c] == 1) {
        return 1;
      } else if (board[r][c] == 2) {
        return 2;
      } else if (board[r][c] == 3) {
        return 3;
      } else {
        return 4;
      }

    } else {
      throw new IllegalArgumentException("has to be type CLUE");
    }
  }
}
