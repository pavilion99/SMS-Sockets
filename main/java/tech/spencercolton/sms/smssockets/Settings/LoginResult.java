package tech.spencercolton.sms.smssockets.Settings;

public enum LoginResult {

    SUCCESS(0),
    SESS_EXP(1),
    USER_PASS(2);

    int value;

    LoginResult(int a) {
        this.value = a;
    }

}
