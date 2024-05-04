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

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // 所有的当前站点的请求地址，都支持跨域访问。
//                .allowedOriginPatterns("*") // 所有的外部域都可跨域访问。 如果是localhost则很难配置，因为在跨域请求的时候，外部域的解析可能是localhost、127.0.0.1、主机名
//                .allowCredentials(true) // 是否支持跨域用户凭证
//                .allowedMethods("GET","POST") // 当前站点支持的跨域请求类型是什么
//                .maxAge(3600); // 超时时长设置为1小时。 时间单位是秒。
//    }


//    @Bean
//    BeanNameViewResolver beanNameViewResolver() {
//        BeanNameViewResolver viewResolver = new BeanNameViewResolver();
//        viewResolver.setOrder(0); // 数字越小，优先级越高
//        return viewResolver;
//    }

    @Bean
    InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/classes/views/");
//        viewResolver.setSuffix(".html");
//        viewResolver.setViewClass(JstlView.class);
//        viewResolver.setOrder(1);
        return viewResolver;
    }


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new ConfirmInterceptor(userService))
//                .addPathPatterns("/**").order(1);
//    }

    // restful api 弃用 jsp 用html 也可以对视图解析

//    @Bean
//    public SimpleMappingExceptionResolver simpleMappingExceptionResolver(){
//        SimpleMappingExceptionResolver exceptionResolver = new SimpleMappingExceptionResolver();
//        exceptionResolver.setDefaultErrorView("error");
//        exceptionResolver.setExceptionAttribute("exception");
//        Properties mappingsProperties = new Properties();
//        mappingsProperties.put("com.example.demo.exception.StudentException", "student_error");
//        exceptionResolver.setExceptionMappings(mappingsProperties);
//        return exceptionResolver;
//    }
//           弃用


//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new AccessTokenInterceptor())
//                .addPathPatterns("/login/**","/index/**") //后续增加需要拦截的评论和发表系统
//                .excludePathPatterns("/js/**","/img/**");
//    }
//           弃用


}
