package com.example.hiveinform.config;

import com.example.hiveinform.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.nio.charset.StandardCharsets;

//@EnableAspectJAutoProxy
@Configuration
@ComponentScan("com.example")
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private UserService userService ;
    // 本来弃用 但是考虑到实际开发 还是写了
    /**
     * 不配置MessageSource和LocalValidatorFactoryBean，
     * 默认错误消息文件位置：classpath:ValidationMessages.properties
     * An application context delegates the message resolution to a bean with the exact name messageSource.
     *
     * @return
     */
    @Bean
    MessageSource messageSource() {
        ReloadableResourceBundleMessageSource ms = new ReloadableResourceBundleMessageSource();
        ms.setBasename("classpath:errorMessages");
        ms.setDefaultEncoding(StandardCharsets.UTF_8.name()); // utf-8
        ms.setCacheSeconds(20);
        return ms;
    }

    //if we had already extended the WebMvcConfigurerAdapter, to avoid having the custom validator ignored,
    // we'd have to set the validator by overriding the getValidator() method from the parent class.
    // 必须使用override来覆盖getValidator，要不然自定义消息不起作用
    //custom name messages in a properties


    @Bean
    MultipartResolver multipartResolver(){
        StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
        return resolver;
    }



    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        return viewResolver;
    }






}
