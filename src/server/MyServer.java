package server;

import org.apache.log4j.*;
import server.auth.AuthService;
import server.auth.DatabaseAuthService;
import server.client.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


import org.apache.log4j.Logger;

public class MyServer {
    Logger file = Logger.getLogger("file");
    String log4jConfPath = "C:\\Java\\Chat\\src\\log4j.properties";

    private static final int PORT = 8189;

    private final AuthService authService = new DatabaseAuthService();

    private List<ClientHandler> clients = new ArrayList<>();

    public MyServer() {
        PropertyConfigurator.configure(log4jConfPath);
        file.info("Server is running");


        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            authService.start();
            while (true) {
                file.info("Awaiting client connection...");
                Socket socket = serverSocket.accept();
                file.info("Client has connected");
                new ClientHandler(socket, this);
            }

        } catch (IOException e) {
            System.err.println("Ошибка в работе сервера. Причина: " + e.getMessage());
            file.error("Ошибка в работе сервера. Причина: " + e.getMessage());
            e.printStackTrace();
        } finally {
            file.info("Server stoped");
            authService.stop();
        }
    }

    public synchronized void subscribe(ClientHandler clientHandler) {
        file.info("Client subscrube");
        clients.add(clientHandler);
    }

    public synchronized void unsubscribe(ClientHandler clientHandler) {
        file.info("Client unsubscrube");
        clients.remove(clientHandler);
    }

    public AuthService getAuthService() {
        file.info("getAuthService");
        return authService;
    }

    public synchronized boolean isNickBusy(String nick) {
        file.info("isNickBusy");
        for (ClientHandler client : clients) {
            if (client.getClientName().equals(nick)) {
                return true;
            }
        }

        return false;
    }

    public synchronized void broadcastMessage(String message) {
        file.info("broadcastMessage");
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }

    }

    public synchronized void privateMessage(String message, String nick, String authNick) {
        file.info("privateMessage");
        for (ClientHandler client : clients) {
            //client.sendMessage(message);
            if (client.getClientName().equalsIgnoreCase(nick)) {
                client.sendMessage(authNick + ": " + message);
            }
        }
    }
}
