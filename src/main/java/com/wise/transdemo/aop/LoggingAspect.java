package com.wise.transdemo.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 定义切入点，拦截所有控制器的方法
    @Pointcut("execution(* com.wise.transdemo.controller.*.*(..))")
    public void controllerMethods() {}

    // 在方法执行前打印请求参数
    @Before("controllerMethods()")
    public void before(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            logger.info("请求URL: {}", request.getRequestURL().toString());
            logger.info("请求方法: {}", request.getMethod());
            logger.info("请求参数: {}", Arrays.toString(joinPoint.getArgs()));
        }
    }

    // 在方法执行后打印返回数据
    @Around("controllerMethods()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        long endTime;
        long executionTime;
        try {
            Object result = joinPoint.proceed();
            endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            try {
                String resultJson = objectMapper.writeValueAsString(result);
                logger.info("返回数据: {}", resultJson);
                logger.info("接口执行时间: {} 毫秒", executionTime);
            } catch (Exception e) {
                logger.error("序列化返回数据失败: {}", e.getMessage());
            }
            return result;
        } catch (Throwable throwable) {
            endTime = System.currentTimeMillis();
            executionTime = endTime - startTime;
            logger.error("接口执行出错，执行时间: {} 毫秒，错误信息: {}", executionTime, throwable.getMessage(), throwable);
            throw throwable;
        }
    }
}