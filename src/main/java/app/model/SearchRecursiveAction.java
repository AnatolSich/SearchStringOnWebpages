package app.model;

import app.service.SearchService;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;

import static app.service.SearchService.tempTasks;

@Log4j2
public class SearchRecursiveAction extends RecursiveAction {

    private String url;

    private String targetString;

    private String linkStart;

    public SearchRecursiveAction(String url, String targetString, String linkStart) {
        this.url = url;
        this.targetString = targetString;
        this.linkStart = linkStart;
    }

    @Override
    protected void compute() {
        log.info("###########");
        log.info("Computing url = " + url);

        processing(url);

        Collection<SearchRecursiveAction> collection = createSubactions();
        if (!collection.isEmpty()) {
            for (SearchRecursiveAction temp : collection
                    ) {
                if (!tempTasks.contains(temp)) {
                    tempTasks.add(temp);
                }
            }
        }

        log.info("Added subtasks. TempTasks size = " + tempTasks.size());

    }

    private Collection<SearchRecursiveAction> createSubactions() {
        List<SearchRecursiveAction> subTasks = new ArrayList<>();

        Document doc = convertToDoc(url);

        if (doc == null) return subTasks;

        String lincRegex = "a[href^=" + linkStart + "]";

        Elements links = doc.select(lincRegex);
        List<String> urls = links.stream().map(e -> e.attr("href")).distinct()
                .filter(s -> !SearchService.proceededUrls.containsKey(s)).collect(Collectors.toList());

        int j = 1;
        for (String subUrl : urls
                ) {
            subTasks.add(new SearchRecursiveAction(subUrl, targetString, linkStart));
            SearchService.proceededUrls.put(subUrl, new Object());
            log.info("" + j++ + ". " + subUrl);
        }
        return subTasks;
    }

    private void processing(String url) {
        log.info("Start proc. Url = " + url + " id = " + Thread.currentThread().getId());
        Document doc = convertToDoc(url);

        if (doc != null && doc.wholeText() != null && doc.wholeText().contains(targetString)) {
            log.info("Url " + url + " added" + " id = " + Thread.currentThread().getId());
            SearchService.searchingResults.putIfAbsent(url, new Object());
        }
        log.info("Stop proc. Url = " + url + " id = " + Thread.currentThread().getId());
        log.info("***");
    }

    private Document convertToDoc(String url) {
        Document doc = null;
        try {
            Connection con = Jsoup.connect(url);
            log.info("Connection = " + (con != null) + " id = " + Thread.currentThread().getId());
            if (con != null) {
                doc = con.get();
            } else {
                for (int i = 0; i < 3; i++) {
                    con = Jsoup.connect(url);
                    if (con != null) {
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error thread id = " + Thread.currentThread().getId());
            log.error(e instanceof HttpStatusException);
            if (e instanceof HttpStatusException) {
                log.error(((HttpStatusException) e).getStatusCode());
                if (((HttpStatusException) e).getStatusCode() == 999) {
                    SearchService.errorUrls.putIfAbsent(url, e.getMessage());
                }
            }
        }

        return doc;
    }

}