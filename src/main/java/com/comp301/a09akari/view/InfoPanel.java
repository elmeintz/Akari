package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import com.comp301.a09akari.model.*;

public class InfoPanel implements FXComponent {
  private ClassicMvcController controller;
  private Model model;

  public InfoPanel(ClassicMvcController controller, Model model) {
    this.controller = controller;
    this.model = model;
  }

  @Override
  public Parent render() {
    // holds buttons horizontally
    HBox hbox = new HBox(20);
    hbox.setPrefHeight(30);

    // styling for the hbox
    hbox.setStyle("-fx-padding: 10px;");

    int currentPuzzle = model.getActivePuzzleIndex() + 1;
    int totalPuzzles = model.getPuzzleLibrarySize();
    Label puzzleIndexLabel = new Label("Puzzle " + currentPuzzle + " of " + totalPuzzles);
    puzzleIndexLabel.setStyle("-fx-font-size: 13px;");
    hbox.getChildren().add(puzzleIndexLabel);

    if (model.isSolved()) {
      Label solvedMessage = new Label("Status: Solved");
      solvedMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: green;");
      hbox.getChildren().add(solvedMessage);
    } else {
      Label solvedMessage = new Label("Status: Unsolved");
      solvedMessage.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
      hbox.getChildren().add(solvedMessage);
    }

    return hbox; // return the HBox as the parent container
  }
}
