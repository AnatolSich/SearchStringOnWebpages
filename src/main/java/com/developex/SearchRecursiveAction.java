package com.developex;

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

    //  private int[] arr;
    private String url;
    private String text; //remove

    public SearchRecursiveAction(String url, String text) {
        this.url = url;
        this.text = text;
    }

    @Override
    protected void compute() {

        if (currentDepth.intValue() < MAX_SEARCH_DEPTH) {
            ForkJoinTask.invokeAll(createSubactions());
        }
        processing(url, text);

    }

    private Collection<SearchRecursiveAction> createSubactions() {
        List<SearchRecursiveAction> subTasks = new ArrayList<>();

        Document doc = convertToDoc(url);

        String lincRegex = "a[href^=" + LINK_STARTED_WITH + "]";

        Elements links = doc.select(lincRegex);
        List<String> urls = links.stream().map(e -> e.attr("href")).collect(Collectors.toList());

        if (!urls.isEmpty()) {
            currentDepth.incrementAndGet();
        }

        for (String subUrl : urls
                ) {
            subTasks.add(new SearchRecursiveAction(subUrl, text));
        }
        return subTasks;
    }

    private void processing(String url, String text) {
        System.out.println("Start proc");
        Document doc = convertToDoc(url);
        if (doc.wholeText().contains(text)) {
            System.out.println("Url " + url + " added");
            searchingResults.putIfAbsent(url, new Object());
        }
        System.out.println("Stop proc");
    }

    private Document convertToDoc(String url) {
        File htmlFile = new File(url);
        Document doc = null;
        try {
            /*
             * TO DO
             * Document doc = Jsoup.connect(url).get();
             * */
            doc = Jsoup.parse(
                    htmlFile,
                    "utf8",
                    htmlFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }
}