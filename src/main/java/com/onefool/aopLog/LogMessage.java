package com.onefool.aopLog;

import cn.hutool.core.collection.CollectionUtil;
import com.onefool.strategy.MapStrategy;
import com.onefool.strategy.StrategyMode;
import net.itbaima.robot.event.RobotListenerHandler;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/3/6 23:48
 */
@Component
@Aspect //添加切面
public class LogMessage {

    private static final Logger logger = LoggerFactory.getLogger(LogMessage.class);

    @Pointcut("@annotation(com.onefool.aopLog.Log)")
    public void logMessage() {
    }


    @After("logMessage()")
    public void after(JoinPoint joinPoint){
        logger.info("前置通知日志记录");
        //获取方法的参数
        Object[] args = joinPoint.getArgs();
        List<Object> list = Arrays.asList(args);
        if (CollectionUtil.isNotEmpty(list)){
            list.forEach(l -> {
                    MapStrategy.getStrategy(l.getClass()).method(l.getClass(),joinPoint);
                logger.info("调用aop方法成功=======>");
            });
        }
    }
}
