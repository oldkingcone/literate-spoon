package HoneyBeeBot;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;



public class gquery {
    public static final String USER_AGENT = "HoneyBot-1.0(https://github.com/oldkingcone/Auto_Dorker)/Java based version/Please dont ban me google./Dont make me Stan you.";
    public static void doDork(File fileName) throws Exception, IOException {
        PreparedStatement doInsert = honeyBee.honeybee.prepareStatement("INSERT INTO dork_bin(url) VALUES (?)");
        FileReader dorkReader = new FileReader(fileName);
        BufferedReader doBuffered = new BufferedReader(dorkReader);
        List<String> dorkLine = new ArrayList<String>();
        String line = null;
        while ((line = doBuffered.readLine()) != null) {
            // start=10, other pages increment by 10. need to find a way to determine how many pages there are in the results.
            Document doc = Jsoup.connect(new StringBuilder().append("https://google.com/search?q=").append(line).toString()).userAgent(USER_AGENT).get();
            Elements lengthTest = doc.select(".AaVjTc > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(11) > a:nth-child(1)");
            if (!lengthTest.text().isEmpty()) {
                Elements pageTest = doc.select(".AaVjTc > tbody:nth-child(1) > tr:nth-child(1) > td:nth-child(2) > a:nth-child(1) > span:nth-child(1)");
                if (!pageTest.isEmpty()) {
                    for (Element result : doc.select("h3.r a")) {
                        final String url = result.attr("href");
                        doInsert.setString(1, url.toString());
                        doInsert.execute();
                    }
                } else {
                    for (int i = 0; i > 100; i++){
                        String longRequest = new String(line + "&start=" + i);
                        Connection connect = Jsoup.connect(String.valueOf("https://google.com/search?q=" + longRequest));
                        connect.userAgent(USER_AGENT);
                        Document paged = connect.get();
                        for (Element result : paged.select("h3.r a")){
                            final String url = result.attr("href");
                            doInsert.setString(1, url.toString());
                            doInsert.execute();
                        }
                    }
                }
            }
            doBuffered.close();
        }
    }
}
//.YyVfkd > span:nth-child(1)
//.YyVfkd
//.AaVjTc > tbody:nth-child(1) > tr:nth-child(1)