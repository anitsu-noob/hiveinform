package com.example.hiveinform.util;

import java.util.UUID;

public class UuidUtil {
    public String get()
    {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
