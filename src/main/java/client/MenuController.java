package client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import server.board.SixPointedStar;

import java.io.IOException;

public class MenuController {

    @FXML
    private Button exitButton;

    @FXML
    public void temporaryRedirection() throws IOException {
        exit();
        Parent root =  FXMLLoader.load(getClass().getClassLoader().getResource("fxml/sixPointedStar.fxml"));
        Stage chooseStage = new Stage();
        chooseStage.setTitle("Six-Pointed Star format");
        chooseStage.setScene(new Scene(root, 912.0, 666.0));
        chooseStage.show();
        //exit();
    }

    @FXML
    public void exit(){
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
