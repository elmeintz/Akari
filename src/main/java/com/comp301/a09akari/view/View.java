package com.comp301.a09akari.view;

import com.comp301.a09akari.model.*;
import com.comp301.a09akari.controller.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class View implements FXComponent, ModelObserver {

  private Model model;
  private ClassicMvcController controller;
  private Stage stage;

  public View(Model model, ClassicMvcController controller, Stage stage) {
    this.model = model;
    this.controller = controller;
    this.stage = stage;
  }

  @Override
  public void update(Model model) {
    Scene scene = new Scene(render());
    stage.setScene(scene);
  }

  @Override
  public Parent render() {
    // Create the root StackPane that will center everything in the stage
    StackPane root = new StackPane();

    // Create the VBox container that holds all UI components (InfoPanel, ButtonPanel, PuzzleView)
    VBox pane = new VBox();
    pane.setFillWidth(false); // Don't stretch VBox horizontally (important)
    pane.setAlignment(Pos.CENTER); // Align all children to the center

    // Add the InfoPanel to the VBox
    InfoPanel i = new InfoPanel(controller, model);
    pane.getChildren().add(i.render());

    // Add the ButtonPanel to the VBox
    ButtonPanel r = new ButtonPanel(controller);
    pane.getChildren().add(r.render());

    // StackPane to center the PuzzleView (will be placed in the center of the root StackPane)
    StackPane puzzleWrapper = new StackPane();
    puzzleWrapper.setAlignment(Pos.CENTER); // Center the content inside StackPane
    puzzleWrapper.setPadding(new Insets(20));

    // Create and render the PuzzleView
    PuzzleView p = new PuzzleView(model, controller);
    Parent puzzleViewNode = p.render();

    // Add the PuzzleView to the StackPane
    puzzleWrapper.getChildren().add(puzzleViewNode);

    // Add the StackPane containing PuzzleView to the VBox
    pane.getChildren().add(puzzleWrapper);

    // Now add the VBox to the root StackPane, centering it on the Stage
    root.getChildren().add(pane);

    return root;
  }
}
