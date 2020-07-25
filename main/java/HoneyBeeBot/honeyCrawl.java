package HoneyBeeBot;

import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class honeyCrawl {
    public static void doCrawl(String toRequest) throws SQLException, IOException, PSQLException, MalformedURLException {
        System.out.println("Testing: " + toRequest);
        Pattern ipSearch = Pattern.compile("((\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3}))?");
        Matcher ipFinder = ipSearch.matcher(toRequest);
        PreparedStatement update_host_ip = honeyBee.honeybee.prepareStatement("UPDATE dork_bin SET host_ip = ?, shodan_url = ?, is_alive = ?, pot_or_not_url = ? , dnsdumpster = ? WHERE url LIKE ? ESCAPE ''");
        PreparedStatement dup_host_ip = honeyBee.honeybee.prepareStatement("UPDATE dork_bin SET shodan_url = ?, pot_or_not_url = ?, dnsdumpster = ? WHERE url LIKE ? ESCAPE ''");
        String dnsDumpster = "https://dnsdumpster.com/static/map/" + ipFinder.group(0) + ".png";
        String honeyScore = "https://api.shodan.io/labs/honeyscore/" + ipFinder.group(0) + "?key=";
        while (ipFinder.find()) {
            if (((!ipFinder.group(0).isEmpty() ? 1 : 0) & (!toRequest.startsWith("ftp") ? 1 : 0)) != 0) {
                String shodanURIBuild = "https://shodan.io/host/" + ipFinder.group(0);
                String hostAddressBuild = ipFinder.group(0);
                URL nochange = new URL("http://" + ipFinder.group(0) + "/");
                URL obj = new URL(toRequest);
                HttpURLConnection toCon = (HttpURLConnection)obj.openConnection();
                toCon.setRequestMethod("GET");
                toCon.setConnectTimeout(2000);
                toCon.setInstanceFollowRedirects(false);
                toCon.setRequestProperty("User-Agent", "HoneyBot-1.0(https://github.com/oldkingcone/Auto_Dorker)/Java based version");
                toCon.setRequestProperty("Connection", "close");
                try {
                    int responseCode = toCon.getResponseCode();
                    if (responseCode == 200) {
                        update_host_ip.setString(1, hostAddressBuild);
                        update_host_ip.setString(2, shodanURIBuild);
                        update_host_ip.setString(3, "Alive");
                        update_host_ip.setString(4, honeyScore);
                        update_host_ip.setString(5, dnsDumpster);
                        update_host_ip.setString(6, toRequest);
                        update_host_ip.executeUpdate();
                    }
                    update_host_ip.setString(1, hostAddressBuild);
                    update_host_ip.setString(2, shodanURIBuild);
                    update_host_ip.setString(3, "Dead");
                    update_host_ip.setString(4, honeyScore);
                    update_host_ip.setString(5, dnsDumpster);
                    update_host_ip.setString(6, toRequest);
                    update_host_ip.executeUpdate();
                } catch (Exception al) {
                    dup_host_ip.setString(1, shodanURIBuild);
                    dup_host_ip.setString(2, honeyScore);
                    dup_host_ip.setString(3, dnsDumpster);
                    dup_host_ip.setString(4, toRequest);
                    dup_host_ip.executeUpdate();
                }
            }
        }
    }
}
