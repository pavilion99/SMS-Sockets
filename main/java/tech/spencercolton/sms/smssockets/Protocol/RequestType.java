package tech.spencercolton.sms.smssockets.Protocol;

public enum RequestType {

    SEND("SND"),
    RECEIVE("RCV");

    String type;

    RequestType(String s) {
        this.type = s;
    }

    public static RequestType getByString(String s) {
        switch(s) {
            case "SND":
                return SEND;
            case "RCV":
                return RECEIVE;
            default:
                return null;
        }
    }

}
