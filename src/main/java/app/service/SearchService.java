package app.service;

import app.model.SearchRecursiveAction;
import app.model.Statictic;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Service
public class SearchService {

    private final ThreadPoolExecutor threadPoolExecutor;


    public static ConcurrentHashMap<String, Object> searchingResults = new ConcurrentHashMap<>();
    public static List<SearchRecursiveAction> tasks = new ArrayList<>();
    public static List<SearchRecursiveAction> tempTasks = new ArrayList<>();
    public static ConcurrentHashMap<String, Object> proceededUrls = new ConcurrentHashMap<>();
    public static ConcurrentHashMap<String, String> errorUrls = new ConcurrentHashMap<>();
    public static AtomicInteger currentDepth = new AtomicInteger(1);

    @Value("${developex.link.start}")
    private String linkStart;

    @Setter
    @Value("${developex.initial.url}")
    private String initialUrl;

    @Setter
    @Value("${developex.initial.targetString}")
    private String targetString;

    @Setter
    @Value("${developex.search.depthLevel}")
    private int depthLevel;

    @Setter
    @Value("${developex.thread.number}")
    private int threadNumber;

    @Setter
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(
            2, ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);

    @Autowired
    public SearchService(@Qualifier("threadPoolExecutor") ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public void installProperties(String initialUrl, String targetString, String threadNumber, String depthLevel) {
        if (initialUrl != null && !initialUrl.isEmpty()) {
            System.setProperty("developex.initial.url", initialUrl);
            this.setInitialUrl(initialUrl);
        }
        if (targetString != null && !targetString.isEmpty()) {
            System.setProperty("developex.initial.targetString", targetString);
            this.setTargetString(targetString);
        }
        if (threadNumber != null && !threadNumber.isEmpty()) {
            System.setProperty("developex.thread.number", threadNumber);
            this.setThreadNumber(Integer.parseInt(threadNumber));
            forkJoinPool = new ForkJoinPool(
                    Integer.parseInt(threadNumber), ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, true);
        }
        if (depthLevel != null && !depthLevel.isEmpty()) {
            System.setProperty("developex.search.depthLevel", depthLevel);
            this.setDepthLevel(Integer.parseInt(depthLevel));
        }

        log.info("PROPERTIES:");
        for (String prop : System.getProperties().stringPropertyNames()
                ) {
            log.info(prop + " = " + System.getProperty(prop));
        }

    }

    public Statictic start(String initialUrl, String targetString, String threadNumber, String depthLevel) {

        installProperties(initialUrl, targetString, threadNumber, depthLevel);
        threadPoolExecutor.execute(this::proceed);
        return returnStat();
    }

    public Statictic returnStat() {
        Statictic stat = null;
        try {
            stat = threadPoolExecutor.submit(this::createStat).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return stat;
    }

    public Statictic createStat() {
        return new Statictic(currentDepth.get(), new ArrayList<>(proceededUrls.keySet()), new ArrayList<>(searchingResults.keySet()), errorUrls);
    }

    public void proceed() {

        forkJoinPool.invoke(new SearchRecursiveAction(initialUrl, targetString, linkStart));
        log.info("Tasks size = " + tasks.size());
        log.info("TempTasks size = " + tempTasks.size());
        tasks = new ArrayList<>(tempTasks);
        tempTasks.clear();
        log.info("Tasks size = " + tasks.size());
        log.info("TempTasks size = " + tempTasks.size());

        for (int i = 1; i <= depthLevel; i++) {
            log.info("*************STAGE " + i + "*************");
            ForkJoinTask.invokeAll(tasks);
            tasks = new ArrayList<>(tempTasks);
            tempTasks.clear();
            currentDepth.incrementAndGet();
        }

        log.info("searchingResults " + searchingResults.size() + " :");
        for (String str : searchingResults.keySet()
                ) {
            log.info(str);
        }

        log.info("errorUrls " + errorUrls.size() + " :");
        for (String str : errorUrls.keySet()
                ) {
            log.info(str);
        }
    }
}
