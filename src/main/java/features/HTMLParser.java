package features;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class HTMLParser {

    public static String parse(String url, String saveDir) throws IOException {
    	try {
            if (url == null || url.isEmpty()) {
                return ""; // Return empty string if URL is null or empty
            }
            
            Document doc = Jsoup.connect(url).get();
            String filename = saveDir + "/" + url.replaceAll("[^a-zA-Z0-9.-]", "_") + ".txt";
            //String filename = saveDir + "/" + url + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
            writer.write(doc.text());
            writer.close();

            Elements links = doc.select("a[href]");
            String linksString = "";
            for (Element link : links) {
                String nextUrl = link.absUrl("href");
                if (nextUrl != null && !nextUrl.isEmpty() && isValidUrl(nextUrl)) {
                    linksString += nextUrl + " ";
                }
            }
            return linksString.trim();
        } catch (HttpStatusException e) {
            System.err.println("HTTP error fetching URL. Status=" + e.getStatusCode() + ", URL=[" + url + "]");
            return ""; // Return an empty string or handle it as appropriate
        }
    }

    private static boolean isValidUrl(String url) {
        return url.startsWith("http://") || url.startsWith("https://");
    }
}
