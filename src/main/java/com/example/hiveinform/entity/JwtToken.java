package com.example.hiveinform.entity;

import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.Date;

@RedisHash("jwt_tokens")
@Data
public class JwtToken implements Serializable {

    @Id
    private String id;

    private String token;

    private String username;

    @Indexed
    private Date expiration;

}