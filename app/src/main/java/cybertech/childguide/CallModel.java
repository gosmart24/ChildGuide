package cybertech.childguide;

/**
 * stagent24@gmail.com
 * Created by CyberTech on 9/23/2017.
 */
public class CallModel {
    String callName;
    String callNo;
    String callDate;
    String callType;
    String callDuration;

    public CallModel(String callName, String callNo, String callDate, String callType, String callDuration) {
        this.callName = callName;
        this.callNo = callNo;
        this.callDate = callDate;
        this.callType = callType;
        this.callDuration = callDuration;

    }

    public CallModel() {
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallNo() {
        return callNo;
    }

    public void setCallNo(String callNo) {
        this.callNo = callNo;
    }

    public String getCallDate() {
        return callDate;
    }

    public void setCallDate(String callDate) {
        this.callDate = callDate;
    }

}
