import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

//Uses Jsoup to crawl through web and creates text files with parsed information.
public class WebCrawler {
    public static String crawl(String link) {

        String html = urlToHTML(link);

        Document doc = Jsoup.parse(html);
        String text = doc.text();
        String title = doc.title();
        createFile(title, text, link);

        Elements e = doc.select("a");
        StringBuilder links = new StringBuilder();

        for (Element e2 : e) {
            String href = e2.attr("abs:href");
            if (href.length() > 3) {
                links.append("\n").append(href);
            }
        }
        return links.toString();
    }

    public static void createFile(String title, String text, String link) {
        StringBuilder newTitle = new StringBuilder();
        try {
            String[] titleSplit = title.split("\\|");
            for (String s : titleSplit) {
                newTitle.append(" ").append(s);
            }
            File f = new File("WebPages//" + newTitle.toString()
                    .replaceAll("[\\\\/:*?\"<>|]", "").trim() + ".txt");
            f.createNewFile();
            PrintWriter pw = new PrintWriter(f);
            pw.println(link);
            pw.println(text);
            pw.close();

        } catch (IOException e) {
            System.err.println("Error creating file: " + newTitle.toString().trim() + ".txt");
        }

    }

    public static String urlToHTML(String link) {
        try {
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            Scanner sc = new Scanner(conn.getInputStream());
            StringBuilder sb = new StringBuilder();
            while (sc.hasNext()) {
                sb.append(" ").append(sc.next());
            }

            String result = sb.toString();
            sc.close();
            return result;
        } catch (IOException e) {
            System.out.println(e);
        }
        return link;
    }

    public static void crawlPages(String links) {

        try {
            File f = new File("CrawledPages.txt");
            f.createNewFile();
            FileWriter fwt = new FileWriter(f);
            fwt.close();

            StringBuilder links2 = new StringBuilder();
            for (String link : links.split("\n")) {
                links2.append(crawl(link));
                System.out.println(link);
                FileWriter fw = new FileWriter(f, true);
                fw.write(link + "\n");
                fw.close();
            }

            StringBuilder links3 = new StringBuilder();
            for (String link : links2.toString().split("\n")) {
                In in = new In(f);
                String linksRead = in.readAll();
                if (!linksRead.contains(link)) {
                    links3.append(crawl(link));
                    System.out.println(link);
                    FileWriter fw = new FileWriter(f, true);
                    fw.write(link + "\n");
                    fw.close();
                }
            }

            for (String link : links3.toString().split("\n")) {
                In in = new In(f);
                String linksRead = in.readAll();
                if (!linksRead.contains(link)) {
                    crawl(link);
                    System.out.println(link);
                    FileWriter fw = new FileWriter(f, true);
                    fw.write(link + "\n");
                    fw.close();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void crawlDefault() {
        String links = "https://www.cbc.ca/" + "\n" + "https://www.bbc.com/news/world/us_and_canada" + "\n"
                + "https://www.ctvnews.ca/" + "\n" + "https://www.cicnews.com/";
        crawlPages(links);
    }

    public static void crawlCustom(String line) {
        String[] links = line.split(" ");
        StringBuilder newLine = new StringBuilder();
        for (String link : links) {
            newLine.append(link).append("\n");
        }
        crawlPages(newLine.toString());
    }

    public static void wipeWebPages() {
        File directory = new File("WebPages");
        File[] files = directory.listFiles();
        assert files != null;

        for (File f : files) {
            if (!f.delete()) {
                System.out.println("Unable to delete file: " + f.getName());
            }
        }
        System.out.println("WebPages wiped!");
    }

    public static void main(String[] args) {
        crawlDefault();
    }
}
