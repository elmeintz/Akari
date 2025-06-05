package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.*;
import com.comp301.a09akari.model.*;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

public class PuzzleView implements FXComponent {
  private final Model model;
  private final ClassicMvcController controller;

  public PuzzleView(Model model, ClassicMvcController controller) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    GridPane grid = new GridPane();
    int height = model.getActivePuzzle().getHeight();
    int width = model.getActivePuzzle().getWidth();

    // iterate over each row and column in the puzzle
    for (int row = 0; row < height; row++) {
      for (int col = 0; col < width; col++) {
        // create a button for the cell
        Button cellButton = new Button();
        cellButton.setMinSize(40, 40); // set size for the button

        // get the cell type (whether it’s a clue, wall, or corridor)
        CellType cellType = model.getActivePuzzle().getCellType(row, col);

        // update the button appearance based on the cell type
        updateCellButton(cellButton, cellType, row, col);

        // attach click handler for adding/removing lamps
        attachClickHandlerToCell(cellButton, row, col);

        // add the button to the grid layout
        grid.add(cellButton, col, row);
      }
    }
    return grid;
  }

  // update buttons appearance based on celltype
  private void updateCellButton(Button button, CellType cellType, int row, int col) {
    // ensure that clue cells have a distinct background color (e.g., light blue) and display their
    // clue numbers.
    if (cellType == CellType.CLUE) {
      button.setStyle(
          "-fx-background-color: lightgray; -fx-border-color: black; -fx-border-width: 0.5;");
      button.setText(String.valueOf(model.getActivePuzzle().getClue(row, col)));
      // test for A "satisfied" clue cell (i.e. one which has the correct number of
      // lamps placed adjacent to it) is visually distinct from an "unsatisfied" clue cell.
      if (model.isClueSatisfied(row, col)) {
        button.setStyle(
            "-fx-background-color: green; -fx-border-color: black; -fx-border-width: 0.5;");
      }
    } else if (cellType == CellType.WALL) {
      // all wall cells are gray and empty with text
      button.setStyle(
          "-fx-background-color: gray; -fx-border-color: black; -fx-border-width: 0.5;");
      button.setText("");
    } else if (cellType == CellType.CORRIDOR) {
      // corridors can either be lit or unlit, so change the background color accordingly
      if (model.isLit(row, col)) {
        // if a corridor cell is Lit, color it yellow and white if not
        button.setStyle(
            "-fx-background-color: yellow; -fx-border-color: black; -fx-border-width: 0.5;");
      } else {
        button.setStyle(
            "-fx-background-color: white; -fx-border-color: black; -fx-border-width: 0.5;");
      }
      button.setText("");

      if (model.isLamp(row, col)) {
        ImageView lampImage = new ImageView();
        try {
          lampImage.setImage(new Image(getClass().getResource("/light-bulb.png").toExternalForm()));
        } catch (Exception e) {
          System.out.println("Lamp image not found!");
        }
        lampImage.setFitHeight(40); // set the image size (same as grid size)
        lampImage.setFitWidth(40);

        button.setStyle("-fx-padding: 0;"); // Removes any internal padding
        button.setContentDisplay(
            javafx.scene.control.ContentDisplay.CENTER); // center lamp in button

        if (model.isLampIllegal(row, col)) {
          lampImage.setStyle("-fx-effect: innershadow(gaussian, red, 10, 0, 0, 0);");
        }

        button.setGraphic(lampImage); // add the lamp image to the button
      }
    }
  }

  private void attachClickHandlerToCell(Button button, int row, int col) {
    button.setOnMouseClicked(
        (MouseEvent event) -> {
          if (model.isLamp(row, col)) {
            removeLamp(row, col); // remove lamp if it exists
          } else {
            addLamp(row, col); // add lamp if none exists
          }
        });
  }

  private void addLamp(int row, int col) {
    if (model.getActivePuzzle().getCellType(row, col) == CellType.CORRIDOR
        && !model.isLamp(row, col)) {
      controller.clickCell(row, col); // call on the controller
    }
  }

  private void removeLamp(int row, int col) {
    if (model.getActivePuzzle().getCellType(row, col) == CellType.CORRIDOR
        && model.isLamp(row, col)) {
      controller.clickCell(row, col); // call on the controller
    }
  }
}
