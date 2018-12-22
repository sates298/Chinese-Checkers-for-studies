package client.controller;

import client.network.ServerConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ChooseServerController extends AbstractController {
  @FXML
  private TextField serverField;
  @FXML
  private Label warningLabel;
  @FXML
  public void connectToServer() {
    try {
      ServerConnector.getInstance().makeConnection(serverField.getText(), 1235);
      redirect("fxml/menu.fxml", "Main Menu", 300, 275, serverField);
    } catch (IOException e) {
      warningLabel.setText("connection unavailable");
    }


  }
}
