package com.comp301.a09akari.model;

import javafx.scene.control.Cell;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private PuzzleLibrary library;
  private Puzzle activepuzzle;
  private int indexactive;
  private List<ModelObserver> observers;
  private List<Lamp> lamps;
  private static final int[] ROW_OFFSETS = {-1, 1, 0, 0};
  private static final int[] COL_OFFSETS = {0, 0, -1, 1};

  public ModelImpl(PuzzleLibrary library) {
    // Your constructor code here
    if (library == null) {
      throw new IllegalArgumentException("can't be null!");
    }
    this.library = library;
    this.indexactive = 0;
    this.activepuzzle = library.getPuzzle(indexactive);
    this.observers = new ArrayList<>();
    this.lamps = new ArrayList<>();
    // Initially, the active puzzle should be set to the first one (index 0) in the puzzle library
  }

  /**
   * Adds a lamp if one doesn't already exist to the active puzzle in the cell at row r, column c.
   * Throws an IndexOutOfBoundsException if r or c is out of bounds, or an IllegalArgumentException
   * if the cell is not type CORRIDOR
   */
  public void addLamp(int r, int c) {
    if (activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("has to be type corridor");
    }
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    Lamp newLamp = new Lamp(r, c);
    if (!lamps.contains(newLamp)) {
      lamps.add(newLamp);
    }
    // needs to add a lamp at r,c
    notifyObservers();
  }

  /**
   * Removes a lamp if one exists from the active puzzle at the cell at row r, column c. Throws an
   * IndexOutOfBoundsException if r or c is out of bounds, or an IllegalArgumentException if the
   * cell is not type CORRIDOR
   */
  public void removeLamp(int r, int c) {
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    if (activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("has to be type corridor");
    }
    Lamp lampToRemove = new Lamp(r, c);
    lamps.remove(lampToRemove);
    // needs to remove a lamp at r,c
    notifyObservers();
  }

  /**
   * Returns true only if, in the active puzzle, the cell location row r, column c is currently
   * illuminated by a nearby lamp in the same column or row (and which is not blocked by a wall or
   * clue). If the cell itself contains a lamp, this method should also return true. Throws an
   * IndexOutOfBoundsException if r or c is out of bounds, or an IllegalArgumentException if the
   * cell is not type CORRIDOR
   */
  public boolean isLit(int r, int c) {
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Out of bounds");
    }
    if (activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor");
    }

    // Check row
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r) {
        // Check if the lamp can see the cell in the same row
        int startCol = Math.min(lamp.getCol(), c);
        int endCol = Math.max(lamp.getCol(), c);
        boolean isBlocked = false;
        for (int col = startCol + 1; col < endCol; col++) {
          if (activepuzzle.getCellType(r, col) != CellType.CORRIDOR) {
            isBlocked = true;
            break;
          }
        }
        if (!isBlocked) {
          return true;
        }
      }
    }

    // Check column
    for (Lamp lamp : lamps) {
      if (lamp.getCol() == c) {
        // Check if the lamp can see the cell in the same column
        int startRow = Math.min(lamp.getRow(), r);
        int endRow = Math.max(lamp.getRow(), r);
        boolean isBlocked = false;
        for (int row = startRow + 1; row < endRow; row++) {
          if (activepuzzle.getCellType(row, c) != CellType.CORRIDOR) {
            isBlocked = true;
            break;
          }
        }
        if (!isBlocked) {
          return true;
        }
      }
    }

    return false;
  }

  /**
   * Returns true only if, in the active puzzle, the cell at row r, column c contains a user-placed
   * lamp. Throws an IndexOutOfBoundsException if r or c is out of bounds, or an
   * IllegalArgumentException if the cell is not type CORRIDOR
   */
  public boolean isLamp(int r, int c) {
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    if (activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("has to be type corridor");
    }
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r && lamp.getCol() == c) {
        return true;
      }
    }
    return false;
  }

  /**
   * Returns true only if, in the active puzzle, the cell at row r, column c contains a user-placed
   * lamp that is in direct view of another lamp (i.e. the lamp is illegally placed). Throws an
   * IndexOutOfBoundsException if r or c is out of bounds, or an IllegalArgumentException if the
   * cell does not currently contain a lamp
   */
  public boolean isLampIllegal(int r, int c) {
    // Check if out of bounds or cell is not CORRIDOR
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("Out of bounds");
    }
    if (activepuzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException("Cell must be a corridor");
    }

    // Check if the cell contains a lamp
    if (!isLamp(r, c)) {
      throw new IllegalArgumentException("No lamp at the specified position");
    }

    // Check for another lamp in the same row or column
    for (Lamp lamp : lamps) {
      if (lamp.getRow() == r && lamp.getCol() != c) {
        // Lamp is in the same row but not in the same column
        if (isLampVisibleInRow(r, lamp.getCol(), c)) {
          return true; // Another lamp is visible in the row
        }
      }

      if (lamp.getCol() == c && lamp.getRow() != r) {
        // Lamp is in the same column but not in the same row
        if (isLampVisibleInColumn(c, lamp.getRow(), r)) {
          return true; // Another lamp is visible in the column
        }
      }
    }

    // If no other lamps are in direct view, return false
    return false;
  }

  // helper method to check if a lamp is visible in the same row
  private boolean isLampVisibleInRow(int row, int startCol, int endCol) {
    int start = Math.min(startCol, endCol) + 1;
    int end = Math.max(startCol, endCol);

    for (int col = start; col < end; col++) {
      if (activepuzzle.getCellType(row, col) != CellType.CORRIDOR) {
        return false; // The view is blocked
      }
    }
    return true; // The lamp is visible
  }

  // helper method to check if a lamp is visible in the same column
  private boolean isLampVisibleInColumn(int col, int startRow, int endRow) {
    int start = Math.min(startRow, endRow) + 1;
    int end = Math.max(startRow, endRow);

    for (int row = start; row < end; row++) {
      if (activepuzzle.getCellType(row, col) != CellType.CORRIDOR) {
        return false; // The view is blocked
      }
    }
    return true; // The lamp is visible
  }

  /** Getter method for the current active Puzzle instance */
  public Puzzle getActivePuzzle() {
    return activepuzzle;
  }

  /** Getter method for the active puzzle index */
  public int getActivePuzzleIndex() {
    return indexactive;
  }

  /**
   * Setter method for the current active Puzzle index. If the passed index is out of bounds, this
   * method should throw an IndexOutOfBoundsException
   */
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= library.size()) {
      throw new IndexOutOfBoundsException("index out of bounds");
    }
    indexactive = index;
    activepuzzle = library.getPuzzle(index);
    notifyObservers();
  }

  /** Getter method for the number of puzzles contained in the internal PuzzleLibrary */
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  /** Resets the active puzzle by removing all lamps which have been placed */
  public void resetPuzzle() {
    lamps.clear();
    notifyObservers();
  }

  /**
   * Returns true if the active puzzle is solved (i.e. every clue is satisfied and every corridor is
   * illuminated)
   */
  public boolean isSolved() {

    // Handle edge case for 1x1 puzzle (special case)
    if (activepuzzle.getHeight() == 1 && activepuzzle.getWidth() == 1) {
      // If the single cell is a corridor, it must be lit
      if (activepuzzle.getCellType(0, 0) == CellType.CORRIDOR) {
        if (!this.isLit(0, 0)) {
          return false; // The corridor is not lit
        }
      }

      // If the single cell is a clue, it must be satisfied
      if (activepuzzle.getCellType(0, 0) == CellType.CLUE) {
        return this.isClueSatisfied(0, 0); // The clue is not satisfied
      }

      return true; // If no issues, the 1x1 puzzle is considered solved
    }

    // For puzzles larger than 1x1
    for (int r = 0; r < activepuzzle.getHeight(); r++) {
      for (int c = 0; c < activepuzzle.getWidth(); c++) {
        // Check CORRIDOR cells
        if (activepuzzle.getCellType(r, c) == CellType.CORRIDOR) {
          // Check if the corridor is lit
          if (!this.isLit(r, c)) {
            return false; // Not all corridors are lit
          }
          // Check if there is a lamp at this position, and if it's illegal
          if (isLamp(r, c) && isLampIllegal(r, c)) {
            return false; // There's an illegal lamp
          }
        }
        // Check CLUE cells
        else if (activepuzzle.getCellType(r, c) == CellType.CLUE) {
          // Check if the clue is satisfied
          if (!this.isClueSatisfied(r, c)) {
            return false; // Not all clues are satisfied
          }
        }
      }
    }

    return true; // If all checks passed, the puzzle is solved
  }

  /**
   * Returns true if the clue located at row r, column c of the active puzzle is satisfied (i.e. has
   * exactly the number of lamps adjacent as is specified by the clue). Throws an
   * IndexOutOfBoundsException if r or c is out of bounds, or an IllegalArgumentException if the
   * cell is not type CLUE
   */
  public boolean isClueSatisfied(int r, int c) {
    if (r >= activepuzzle.getHeight() || c >= activepuzzle.getWidth()) {
      throw new IndexOutOfBoundsException("can't reach these values");
    }
    if (activepuzzle.getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException("has to be type Clue");
    }

    int neededCount = activepuzzle.getClue(r, c);
    int actualCount = 0;

    for (int i = 0; i < 4; i++) {
      int adjacentRow = r + ROW_OFFSETS[i];
      int adjacentCol = c + COL_OFFSETS[i];

      // Check if the adjacent position is within bounds
      if (adjacentRow >= 0
          && adjacentRow < activepuzzle.getHeight()
          && adjacentCol >= 0
          && adjacentCol < activepuzzle.getWidth()) {
        // Check if the adjacent cell contains a lamp
        if (activepuzzle.getCellType(adjacentRow, adjacentCol) == CellType.CORRIDOR) {
          // Check if the adjacent cell contains a lamp
          if (this.isLamp(adjacentRow, adjacentCol)) {
            actualCount++;
          }
        }
      }
    }

    // Return true if the actual count matches the needed count
    return actualCount == neededCount;

    // need to check r+1, r-1, c+1, c-1
  }

  /** Adds an observer to the model */
  public void addObserver(ModelObserver observer) {
    if (observer == null) {
      throw new IllegalArgumentException("can't be this observer");
    }
    observers.add(observer);
  }

  /** Removes an observer from the model */
  public void removeObserver(ModelObserver observer) {
    observers.remove(observer);
  }

  private void notifyObservers() {
    for (ModelObserver observer : observers) {
      observer.update(this);
    }
  }
}
