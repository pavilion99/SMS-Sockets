package tech.spencercolton.sms.smssockets.Protocol;

import com.oracle.javafx.jmx.json.JSONException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import tech.spencercolton.sms.smssockets.Client.Message;
import tech.spencercolton.sms.smssockets.Database.DBConnector;
import tech.spencercolton.sms.smssockets.Settings.LoginResult;
import tech.spencercolton.sms.smssockets.Settings.Settings;
import tech.spencercolton.sms.smssockets.Socket.SMSocket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SMSProtocol extends Thread {

    private Socket s;
    private CState state = CState.WAITING;
    private String username;
    private String sessid;
    private DBConnector conn;
    private int id;

    public SMSProtocol() {
        try (
                PrintWriter out = new PrintWriter(this.s.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
                ) {

            String input = null, output = null;

            do {
                switch(state) {
                    case USR_REREQUESTED:
                    case WAITING:
                        output = "USR";
                        this.state = CState.USR_REQUESTED;
                        break;
                    case PSS_REREQUESTED:
                    case USR_REQUESTED:
                        this.username = input;
                        output = "SES";
                        switch(this.state) {
                            case PSS_REREQUESTED:
                                this.state = CState.RETRY;
                                break;
                            case USR_REQUESTED:
                                this.state = CState.PSS_REQUESTED;
                                break;
                        }
                        this.state = CState.PSS_REQUESTED;
                        break;
                    case RETRY:
                    case PSS_REQUESTED:
                        this.sessid = input;
                        this.conn = new DBConnector(Settings.get("db-host"), Integer.parseInt(Settings.get("db-port")), Settings.get("db-username"), Settings.get("db-password"), Settings.get("db-db"));
                        try {
                            LoginResult e = this.conn.login(this.username, this.sessid);
                            switch(e) {
                                case SUCCESS:
                                    output = "TOR";
                                    this.id = this.conn.getId();
                                    this.state = CState.REQUEST_TYPE;
                                    break;
                                case USER_PASS:
                                    this.state = CState.USR_REREQUESTED;
                                    break;
                                case SESS_EXP:
                                    output = "SEX";
                                    break;
                            }
                        } catch(SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    case REQUEST_TYPE:
                        RequestType g = RequestType.getByString(input);
                        switch(g) {
                            case SEND:
                                String input2 = in.readLine();
                                while(input2 != null) {
                                    try {
                                        JSONObject j = (JSONObject)((new JSONParser()).parse(input2));
                                        int to = (int)j.get("to");
                                        String content = (String)j.get("content");
                                        if(SMSocket.openClients.contains(to)) {
                                            if(Message.messages.containsKey(to)) {
                                                Message.messages.put(to, new ArrayList<Message>());
                                            }
                                            Message.messages.put(to, new Message(this.id, to, content));
                                        }
                                        this.conn.insertMessage(this.id, to, content);
                                    } catch(ParseException|SQLException e) {
                                        System.out.println(input2);
                                        e.printStackTrace();
                                    }
                                }
                                s.close();
                                System.out.println("Client at " + s.getRemoteSocketAddress() + " disconnected.");
                                return;
                            case RECEIVE:
                                while(true) {
                                    if(Message.messages.containsKey(this.id)) {
                                        if(Message.messages.get(this.id).size() != 0) {
                                            for(Message m : Message.messages.get(this.id)) {

                                            }
                                        }
                                    }
                                }
                                return;
                        }
                    default:
                        output = "ERR";
                        break;
                }

                if(output != null) {
                    out.println(output);
                }
            } while((input = in.readLine()) != null);

            s.close();
            System.out.println("Client at " + s.getRemoteSocketAddress() + " disconnected.");

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
