package HoneyBeeBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.commons.io.FilenameUtils;

public class readFiles {
    public static String lineParse(String filePath, String dest_dir) throws FileNotFoundException {
        String isMalicious = null;
        System.out.println("\n[ ** ] Starting finished file extraction. [ ** ]\n");
        try {
            if (filePath != null) {
                File f = new File(filePath);
                File[] dir_list = f.listFiles();
                for (File dir_entry : dir_list) {
                    if (dir_entry == null) {
                        System.out.println("Reached end of list, need to recycle through the directory.");
                    } else if (dir_entry.isFile()) {
                        System.out.println("-> " + dir_entry.toString());
                        String ext = dir_entry.toString();
                        String ext_Test = FilenameUtils.getExtension(ext);
                        if (ext_Test.equals("dorked")) {
                            String type_Query = dir_entry.toString();
                            BufferedReader in_file = new BufferedReader(new FileReader(dir_entry));
                            checkHoney check_honey = new checkHoney();
                            if ((type_Query.contains("malicious") | type_Query.contains("oracle") | type_Query.contains("bitcoin") | type_Query.contains("vuln"))) {
                                isMalicious = "yes";
                            } else {
                                isMalicious = "no";
                            }
                            String line;
                            while ((line = in_file.readLine()) != null) {
                                try {
                                    if (checkHoney.check(line)) {
                                        PreparedStatement preparedStatement = honeyBee.honeybee.prepareStatement("INSERT INTO dork_bin(url, honeypot, no_touch, type_query, is_malicious) VALUES (?, ?, ?, ?, ?)");
                                        preparedStatement.setString(1, line);
                                        preparedStatement.setBoolean(2, true);
                                        preparedStatement.setInt(3, 0);
                                        preparedStatement.setString(4, type_Query);
                                        preparedStatement.setString(5, isMalicious);
                                        preparedStatement.execute();
                                        continue;
                                    }
                                    PreparedStatement insert = honeyBee.honeybee.prepareStatement("INSERT INTO dork_bin(url, honeypot, no_touch, type_query, is_malicious) VALUES (?, ?, ?, ?, ?)");
                                    insert.setString(1, line);
                                    insert.setBoolean(2, false);
                                    insert.setInt(3, 0);
                                    insert.setString(4, type_Query);
                                    insert.setString(5, isMalicious);
                                    insert.execute();
                                } catch (SQLException sQLException) {}
                            }
                            in_file.close();
                            dir_entry.renameTo(new File(dest_dir + "/" + dir_entry.getName()));
                            dir_entry.delete();
                        }
                    }
                }
            } else {
                System.out.println("File path was empty.");
                return "Cannot complete, File Path was empty. Going to Rerun.";
            }
            return "Done";
        } catch (NullPointerException|java.io.IOException nullPointerException) {
            return "Routine finished.";
        }
    }
}
