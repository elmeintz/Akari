package com.comp301.a09akari.view;

import com.comp301.a09akari.model.*;
import com.comp301.a09akari.controller.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import com.comp301.a09akari.SamplePuzzles;

public class AppLauncher extends Application {

  private Model model;
  private ClassicMvcController controller;

  @Override
  public void start(Stage stage) {
    // create the PuzzleLibrary and populate it with SamplePuzzles
    PuzzleLibrary puzzleLibrary = new PuzzleLibraryImpl();
    addSamplePuzzlesToLibrary(puzzleLibrary);

    // initialize the Model and Controller
    model = new ModelImpl(puzzleLibrary);
    controller = new ControllerImpl(model);

    // initialize the View components
    View view = new View(model, controller, stage);

    // adds the model view as an observer
    model.addObserver(view);

    // set up the scene and show the stage
    Scene scene = new Scene(view.render());
    stage.setScene(scene);
    stage.setTitle("Akari Puzzle Game");
    stage.show();
  }

  // helper method to add puzzles to the library
  private void addSamplePuzzlesToLibrary(PuzzleLibrary puzzleLibrary) {
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_01));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_02));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_03));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_04));
    puzzleLibrary.addPuzzle(new PuzzleImpl(SamplePuzzles.PUZZLE_05));
  }
}
