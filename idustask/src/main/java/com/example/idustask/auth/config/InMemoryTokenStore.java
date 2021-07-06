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

    public static void removeAccessToken(String jwtToken) {
        tokenStore.replace(jwtToken,"");
    }

    public static ConcurrentHashMap<String, String> getTokenStore() {
        return tokenStore;
    }


}
