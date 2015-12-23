package com.dreamspace.superman.model.api;

import java.util.List;

/**
 * Created by Wells on 2015/12/21.
 */
public class QnRes {
    private List<SingleQnRes> tokens;
    private int quantity;

    public List<SingleQnRes> getTokens() {
        return tokens;
    }

    public void setTokens(List<SingleQnRes> tokens) {
        this.tokens = tokens;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
