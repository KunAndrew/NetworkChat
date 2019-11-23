package client.controller;


import client.controller.message.IMessageService;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.commons.lang3.exception.ExceptionUtils;
import сlient.controller.message.ServerMessageService;

public class PrimaryController implements Initializable {

    public @FXML
    javafx.scene.control.TextArea chatTextArea;
    public @FXML
    TextField messageText;
    public @FXML
    Button sendMessageButton;

    public @FXML TextField loginField;
    public @FXML
    PasswordField passField;

    public @FXML
    HBox authPanel;
    public @FXML
    VBox chatPanel;


    private IMessageService messageService;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//        this.messageService = new MockMessageService(chatTextArea);
        try {
            this.messageService = new ServerMessageService(this, true);
        } catch (Exception e) {
            showError(e);
        }
    }

    private void showError(Exception e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("oops! Something went wrong!");
        alert.setHeaderText(e.getMessage());

        VBox dialogPaneContent = new VBox();

        Label label = new Label("Stack Trace:");

        String stackTrace = ExceptionUtils.getStackTrace(e);
        TextArea textArea = new TextArea();
        textArea.setText(stackTrace);

        // dialogPaneContent.getChildren().addAll(label, textArea);

        // Set content for Dialog Pane
        alert.getDialogPane().setContent(dialogPaneContent);
        alert.setResizable(true);
        alert.showAndWait();

        e.printStackTrace();
    }

    @FXML
    public void sendText(ActionEvent actionEvent) {
        sendMessage();
    }

    @FXML
    public void sendMessage(ActionEvent actionEvent) {
        sendMessage();
    }

    private void sendMessage() {
        String message = messageText.getText();
        chatTextArea.appendText("Я: " + message + System.lineSeparator());
        messageService.sendMessage(message);
        //messageText.clear();
    }

    public void shutdown() {
        try {
            messageService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void sendAuth(ActionEvent actionEvent) {
        String login = loginField.getText();
        String password = passField.getText();
        messageService.sendMessage(String.format("/auth %s %s", login, password));
    }
}
