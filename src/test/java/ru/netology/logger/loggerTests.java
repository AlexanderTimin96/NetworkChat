package ru.netology.logger;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class loggerTests {
    private static String pathName = "src/test/java/ru/netology/logger/log.log";
    public final Logger logger = Logger.getInstance();

    @AfterAll
    public static void afterAllTests() {
        File logFile = new File(pathName);
        logFile.delete();
    }

    @Test
    public void createFileLogTest() {
        logger.createFileLog(pathName);

        File file = new File(pathName);
        Assertions.assertTrue(file.exists());
    }

    @Test
    public void logTest() {
        String excepted = "(INFO) OK!";

        logger.log(pathName, LevelLog.INFO, "OK!");

        try (BufferedReader in = new BufferedReader(new FileReader(pathName))) {
            String line;
            while ((line = in.readLine()) != null) {
                Assertions.assertTrue(line.contains(excepted));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
