package com.developex;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

import static com.developex.Main.*;

public class SearchRecursiveAction extends RecursiveAction {

    private String url;
    private String text; //remove

    public SearchRecursiveAction(String url, String text) {
        this.url = url;
        this.text = text;
    }

    @Override
    protected void compute() {
        processing(url, text);

        if (currentDepth.intValue() < MAX_SEARCH_DEPTH) {
            Collection<SearchRecursiveAction> collection = createSubactions();
            if (!collection.isEmpty()) {
                ForkJoinTask.invokeAll(collection);
            }
        }
    }

    private Collection<SearchRecursiveAction> createSubactions() {
        List<SearchRecursiveAction> subTasks = new ArrayList<>();

        Document doc = convertToDoc(url);

        String lincRegex = "a[href^=" + LINK_STARTED_WITH + "]";

        Elements links = doc.select(lincRegex);
        List<String> urls = links.stream().map(e -> e.attr("href")).collect(Collectors.toList());

        if (!urls.isEmpty()) {
            System.out.println("Increment currentDepth = " + currentDepth.incrementAndGet());
        }

        for (String subUrl : urls
                ) {
            subTasks.add(new SearchRecursiveAction(subUrl, text));
        }
        return subTasks;
    }

    private void processing(String url, String text) {
        System.out.println("Start proc. Url = " + url + " id = " + Thread.currentThread().getId());
        Document doc = convertToDoc(url);
        if (doc != null && doc.wholeText() != null && doc.wholeText().contains(text)) {
            System.out.println("Url " + url + " added" + " id = " + Thread.currentThread().getId());
            searchingResults.putIfAbsent(url, new Object());
        }
        System.out.println("Stop proc. Url = " + url + " id = " + Thread.currentThread().getId());
        System.out.println("***");
    }

    private Document convertToDoc(String url) {
        Document doc = null;
        try {
            Connection con = Jsoup.connect(url);
            System.out.println("Connection = " + (con != null) + " id = " + Thread.currentThread().getId());
            doc = con.get();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error thread id = " + Thread.currentThread().getId());

        }
        return doc;
    }
}