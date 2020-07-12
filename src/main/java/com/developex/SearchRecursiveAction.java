package com.developex;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.developex.Main.*;

public class SearchRecursiveAction extends RecursiveAction {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);


    private String url;
    private String text; //remove

    public SearchRecursiveAction(String url, String text) {
        this.url = url;
        this.text = text;
    }

    @Override
    protected void compute() {
        LOGGER.info("###########");
        LOGGER.info("Computing url = " + url);

        processing(url, text);

        Collection<SearchRecursiveAction> collection = createSubactions();
        if (!collection.isEmpty()) {
            for (SearchRecursiveAction temp : collection
                    ) {
                if (!tempTasks.contains(temp)) {
                    tempTasks.add(temp);
                }
            }
        }

        LOGGER.info("Added subtasks. TempTasks size = " + tempTasks.size());

    }

    private Collection<SearchRecursiveAction> createSubactions() {
        List<SearchRecursiveAction> subTasks = new ArrayList<>();

        Document doc = convertToDoc(url);

        if (doc == null) return subTasks;

        String lincRegex = "a[href^=" + LINK_STARTED_WITH + "]";

        Elements links = doc.select(lincRegex);
        List<String> urls = links.stream().map(e -> e.attr("href")).distinct()
                .filter(s -> !proceededUrls.containsKey(s)).collect(Collectors.toList());

        int j = 1;
        for (String subUrl : urls
                ) {
            subTasks.add(new SearchRecursiveAction(subUrl, text));
            proceededUrls.put(subUrl, new Object());
            LOGGER.info("" + j++ + ". " + subUrl);

        }
        return subTasks;
    }

    private void processing(String url, String text) {
        LOGGER.info("Start proc. Url = " + url + " id = " + Thread.currentThread().getId());
        Document doc = convertToDoc(url);
        if (doc != null && doc.wholeText() != null && doc.wholeText().contains(text)) {
            LOGGER.info("Url " + url + " added" + " id = " + Thread.currentThread().getId());
            searchingResults.putIfAbsent(url, new Object());
        }
        LOGGER.info("Stop proc. Url = " + url + " id = " + Thread.currentThread().getId());
        LOGGER.info("***");
    }

    private Document convertToDoc(String url) {
        Document doc = null;
        try {
            Connection con = Jsoup.connect(url);
            LOGGER.info("Connection = " + (con != null) + " id = " + Thread.currentThread().getId());
            doc = con.get();
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.info("Error thread id = " + Thread.currentThread().getId());

        }

        return doc;
    }

    public String getUrl() {
        return url;
    }
}