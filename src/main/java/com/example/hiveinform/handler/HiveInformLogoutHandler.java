package com.example.hiveinform.handler;

import com.example.hiveinform.service.impl.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
public class HiveInformLogoutHandler implements LogoutHandler {

    @Autowired
    private RedisTemplate redisTemplate ;

    @Autowired
    private JwtService jwtService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        redisTemplate.delete(jwtService.extractUsername(request.getHeader("Authorization")));
        //删除session id 等等 由于现在分布式 session 没引入 就算求 懒了 暂时不想写
    }
}
