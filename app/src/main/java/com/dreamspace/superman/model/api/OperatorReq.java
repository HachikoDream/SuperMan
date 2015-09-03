package com.dreamspace.superman.model.api;

/**
 * Created by Wells on 2015/9/3.
 */
public class OperatorReq {
    //confirm rejected
    private String operator;
    private String reason;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
