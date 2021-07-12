package com.example.idustask.auth.config;


import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTokenStore {


    private ConcurrentHashMap<String, String> tokenStore = new ConcurrentHashMap<>();

    public void setTokenStore(String token) {
        this.tokenStore.put(token,token);
    }

    public void removeAccessToken(String token) {
        this.tokenStore.remove(token);
    }

    public String getTokenStore(String token) {
        return this.tokenStore.get(token);
    }

}
