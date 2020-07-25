package HoneyBeeBot;

import org.postgresql.util.PSQLException;

import com.diogonunes.jcolor.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class honeyBee {
    static Connection honeybee = null;

    static Statement my_honey = null;

    public static void main(String[] args) throws PSQLException {
        try {
            System.out.println("[ ?? ] Testing connection. [ ?? ]");
            Class.forName("org.postgresql.Driver");
            honeybee = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "albert", "");
            System.out.println("[ ** ]Connection looks good! [ ** ]\n" + honeybee.getClientInfo() + "\n[ !! ] Preparing statement. [ !! ]");

            my_honey = honeybee.createStatement();
            String to_execute = "CREATE TABLE IF NOT EXISTS dork_bin (id SERIAL PRIMARY KEY NOT NULL, dtg TIMESTAMP with time zone NOT NULL, url TEXT UNIQUE NOT NULL, honeypot BOOLEAN, is_alive TEXT, cms TEXT, shodan_url TEXT, pot_or_not_url TEXT, host_ip TEXT UNIQUE, no_touch INTEGER, type_query TEXT, is_malicious TEXT, dnsdumpster TEXT UNIQUE )";
            System.out.println("[ !! ] Statement prepared. [ !! ]");
            System.out.println("[ !! ] Executing: \n" + to_execute.toString() + " [ !! ]");
            System.out.println("[ !! ] Please wait, updating dnsdumpster entries. [ !! ]");
            updateDNSDumpster.doDNSUpdate();
            try {
                my_honey.execute(to_execute);
                readFiles.lineParse("resources/queried", "resources/queired/finished");
                ArrayList<String> firehoseControl = new ArrayList<>();
                ArrayList<String> altFireHostControl = new ArrayList<>();
                try {
                    PreparedStatement nothoney = honeybee.prepareStatement("SELECT url from dork_bin where no_touch = 1 AND is_alive IS null");
                    ResultSet doNotHoney = nothoney.executeQuery();
                    while (doNotHoney.next())
                        altFireHostControl.add(doNotHoney.getString(1));
                    for (int i = 0; i < altFireHostControl.size(); i++) {
                        URL passThrough = new URL(altFireHostControl.get(i));
                        try {
                            notHoneyCrawl.doNonHoneyCrawl(passThrough);
                        } catch (Exception e) {
                        }
                    }
                    PreparedStatement limitBy = honeybee.prepareCall("SELECT url from dork_bin where no_touch = 0 AND is_alive IS null");
                    ResultSet doControl = limitBy.executeQuery();
                    while (doControl.next())
                        firehoseControl.add(doControl.getString(1));
                    for (int j = 0; j < firehoseControl.size(); j++) {
                        try{
                            honeyCrawl.doCrawl(firehoseControl.get(j));
                        }catch (Exception e){
                        }
                        firehoseControl.remove(j);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } catch (SQLException|java.io.IOException npe) {
                npe.printStackTrace();
            }
        } catch (ClassNotFoundException|SQLException|ClassCastException|NoClassDefFoundError npe) {
            System.out.println("Looks like there was an error...\n-> " + npe.toString() + "getMessage: " + npe.getMessage());
        }
    }
}
