package com.developex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.logging.Logger;

public class Main {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public final static String LINK_STARTED_WITH = "https";


    public final static int MAX_SEARCH_DEPTH = 2;
    private final static int THREADS_NUMBER = 5;

    public static ConcurrentHashMap<String, Object> searchingResults = new ConcurrentHashMap<>();
    public static List<SearchRecursiveAction> tasks = new ArrayList<>();
    public static List<SearchRecursiveAction> tempTasks = new ArrayList<>();
    public static ConcurrentHashMap<String, Object> proceededUrls = new ConcurrentHashMap<>();

    private static String resourcePath;
    private static String targetString;
    public static ForkJoinPool forkJoinPool = new ForkJoinPool(THREADS_NUMBER);


    public static void main(String[] args) {

        try {
            MyLogger.setup();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Problems with creating the log file");
        }


        checkInputs(args);

        forkJoinPool.invoke(new SearchRecursiveAction(resourcePath, targetString));
        LOGGER.info("Tasks size = " + tasks.size());
        LOGGER.info("TempTasks size = " + tempTasks.size());
        tasks = new ArrayList<>(tempTasks);
        tempTasks.clear();
        LOGGER.info("Tasks size = " + tasks.size());
        LOGGER.info("TempTasks size = " + tempTasks.size());


        for (int i = 1; i <= MAX_SEARCH_DEPTH; i++) {
            LOGGER.info("*************STAGE " + i + "*************");
            ForkJoinTask.invokeAll(tasks);
            tasks = new ArrayList<>(tempTasks);
            tempTasks.clear();
        }


        LOGGER.info("searchingResults " + searchingResults.size() + " :");
        for (String str : searchingResults.keySet()
                ) {
            LOGGER.info(str);
        }


        LOGGER.info("proceededUrls " + proceededUrls.size() + " :");
        for (String str : proceededUrls.keySet()
                ) {
            LOGGER.info(str);
        }


    }

    private static void checkInputs(String args[]) {
        int argNumber = args.length;
        if (argNumber > 2) {
            LOGGER.info("Too much arguments");
            System.exit(1);
        } else if (argNumber < 2) {
            LOGGER.info("Not enough arguments");
            System.exit(1);
        } else {
            resourcePath = args[0];
            targetString = args[1];
        }
        LOGGER.info("******Inputs********");
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                System.out.print("resourcePath = ");
                LOGGER.info(resourcePath);
            }
            if (i == 1) {
                System.out.print("targetString = ");
                LOGGER.info(targetString);
            }
        }
        LOGGER.info("************");
    }
}
