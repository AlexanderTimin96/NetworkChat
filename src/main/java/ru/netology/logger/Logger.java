package ru.netology.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static Logger INSTANCE = null;
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private Logger() {
    }

    public static Logger getInstance() {
        if (INSTANCE == null) {
            synchronized (Logger.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Logger();
                }
            }
        }
        return INSTANCE;
    }

    public synchronized void log(String pathNameLogFile, LevelLog level, String msg) {
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(pathNameLogFile, true))) {
            bf.write("[" + dtf.format(LocalDateTime.now()) + "] (" + level + ") " + msg + "\n");
            bf.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void createFileLog(String pathNameLogFile) {
        File file = new File(pathNameLogFile);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
