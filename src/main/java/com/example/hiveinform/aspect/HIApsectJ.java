package com.example.hiveinform.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Slf4j
@Component
public class HIApsectJ {

    // 针对 service 的 不针对 security
    @Pointcut("execution(* com.example.hiveinform.service.*.*(..))")
    public void HivePointcut()
    {

    }

    @Before("HivePointcut()")
    public void before(JoinPoint joinPoint)
    {
//        System.out.println("Before+++"+joinPoint.getSignature().getName());
        log.info("Before+++",joinPoint.getSignature().getName());
    }

    @AfterReturning("HivePointcut()")
    public void afterReturning(JoinPoint joinPoint)
    {
//        System.out.println("afterReturning+++"+joinPoint.toShortString());
        log.info("afterReturning+++"+joinPoint.getSignature().getName());
    }

    @Around("HivePointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {


        Object proceed = proceedingJoinPoint.proceed();
        if (proceed == null)
            proceed = "";
        log.info("Around+++ " + proceed.toString());

//        System.out.println("Around+++"+proceed.toString());
        return proceed;
    }

    @After("HivePointcut()")
    public void after(JoinPoint joinPoint)
    {
//        System.out.println("the end +++"+joinPoint.getSourceLocation());
        log.info("the end +++"+joinPoint.getSignature().getName());
    }

    @AfterThrowing(value = "HivePointcut()",throwing = "e")
    public void afterThrowing(Exception e)
    {
//        System.out.println("Exception+++"+e);
        log.error(e.toString());
    }
}
