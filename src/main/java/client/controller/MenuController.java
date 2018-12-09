package client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class MenuController {

    @FXML
    private Button exitButton;

    @FXML
    public void temporaryRedirection() throws IOException {
        exit();
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/board.fxml")));
        Stage chooseStage = new Stage();
        chooseStage.setTitle("Board");
        chooseStage.setScene(new Scene(root, 920,670));
        chooseStage.show();
        //exit();
    }

    @FXML
    public void joinToGame() throws IOException{
        exit();
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/joinToGame.fxml")));
        Stage chooseStage = new Stage();
        chooseStage.setTitle("Join To Game");
        chooseStage.setScene(new Scene(root, 300, 275));
        chooseStage.show();
        temporaryRedirection();
    }

    @FXML
    public void newGame() throws IOException{
        exit();
        Parent root =
                FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/newGame.fxml")));
        Stage chooseStage = new Stage();
        chooseStage.setTitle("Create New Game");
        chooseStage.setScene(new Scene(root, 300, 275));
        chooseStage.show();
    }

    @FXML
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
