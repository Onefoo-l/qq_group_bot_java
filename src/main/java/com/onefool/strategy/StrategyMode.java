package com.onefool.strategy;

import org.aspectj.lang.JoinPoint;

/**
 * @Author linjiawei
 * @Date 2024/3/7 0:22
 */
public interface StrategyMode {

    void method(Class<?> clazz, JoinPoint joinPoint);
}
