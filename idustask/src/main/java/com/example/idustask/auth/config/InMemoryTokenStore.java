package com.example.idustask.auth.config;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenStore {

    private static ConcurrentHashMap<String, String> tokenStore;

    public InMemoryTokenStore(String token) {
        this.tokenStore = new ConcurrentHashMap<>();
        this.tokenStore.put(token,token);
    }

    public void removeAccessToken(String token) {
    }

    public static ConcurrentHashMap<String, String> getTokenStore() {
        return tokenStore;
    }


}
