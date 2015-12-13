package tech.spencercolton.sms.smssockets.Client;

import java.util.HashMap;

public class Message {

    public static HashMap<Integer, Message> messages = new HashMap<>();

    private int from;
    private int to;
    private String content;

    public Message(int from, int to, String content) {
        this.from = from;
        this.to = to;
        this.content = content;

        messages.put(this.to, this);
    }

    public int getFrom() {
        return this.from;
    }

    public int getTo() {
        return this.to;
    }

    public String getContent() {
        return this.content;
    }

}
