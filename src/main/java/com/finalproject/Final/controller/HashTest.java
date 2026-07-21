package com.finalproject.Final.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//sai
//for setting password hash for accounts for testing

public class HashTest {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        String hash = encoder.encode("Tt1234567");

        System.out.println(hash);
    }
}
