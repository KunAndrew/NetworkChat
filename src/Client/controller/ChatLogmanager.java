package client.controller;

public interface ChatLogmanager {
    void initLog();
    void addToLog(String clientMessage);
    void getChatLog();
}
