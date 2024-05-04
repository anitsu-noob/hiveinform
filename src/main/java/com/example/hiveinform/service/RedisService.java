package com.example.hiveinform.service;

public interface RedisService {
    void save(String key, Object value, int expirationTime);

    String get(String key);
    void delete(String key);
}
