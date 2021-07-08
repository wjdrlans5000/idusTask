package com.example.idustask.auth.config;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTokenStore {

    private static final ThreadLocal<String> TOKEN_STORE = new ThreadLocal<>();

    public static void setToken (String token){
        TOKEN_STORE.set(token);
    }

    public static String getToken(){
        return TOKEN_STORE.get();
    }

    public static void removeToken(){
        TOKEN_STORE.remove();
    }
//    private static ConcurrentHashMap<String, String> tokenStore;
//
//    public InMemoryTokenStore(String token) {
//        this.tokenStore = new ConcurrentHashMap<>();
//        this.tokenStore.put(token,token);
//    }
//
//    public static void removeAccessToken(String jwtToken) {
//        tokenStore.replace(jwtToken,"");
//    }
//
//    public static ConcurrentHashMap<String, String> getTokenStore() {
//        return tokenStore;
//    }


}
