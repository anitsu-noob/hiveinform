package com.example.hiveinform.config;

import com.example.hiveinform.security.JwtAuthFilter;
import com.example.hiveinform.security.SimpleAccessDeniedHandler;
import com.example.hiveinform.security.SimpleAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class WebSecurityConfig {
    private final SimpleAuthenticationEntryPoint authenticationEntryPoint;
    private final SimpleAccessDeniedHandler simpleAccessDeniedHandler;
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable().cors();

        http.authorizeHttpRequests()
                .requestMatchers("/Login/**","/register/**","/article/**","/comment/**","/image/**","/verifyCode/**","/carousel/**")
                .permitAll()
                .anyRequest()
                .authenticated();

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authenticationProvider(authProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling().accessDeniedHandler(simpleAccessDeniedHandler).authenticationEntryPoint(authenticationEntryPoint);
        return http.build();
    }

//    @Autowired
//    private RedisTemplate<JwtToken,String> redisTemplate;
//
//    @Bean
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.authenticationProvider(new RedisAuthenticationProvider(redisTemplate));
//    }    先不删 有点卵用的
}