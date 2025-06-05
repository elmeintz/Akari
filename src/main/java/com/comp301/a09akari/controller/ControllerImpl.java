package com.comp301.a09akari.controller;

import com.comp301.a09akari.model.Model;
import com.comp301.a09akari.view.View;

public class ControllerImpl implements ClassicMvcController {
  private Model model;

  public ControllerImpl(Model model) {
    this.model = model;
  }

  /** Handles the click action to go to the next puzzle */
  public void clickNextPuzzle() {
    int currentIndex = model.getActivePuzzleIndex();
    int lastindex = model.getPuzzleLibrarySize() - 1;

    // If we're not on the last puzzle, increment the index
    if (currentIndex < lastindex) {
      model.setActivePuzzleIndex(currentIndex + 1);
    }
    if (currentIndex == lastindex) {
      model.setActivePuzzleIndex(0);
    }
  }

  /** Handles the click action to go to the previous puzzle */
  public void clickPrevPuzzle() {
    int currentIndex = model.getActivePuzzleIndex();
    int lastindex = model.getPuzzleLibrarySize() - 1;

    // If we're not on the first puzzle, decrement the index
    if (currentIndex > 0) {
      model.setActivePuzzleIndex(currentIndex - 1);
    }
    if (currentIndex == 0) {
      model.setActivePuzzleIndex(lastindex);
    }
  }

  /** Handles the click action to go to a random puzzle */
  public void clickRandPuzzle() {
    int totalPuzzles = model.getPuzzleLibrarySize();
    if (totalPuzzles > 0) {
      int randomIndex =
          (int)
              (Math.random() * totalPuzzles); // Random index within the range of available puzzles
      model.setActivePuzzleIndex(randomIndex);
    }
  }

  /** Handles the click action to reset the currently active puzzle */
  public void clickResetPuzzle() {
    model.resetPuzzle();
  }

  /** Handles the click event on the cell at row r, column c */
  public void clickCell(int r, int c) {
    try {
      if (model.isLamp(r, c)) {
        // If there's a lamp already placed, remove it
        model.removeLamp(r, c);
      } else {
        // Otherwise, place a new lamp
        model.addLamp(r, c);
      }
    } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
      // Handle invalid clicks (e.g., on walls or out of bounds)
      toString("Can't click here (" + r + ", " + c + "): " + e.getMessage());
    }
  }

  private void toString(String message) {
    System.out.println(
        message); // In a real app, replace with actual UI feedback (e.g., dialog box)
  }
}
