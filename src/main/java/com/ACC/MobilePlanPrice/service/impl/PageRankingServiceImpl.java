package com.ACC.MobilePlanPrice.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
 
import org.springframework.stereotype.Service;

import com.ACC.MobilePlanPrice.model.PageRanking;
 
 
@Service
public class PageRankingServiceImpl {
	public List<PageRanking> getPageRanking(String filePath, String input) {
        List<PageRanking> topPages = new ArrayList<>();
 
        // Defining Array List to store keywords
        List<String> keywords = new ArrayList<>();
 
        // Splitting the input string into keywords
        for (String keyword : input.split(",")) {
            keywords.add(keyword.trim().toLowerCase());
        }
 
        // A Map to store the frequency of each keyword
        Map<String, Integer> keywordFrequencies = new HashMap<>();
 
        // To initialize the frequency of each keyword to 0
        for (String keyword : keywords) {
            keywordFrequencies.put(keyword, 0);
        }
 
        // The local directory that contains the web pages
        File folder = new File(filePath);
 
        // To get all files from the directory
        File[] listOfFiles = folder.listFiles();
 
        // Initialize a Priority queue to store the web pages as per their scores
        PriorityQueue<PageRanking> heap = new PriorityQueue<>(Comparator.comparingInt(PageRanking::getScore).reversed());
 
        // To loop through each web page
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                int score = 0;
 
                // to read the contents of the file
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
 
                    // To read each line of the file
                    while ((line = reader.readLine()) != null) {
                        // To split the line into words
                        StringTokenizer tokenizer = new StringTokenizer(line);
                        while (tokenizer.hasMoreTokens()) {
                            String word = tokenizer.nextToken().toLowerCase().replaceAll("[^a-z0-9]+", "");
 
                            // To check if the word is a keyword
                            if (keywords.contains(word)) {
                                // To increase the score and frequency of the keyword
                                score += 1;
                                keywordFrequencies.put(word, keywordFrequencies.getOrDefault(word, 0) + 1);
                            }
                        }
                    }
 
                    // To calculate the total score for the page
                    for (Map.Entry<String, Integer> entry : keywordFrequencies.entrySet()) {
                        score += entry.getValue();
                    }
 
                    // Reset the frequency of each keyword to 0
                    for (String keyword : keywords) {
                        keywordFrequencies.put(keyword, 0);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
 
                // To create a page object with the name and score of the file
                PageRanking page = new PageRanking(file.getName(), score);
                // To add the page object to the priority queue
                heap.offer(page);
            }
        }
 
        // To retrieve the top 10 web pages based on keyword matches
        int count = 0;
        while (!heap.isEmpty() && count < 10) {
            PageRanking page = heap.poll();
            topPages.add(page);
            count++;
        }
 
        return topPages;
    }
 
}