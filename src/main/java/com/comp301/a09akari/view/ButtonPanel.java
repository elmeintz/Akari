package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ClassicMvcController;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

public class ButtonPanel implements FXComponent {
  private ClassicMvcController controller;

  public ButtonPanel(ClassicMvcController controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    // holds buttons horizontally
    HBox hbox = new HBox(10);

    // styling for the hbox
    hbox.setStyle("-fx-padding: 10px;");

    // create Reset button
    Button resetButton = new Button("Reset");
    resetButton.setOnAction(
        (ActionEvent e) -> {
          controller.clickResetPuzzle(); // call to controller
        });

    // create Next button
    Button nextButton = new Button("Next Puzzle");
    nextButton.setOnAction(
        (ActionEvent e) -> {
          controller.clickResetPuzzle();
          controller.clickNextPuzzle(); // call to controller
        });

    // create Previous button
    Button prevButton = new Button("Previous Puzzle");
    prevButton.setOnAction(
        (ActionEvent e) -> {
          controller.clickResetPuzzle();
          controller.clickPrevPuzzle(); // call to controller
        });

    // create Random Puzzle button
    Button randomButton = new Button("Random Puzzle");
    randomButton.setOnAction(
        (ActionEvent e) -> {
          controller.clickResetPuzzle();
          controller.clickRandPuzzle(); // call to controller
        });

    // add the buttons to the HBox
    hbox.getChildren().addAll(prevButton, nextButton, randomButton, resetButton);

    return hbox; // return the HBox as the parent container
  }
}
