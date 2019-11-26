package client.controller.message;

import client.controller.ChatLogmanager;
import client.controller.Network;
import client.controller.PrimaryController;
import client.controller.TextFileLogManager;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ServerMessageService implements IMessageService {

    private static final String HOST_ADDRESS_PROP = "server.address";
    private static final String HOST_PORT_PROP = "server.port";
    public static final String STOP_SERVER_COMMAND = "/end";

    private String hostAddress;
    private int hostPort;

    private final TextArea chatTextArea;
    public static PrimaryController primaryController;
    private boolean needStopServerOnClosed;
    private Network network;

    public static ChatLogmanager chatLogmanager = new TextFileLogManager();

    public ServerMessageService(PrimaryController primaryController, boolean needStopServerOnClosed) {
        this.chatTextArea = primaryController.chatTextArea;
        this.primaryController = primaryController;
        this.needStopServerOnClosed = needStopServerOnClosed;
        initialize();
    }


    private void initialize() {
        readProperties();
        startConnectionToServer();
    }

    private void startConnectionToServer() {
        try {
            this.network = new Network(hostAddress, hostPort, this);
        } catch (IOException e) {
            throw new ServerConnectionException("Failed to connect to server", e);
        }
    }

    private void readProperties() {
        Properties serverProperties = new Properties();
   /*     try (InputStream inputStream = getClass().getResourceAsStream("C:\\Users\\Andrew\\Desktop")) {
            serverProperties.load(inputStream);*/
        try (FileInputStream inputStream = new FileInputStream(new File("C:\\Java\\Chat\\src\\Client\\resources\\application.properties"))) {
            serverProperties.load(inputStream);
            hostAddress = serverProperties.getProperty(HOST_ADDRESS_PROP);
            hostPort = Integer.parseInt(serverProperties.getProperty(HOST_PORT_PROP));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read application.properties file", e);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid port value", e);
        }
    }

    @Override
    public void sendMessage(String message) {
        network.send(message);
        String mess[] = message.split(" ", 3);
        String pref = mess[0];
        System.out.println(mess[0]);
        if (pref.equals("/auth")) {
        } else {
            chatLogmanager.addToLog(message);
        }
    }

    @Override
    public void processRetrievedMessage(String message) {
        if (message.startsWith("/authok")) {
            primaryController.authPanel.setVisible(false);
            primaryController.chatPanel.setVisible(true);
        } else if (primaryController.authPanel.isVisible()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Authentication is failed");
            alert.setContentText(message);
            alert.showAndWait();
        } else {
            chatTextArea.appendText("Сервер: " + message + System.lineSeparator());
            chatLogmanager.addToLog(message);
        }
    }

    @Override
    public void close() throws IOException {
        if (needStopServerOnClosed) {
            sendMessage(STOP_SERVER_COMMAND);
        }
        network.close();
    }
}
