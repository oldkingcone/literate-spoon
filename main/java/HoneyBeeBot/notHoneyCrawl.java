package HoneyBeeBot;

import org.postgresql.util.PSQLException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class notHoneyCrawl {
    public static void doNonHoneyCrawl(URL urlObject) throws MalformedURLException, IOException, SQLException, PSQLException, Exception {
        PreparedStatement update_host_ip = honeyBee.honeybee.prepareStatement("UPDATE dork_bin SET host_ip = ?, shodan_url = ?, is_alive = ?, pot_or_not_url = ?, dnsdumpster = ? WHERE url LIKE ? ESCAPE ''");
        PreparedStatement dup_host_ip = honeyBee.honeybee.prepareStatement("UPDATE dork_bin SET shodan_url = ?, pot_or_not_url = ?, dnsdumpster = ? WHERE url LIKE ? ESCAPE ''");
        String hostAddressBuild = urlObject.getHost();
        String shodanURIBuild = "https://shodan.io/host/" + hostAddressBuild;
        String baseURL = urlObject.getProtocol() + "://" + urlObject.getHost();
        String dnsdumpster = "https://dnsdumpster.com/static/map/" + hostAddressBuild + ".png";
        String honeyScore = "https://api.shodan.io/labs/honeyscore/" + hostAddressBuild + "?key=";

        try {
            System.out.println("Testing: " + urlObject.toString());
            if (((!baseURL.isEmpty() ? 1 : 0) & (!baseURL.startsWith("ftp") ? 1 : 0)) != 0) {
                URL newURL = new URL(baseURL);
                HttpURLConnection toCon = (HttpURLConnection) newURL.openConnection();
                toCon.setRequestMethod("GET");
                toCon.setConnectTimeout(2000);
                toCon.setInstanceFollowRedirects(false);
                toCon.setRequestProperty("User-Agent", "HoneyBot-1.0(https://github.com/oldkingcone/Auto_Dorker)/Java version");
                toCon.setRequestProperty("Connection", "close");
                int responseCode = toCon.getResponseCode();
                if (responseCode == 200) {
                    update_host_ip.setString(1, hostAddressBuild);
                    update_host_ip.setString(2, shodanURIBuild);
                    update_host_ip.setString(3, "Alive");
                    update_host_ip.setString(4, honeyScore);
                    update_host_ip.setString(5, dnsdumpster);
                    update_host_ip.setString(6, String.valueOf(urlObject));
                    update_host_ip.executeUpdate();
                } else {
                    update_host_ip.setString(1, hostAddressBuild);
                    update_host_ip.setString(2, shodanURIBuild);
                    update_host_ip.setString(3, "Dead");
                    update_host_ip.setString(4, honeyScore);
                    update_host_ip.setString(5, dnsdumpster);
                    update_host_ip.setString(6, String.valueOf(urlObject));
                    update_host_ip.executeUpdate();
                }

            }
        }catch (Exception e){
            dup_host_ip.setString(1, shodanURIBuild);
            dup_host_ip.setString(2, honeyScore);
            dup_host_ip.setString(3, dnsdumpster);
            dup_host_ip.setString(4, String.valueOf(urlObject));
            dup_host_ip.executeUpdate();
        }
    }
}