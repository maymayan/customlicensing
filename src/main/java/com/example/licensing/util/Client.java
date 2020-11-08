package com.example.licensing.util;

import com.mashape.unirest.http.Unirest;

public class Client {
    public static void main(String[] args) {

        System.out.println(Unirest.get("http://localhost:8080/authorize?productKey=1&serverId=1").getBody().toString());
    }
}
