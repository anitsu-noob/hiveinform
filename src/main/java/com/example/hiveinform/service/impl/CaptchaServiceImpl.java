package com.example.hiveinform.service.impl;

import com.example.hiveinform.dto.CaptchaDto;
import com.example.hiveinform.service.CaptchaService;
import com.example.hiveinform.service.RedisService;
import com.example.hiveinform.util.UuidUtil;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service("CaptchaService")
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private Producer kaptchaProducer;

    @Autowired
    private RedisService redisService;

    public String getCaptcha() throws IOException {
        String captcha = kaptchaProducer.createText(); // 生成验证码文本
        BufferedImage captchaImage = kaptchaProducer.createImage(captcha);  //创建图片
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(captchaImage, "png", baos);
        byte[] captchaImageBytes = baos.toByteArray();
        String captchaImageBase64 = Base64.getEncoder().encodeToString(captchaImageBytes);// 转换 base64
        UuidUtil uuid = new UuidUtil();
        String random = "" ;
        do {
            random=uuid.get();
        }while (redisService.get(random)!=null);
        redisService.save(random,captcha,180);  // 存储 redis 设置三分钟过期时间
        return "data:image/png;base64,"+captchaImageBase64;
    }

    @Override
    public Boolean verifyCaptcha(CaptchaDto captchaDto) {
        String code = redisService.get(captchaDto.getUuid());  // 提取uuid的code 进行验证
        return code.equals(captchaDto);
    }
}
