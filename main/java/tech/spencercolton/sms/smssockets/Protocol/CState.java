package tech.spencercolton.sms.smssockets.Protocol;

public enum CState {

    WAITING(0),
    USR_REREQUESTED(0),
    USR_REQUESTED(1),
    PSS_REREQUESTED(1),
    PSS_REQUESTED(2),
    RETRY(2),
    REQUEST_TYPE(3);

    int val;

    CState(int a) {
        this.val = a;
    }

}
