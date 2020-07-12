package com.developex;

import java.io.IOException;
import java.util.logging.*;

public class MyLogger {
    static private FileHandler fileTxt;
    static private MyFormatter formatterTxt;

    static public void setup() throws IOException {
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
/*        Logger rootLogger = Logger.getLogger("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }*/


        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("Log.txt");

        // create a TXT formatter
        formatterTxt = new MyFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

    }
}