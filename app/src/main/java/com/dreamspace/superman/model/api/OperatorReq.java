package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class OperatorReq {
    //opeator:"confirmed" or "rejected"
    private String opeator;
    private String reason;

    public String getOpeator() {
        return opeator;
    }

    public void setOpeator(String opeator) {
        this.opeator = opeator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
