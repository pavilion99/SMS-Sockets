package tech.spencercolton.sms.smssockets.Socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

public class SMSocket {

    private ServerSocket s;

    public static List<Integer> openClients = new ArrayList<>();

    public void open(int port) throws IOException {
        this.s = new ServerSocket(port);

        System.out.println("SMS server bound on port " + port);
        System.out.println("Waiting for clients...");
        while(true) {

        }
    }

}
