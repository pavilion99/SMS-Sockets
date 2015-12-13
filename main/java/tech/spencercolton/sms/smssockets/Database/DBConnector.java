package tech.spencercolton.sms.smssockets.Database;

import tech.spencercolton.sms.smssockets.Settings.LoginResult;
import tech.spencercolton.sms.smssockets.Settings.Settings;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class DBConnector {

    private Connection con;
    private int id;

    public DBConnector(String host, int port, String username, String password, String database) {
        String connUrl = "jdbc:mysql://" + host + ":" + port + "/" + database;
        try {
            this.con = DriverManager.getConnection(connUrl, username, password);
        } catch(SQLException e) {
            e.printStackTrace();
            con = null;
        }
    }

    public LoginResult login(String username, String sessid) throws SQLException {
        Statement s = this.con.createStatement();
        ResultSet r = s.executeQuery("SELECT * FROM `" + Settings.get("table") + "` WHERE `username`='" + username + "' AND `session`='" + sessid + "'");
        Date d = new Date();
        if(r.next()) {
            int diff = r.getDate("sess_date").compareTo(d);

            if(diff >= 1800) {
                return LoginResult.SESS_EXP;
            } else {
                this.id = r.getInt("id");
                return LoginResult.SUCCESS;
            }
        } else {
            return LoginResult.USER_PASS;
        }
    }

    public int getId() {
        return this.id;
    }

    public void insertMessage(int from, int to, String content) throws SQLException {
        PreparedStatement s = this.con.prepareStatement("INSERT INTO `messages` (`u_to`,`u_from`,`sent`,`content`) VALUES (?, ?, CURRENT_TIMESTAMP, ?)");
        s.setInt(1, to);
        s.setInt(2, from);
        s.setString(3, content);
        s.executeUpdate();
    }

}
