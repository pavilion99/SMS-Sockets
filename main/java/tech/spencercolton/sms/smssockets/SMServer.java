package tech.spencercolton.sms.smssockets;

import tech.spencercolton.sms.smssockets.Settings.Settings;
import tech.spencercolton.sms.smssockets.Socket.SMSocket;

import java.io.IOException;

public class SMServer {

    public static void main(String[] args) {
        try {
            new SMSocket().open(Integer.parseInt(Settings.get("port")));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
