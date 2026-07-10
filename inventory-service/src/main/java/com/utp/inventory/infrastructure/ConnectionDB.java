package com.utp.inventory.infrastructure;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionDB {

    private static String env(String key, String def) {
        String v = System.getenv(key);
        return (v == null || v.isEmpty()) ? def : v;
    }

    private static final String HOST = env("DB_HOST", "localhost");
    private static final String PORT = env("DB_PORT", "3306");
    private static final String NAME = env("DB_NAME", "inventario_ventas");
    private static final String USER = env("DB_USER", "app");
    private static final String PASS = env("DB_PASSWORD", "app");
    private static final String URL =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + NAME
        + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
        + "&useUnicode=true&characterEncoding=UTF-8"
        + "&zeroDateTimeBehavior=CONVERT_TO_NULL";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Driver MySQL no encontrado", e);
        }
    }

    public static Connection get() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
