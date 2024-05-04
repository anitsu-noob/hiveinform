package com.example.hiveinform.config;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha kaptcha() {
        DefaultKaptcha kaptcha = new DefaultKaptcha();

        Properties properties = new Properties();
        properties.put("kaptcha.image.width", "200");
        properties.put("kaptcha.image.height", "60");
        properties.put("kaptcha.textproducer.char.string", "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"); // 允许的字符集
        properties.put("kaptcha.textproducer.char.length", "6"); // 验证码长度
        properties.put("kaptcha.textproducer.font.names", "Arial"); // 字体样式
//        properties.put("kaptcha.noise.impl", "com.google.code.kaptcha.impl.NoNoise"); // 禁用默认干扰线
//
//

        Config config = new Config(properties);
        kaptcha.setConfig(config);

        return kaptcha;
    }
}
