package com.dreamspace.superman.model.api;

import com.dreamspace.superman.model.Catalog;

import java.util.List;

/**
 * Created by Wells on 2016/2/22.
 */
public class CatalogRes {
    private List<Catalog> catalogs;
    private String status;
    private int quantity;
    public static final String SUCCESS_STATE = "success";

    public List<Catalog> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<Catalog> catalogs) {
        this.catalogs = catalogs;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
