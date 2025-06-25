package com.restaurant.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenPwd {
    public static void main(String[] args) {
        String raw = "123456";                  // 你想设的新密码
        String hash = new BCryptPasswordEncoder().encode(raw);
        System.out.println(hash);
    }
}