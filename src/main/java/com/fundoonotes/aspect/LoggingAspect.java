package com.fundoonotes.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.fundoonotes.service..*(..))")
    public void logBeforeServiceMethod(JoinPoint joinPoint) {
        log.info("Executing service method: {}", joinPoint.getSignature().getName());
    }

    @Around("execution(* com.fundoonotes.service..*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        log.info("Method {} executed in {} ms",
                joinPoint.getSignature().getName(),
                (end - start));
        return result;
    }

    @AfterThrowing(
            pointcut = "execution(* com.fundoonotes.service..*(..))",
            throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method: {} | Message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}
