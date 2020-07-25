package HoneyBeeBot;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.ResultSet;

public class updateDNSDumpster {

    public static void doDNSUpdate() throws SQLException {
        PreparedStatement checkforNull = honeyBee.honeybee.prepareStatement("SELECT host_ip from dork_bin where dnsdumpster is null and host_ip is not null");
        PreparedStatement updateNull = honeyBee.honeybee.prepareStatement("UPDATE dork_bin SET dnsdumpster = ? WHERE host_ip LIKE ? ESCAPE ''");
        ArrayList<String> parseNulls = new ArrayList<>();
        ResultSet nullCheck = checkforNull.executeQuery();
        while (nullCheck.next()){
            parseNulls.add(nullCheck.getString(1));
//            System.out.println("Selected: " + nullCheck.getString(1));
        }
        for (int i = 0; i < parseNulls.size(); i++){
            String dnsDumpster = "https://dnsdumpster.com/static/map/" + parseNulls.get(i) + ".png";
            updateNull.setString(1, dnsDumpster);
            updateNull.setString(2, parseNulls.get(i));
            updateNull.executeUpdate();

        }
    }
}

//
