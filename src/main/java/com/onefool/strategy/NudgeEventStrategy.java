package com.onefool.strategy;

import com.onefool.aopLog.Log;
import com.onefool.common.GroupMessageQueue;
import com.onefool.pojo.GroupEventPojo;
import net.itbaima.robot.event.RobotListenerHandler;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.event.events.NudgeEvent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @Author linjiawei
 * @Date 2024/3/13 16:46
 */
@Component
public class NudgeEventStrategy implements StrategyMode {

    private static final Logger logger = LoggerFactory.getLogger(NudgeEventStrategy.class);

    @Autowired
    public RedisTemplate<String, String> redisTemplate;

    @Override
    public void method(Class<?> clazz, JoinPoint joinPoint) {
        var eventPojo = new GroupEventPojo();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        var logAnnotation = method.getAnnotation(Log.class);
        String logValue = logAnnotation.value();

        RobotListenerHandler annotation = method.getAnnotation(RobotListenerHandler.class);
        long[] longs = annotation.contactId();
        long contactFirstId = longs[0];

        try {
            Object o = clazz.newInstance();
            NudgeEvent event = (NudgeEvent) o;
            UserOrBot from = event.getFrom();
            long fromId = from.getId();
            String fromNick = from.getNick();
            String fromIdRedis = redisTemplate.opsForValue().get(String.valueOf(fromId));
            if (StringUtils.isEmpty(fromIdRedis)) redisTemplate.opsForValue().set(String.valueOf(fromId),"0");

            eventPojo.setGroupName(null);
            eventPojo.setGroupId(contactFirstId);
            eventPojo.setContent("动作::戳一戳");
            eventPojo.setFromId(fromId);
            eventPojo.setFromName(fromNick);
            eventPojo.setLogOperation(logValue);
            eventPojo.setCreateTime(LocalDateTime.now());
            eventPojo.setUpdateTime(LocalDateTime.now());
            GroupMessageQueue.add(eventPojo);
        } catch (Exception e) {
            logger.error("实例化失败=======>",e);
        }
    }
}
