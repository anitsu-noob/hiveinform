package com.example.hiveinform.security;

import com.example.hiveinform.entity.JwtToken;
import com.example.hiveinform.service.UserService;
import com.example.hiveinform.service.impl.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

public class RedisAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService ;
    @Autowired
    private JwtService jwtService ;

    private final RedisTemplate<JwtToken, String> redisTemplate;
    private final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

    public RedisAuthenticationProvider(RedisTemplate<JwtToken, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = (String) authentication.getCredentials();
        String username = null;

        try {
            username= jwtService.extractUsername(token);

        } catch (RuntimeException e) {
            throw new RuntimeException("Invalid JWT");
        }

        if (username == null) {
            throw new RuntimeException("Invalid JWT");
        }

        String key = "jwt:" + username;
        String storedToken = redisTemplate.opsForValue().get(stringRedisSerializer.serialize(key));

        if (!token.equals(storedToken)) {
            throw new RuntimeException("Invalid JWT");
        }

        UserDetails userDetails = userService.getByUsername(username);
        return new RedisAuthenticationToken(token, userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return RedisAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
