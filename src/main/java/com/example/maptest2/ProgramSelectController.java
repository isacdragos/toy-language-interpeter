package com.example.maptest2;

import controller.Controller;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import model.prgState.PrgState;
import model.statement.IStmt;
import repository.IRepository;
import repository.Repository;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

public class ProgramSelectController {

    @FXML
    private ListView<String> programListView;

    @FXML
    private Button selectButton;

    private List<IStmt> programs;

    public void setPrograms(List<IStmt> programs) {
        this.programs = programs;
        programListView.getItems().clear();
        programs.forEach(program -> programListView.getItems().add(program.toString()));
    }

    @FXML
    private void handleSelectProgram() {
        int index = programListView.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a program");
            alert.showAndWait();
            return;
        }

        IStmt selectedProgram = programs.get(index);

        PrgState prg = new PrgState(selectedProgram);
        IRepository repo = new Repository(prg, "log" + index + ".txt");
        Controller controller = new Controller(repo, true);

        try {
            // Try several candidate resource names (adjust as needed to match your FXML filename)
            String[] candidates = {
                    "MainWindowController.fxml",
                    "main-window.fxml",
                    "/com/example/guifinal/MainWindowController.fxml",
                    "/com/example/guifinal/main-window.fxml"
            };

            Optional<URL> fxmlUrl = java.util.Arrays.stream(candidates)
                    .map(name -> {
                        if (name.startsWith("/")) return getClass().getResource(name);
                        return getClass().getResource(name);
                    })
                    .filter(url -> url != null)
                    .findFirst();

            if (fxmlUrl.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Could not find Main Window FXML. Check the FXML filename and its location in `src/main/resources/com/example/guifinal`.");
                alert.showAndWait();
                return;

            }
            FXMLLoader loader = new FXMLLoader(fxmlUrl.get());
            Parent root = loader.load();

            MainWindowController mainCtrl = loader.getController();
            mainCtrl.setController(controller);

            Stage stage = new Stage();
            stage.setTitle("Toy Language Interpreter");
            stage.setScene(new Scene(root));
            stage.show();

//            ((Stage) programListView.getScene().getWindow()).close();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.showAndWait();
        }
    }
    }
