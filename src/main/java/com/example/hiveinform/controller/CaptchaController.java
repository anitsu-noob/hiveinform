package com.example.hiveinform.controller;

import com.example.hiveinform.dto.CaptchaDto;
import com.example.hiveinform.service.CaptchaService;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.util.UuidUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
@RequestMapping("/verifyCode")
public class CaptchaController {

    
    @Autowired
    private CaptchaService captchaService;

    @Autowired
    private RedisService redisService;

    @GetMapping("")
    public String getCaptchaImage() throws IOException {

        // redis 存储 captcha 的验证文本  时效 3分钟
        return captchaService.getCaptcha();

    }

    @PostMapping("")
    public String VerifyCode(@RequestBody CaptchaDto captchaDto) throws IOException {
        String result = captchaService.verifyCaptcha(captchaDto) ? "T" : "F" ;

        return result;
    }
}
