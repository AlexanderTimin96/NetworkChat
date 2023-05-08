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

    public void log(String pathNameLogFile, LevelLog level, String msg) {
        File logFile = new File(pathNameLogFile);
        try (BufferedWriter bf = new BufferedWriter(new FileWriter(pathNameLogFile, true))) {
            if (logFile.createNewFile() || logFile.exists()) {
                bf.write("[" + dtf.format(LocalDateTime.now()) + "] (" + level + ") " + msg);
                bf.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
