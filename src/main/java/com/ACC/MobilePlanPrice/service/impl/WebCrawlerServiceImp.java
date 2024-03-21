package com.ACC.MobilePlanPrice.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

@Service
@Scope("prototype")

public class WebCrawlerServiceImp {

	@Autowired
	private HTMLParser htmlParser;
	
    private Set<String> visitedUrls;
    private Queue<String> urlsToVisit;
    private int maxUrlsToVisit;
    private String saveDir;

    public WebCrawlerServiceImp() {
        visitedUrls = new HashSet<String>();
        urlsToVisit = new LinkedList<String>();
        this.maxUrlsToVisit = 5;
        this.saveDir = "MobileWebCrawlDir";
    }

    public void clear() {
        visitedUrls.clear();
        urlsToVisit.clear();
    }

    public Set<String> crawl(String startingUrl) throws IOException {
        File dir = new File(saveDir);
        if (!dir.exists()) {
            dir.mkdir();
        }
        urlsToVisit.add(startingUrl);
        while (!urlsToVisit.isEmpty() && visitedUrls.size() < maxUrlsToVisit) {
            String url = urlsToVisit.poll();
            if (!visitedUrls.contains(url)) {
                visitedUrls.add(url);
                System.out.println("Visiting: " + url);
                // Parse the page and extract links
                String links = htmlParser.parse(url, saveDir);
                for (String nextUrl : links.split(" ")) {
                    if (!visitedUrls.contains(nextUrl)) {
                        urlsToVisit.add(nextUrl);
                    }
                }
            }
        }
        System.out.println("Website is crawled!");
        return visitedUrls;
    }
    
    


}