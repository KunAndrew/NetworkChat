package client.controller;
import client.controller.message.ServerMessageService;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class TextFileLogManager implements ChatLogmanager {

    public static final String LOG_NAME = "log";                                     //каталог логов
    public static final String LOG_FILE_NAME = "chatLogFile.log";
    private int NUMBER_LINES_HISTORY = 100;
    private File filePath;
    private File logfile;
    //имя файла

    @Override

    public void initLog() {
        filePath = new File(LOG_NAME);
        filePath.mkdir();
        logfile = new File(filePath + "\\"+ LOG_FILE_NAME);
        if (!logfile.exists()) {
            try {
                logfile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        getChatLog();
    }

    @Override
    public void addToLog(String clientMessage) {
        try {
            FileWriter writer = new FileWriter(logfile, true);

            BufferedWriter bufferWriter = new BufferedWriter(writer);
            bufferWriter.write(clientMessage + "\n");
            bufferWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void getChatLog() {
        try {
            FileReader fileReader = new FileReader(logfile);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            List<String> history = new LinkedList<>();
            while ((line = bufferedReader.readLine()) != null) {
                history.add(line + '\n');
            }
            bufferedReader.close();
            int numberLinesHistory;
            if (history.size() >= NUMBER_LINES_HISTORY) numberLinesHistory = history.size() - 1 - NUMBER_LINES_HISTORY;
            else numberLinesHistory = 0;
            for (int i = numberLinesHistory; i < history.size(); i++) {
                if (history.size() != 0)
                    ServerMessageService.primaryController.writeLog(history.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
