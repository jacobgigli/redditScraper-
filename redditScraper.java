import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Scanner;
/*
A simple web-scraping program that gets the top 25 posts of all time from any subreddit
 Author: Jacob Gigli
 */
public class redditSctaper {
    public static void main(String[] args) throws IOException {
        // Fixed key words for the program; initialize program variables
        String titles [] = {"User: ", "Title: ", ""};
        String urls [] = {"Discussion: ", "Link: ", ""};
        int urlCounter;
        int titlesCounter;
        boolean printedComments = false;
        Scanner scanner = new Scanner(System.in);
        int counter = 1;
        System.out.println("Enter a sub reddit for its top posts");
        String subReddit = scanner.next();
        // Connect to reddit site to get the top posts
        Document document = Jsoup.connect("https://www.reddit.com/r/" + subReddit + "/top/?t=all").get();
        // iterate through all top posts
        for (Element data : document.select("div.scrollerItem")) {
            titlesCounter = 0;
            urlCounter = 0;
            // filter out promoted posts
            if (!data.text().contains("promoted")){
                System.out.println("#" + counter);
                counter = counter + 1;

                System.out.println("Upvotes: " + data.select("div._1rZYMD_4xY3gRcSS3p8ODO").text().replaceAll(" .+$", ""));
                for (Element data1 : data.select("a")) {
                    // Only print out links that lead to discussion or the post link itself
                    if (data1.text().contains(".com") || data1.attr("href").toLowerCase().contains(".com/r/"+subReddit)) {
                        String url = data1.attr("href");
                        // print out the links
                        System.out.println(urls[urlCounter] + url);
                        urlCounter = urlCounter + 1;

                    }
                    // only print out text that isn't empty and filter out flair text
                    else if (data1.text() != null && !data1.text().isEmpty() && !(data1.attr("href").contains("flair_name"))) {
                        if (!printedComments || data1.text().contains("comments")){
                            // print out the text
                            System.out.println(titles[titlesCounter] + data1.text());
                            titlesCounter = titlesCounter + 1;
                            // Stop printing text after the comments are printed; garbage is after
                            if (data1.attr("href").contains("comments")){
                                printedComments = true;
                            }



                        }

                    }

            }
                printedComments = false;

            }
            else {
                continue;
            }
            // Move on to next post
            System.out.println();
        }

    }
}
