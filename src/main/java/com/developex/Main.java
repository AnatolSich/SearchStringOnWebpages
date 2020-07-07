package com.developex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Main {

    private static String resourcePath;
    private static String targetString;
    private final static String DEFAULT_TARGET_STRING = "Everything Ok";


    public static void main(String[] args) throws IOException {

        checkInputs(args);
    Optional<Elements> allResourceElementsOpt = findAllPageElements(new File(resourcePath));

        if (!allResourceElementsOpt.isPresent()) {
            System.out.println("No elements found");
            System.exit(1);
        }
        Elements allResourceElements = allResourceElementsOpt.get();

        if (allResourceElements.isEmpty()) {
            System.out.println("No elements found");
            System.exit(1);
        }

        for (Element element: allResourceElements
             ) {
            System.out.println("***");
            for (Attribute attribute: element.attributes()
                 ) {
                System.out.println(attribute);
            }
        }

        Document doc = Jsoup.parse(
                new File(resourcePath),
                "utf8",
                new File(resourcePath).getAbsolutePath());
  //      System.out.println(doc.text());
  //    System.out.println(doc.html());
  //      System.out.println(doc.toString());
       System.out.println(doc.wholeText());
       System.out.println(doc.wholeText().contains(DEFAULT_TARGET_STRING));


    }


    private static Optional<Elements> findAllPageElements(File htmlFile) {
        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    "utf8",
                    htmlFile.getAbsolutePath());
            return Optional.of(doc.getAllElements());
        } catch (FileNotFoundException e) {
            System.out.println("Error reading file: " + htmlFile.getAbsolutePath());
            System.out.println("Invalid diff-case filepath");
            System.exit(1);
        } catch (NullPointerException e) {
            System.out.println("No elements in diff-case found");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return Optional.empty();
    }


    private static void checkInputs(String args[]) {
        int argNumber = args.length;
        if (argNumber > 2) {
            System.out.println("Too much arguments");
            System.exit(1);
        } else if (argNumber < 2) {
            System.out.println("Not enough arguments");
            System.exit(1);
        } else {
            File file1 = new File(args[0]);
            if (file1.isFile()) {
                resourcePath = args[0];
                targetString = args[1];
            } else {
                System.out.println("First - must be filepath argument");
                System.exit(1);
            }
        }
        System.out.println("******Inputs********");
        for (int i = 0; i < 2; i++) {
            if (i == 0) {
                System.out.print("resourcePath = ");
                System.out.println(resourcePath);
            }
            if (i == 1) {
                System.out.print("targetString = ");
                System.out.println(targetString);
            }
        }
        System.out.println("************");
    }
}
