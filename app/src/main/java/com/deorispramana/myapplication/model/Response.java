package com.deorispramana.myapplication.model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("data")
    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
