package tech.spencercolton.sms.smssockets.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {

    private static Properties settings = new Properties();

    public Settings() {
        File f = new File("config.properties");
        if (f.exists()) {
            try {
                settings.load(new FileInputStream("config.properties"));
            }catch(IOException e) {
                e.printStackTrace();
            }
        } else {
            int port = 8000;
            int dbport = 3306;
            String host = "localhost";
            String db = "database";
            String table = "table";
            String user = "username";
            String password = "password";
            Properties p = new Properties();
            p.setProperty("port", Integer.toString(port));
            p.setProperty("db-host", host);
            p.setProperty("db-db", db);
            p.setProperty("db-table", table);
            p.setProperty("db-username", user);
            p.setProperty("db-password", password);
            p.setProperty("db-port", Integer.toString(dbport));
            try {
                if (f.createNewFile()) {
                    p.store(new FileOutputStream("config.properties"), null);
                    settings.load(new FileInputStream("config.properties"));
                } else {
                    System.out.println("Error: couldn't create properties file.");
                }
            } catch(IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static String get(String s) {
        return settings.getProperty(s);
    }

}
