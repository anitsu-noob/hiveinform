package com.example.hiveinform.security;

import com.example.hiveinform.entity.JwtToken;
import com.example.hiveinform.repository.JwtTokenRepository;
import com.example.hiveinform.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {


    @Autowired
    private RedisTemplate redisTemplate;

    private final JwtTokenRepository  jwtTokenRepository ;

    private final JwtService jwtService;

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization")  == null ? request.getParameter("token") : request.getHeader("Authorization");
        final String jwt;
        final String username;


        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        username = jwtService.extractUsername(jwt);
        String jwtToken= (String) redisTemplate.opsForValue().get(username);
        // 后续更改 jwt  jwttoken 和 jwt 是否相同
        if (jwtToken != null ) {
            if (username.equals(jwtService.extractUsername(jwtToken)))
            {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                userDetails.getAuthorities();
                RedisAuthenticationToken authToken = new RedisAuthenticationToken(jwt,userDetails,userDetails.getAuthorities());   // redis 读出 数据 如果没有 那就是假冒的 过时的
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );   //  生成 author 信息
                SecurityContextHolder.getContext().setAuthentication(authToken);  // 装配信息
                filterChain.doFilter(request, response);
            }
            else {
                throw new IllegalArgumentException("冒用的jwt");
            }
        }
        else
        {
            throw new IllegalArgumentException("冒用的jwt");
        }
        // jwt redis 认证 如果在redis存在 那就是有效jwt 如果不存咋 证明 这个jwt是被人截取了 那么应该记录 ip和mac 地址
        // 如果 ip mac 重复出现 则加入到白名单 后续进行拦截 或 向用户 发送冻结请求
        // 但在登录时 就已经将token 记录到了redis 里面
//        else {
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
//                userDetails.getAuthorities();
//                if (jwtService.isTokenValid(jwt, userDetails)) {
//                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                            userDetails,
//                            null,
//                            userDetails.getAuthorities()
//                    );
//                    authToken.setDetails(
//                            new WebAuthenticationDetailsSource().buildDetails(request)
//                    );
//                    SecurityContextHolder.getContext().setAuthentication(authToken);
//                }
//
//                filterChain.doFilter(request, response);
//            }
//        }   后续更改 此方法 为初期 jwt 常规 认证流程

    }
}