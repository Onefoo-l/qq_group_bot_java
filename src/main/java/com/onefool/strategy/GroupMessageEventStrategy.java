package com.onefool.strategy;

import com.onefool.aopLog.Log;
import com.onefool.common.GroupMessageQueue;
import com.onefool.pojo.GroupEventPojo;
import jakarta.annotation.Resource;
import net.itbaima.robot.event.RobotListenerHandler;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.*;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Method;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @Author linjiawei
 * @Date 2024/3/7 0:23
 */
public class GroupMessageEventStrategy implements StrategyMode{

    private static final Logger logger = LoggerFactory.getLogger(GroupMessageEventStrategy.class);

    @Override
    public void method(Class<?> clazz, JoinPoint joinPoint)  {
        var groupEventPojo = new GroupEventPojo();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //@Log的注解 日志记录名称
        Log logAnnotation = method.getAnnotation(Log.class);
        String logValue = logAnnotation.value();
        try {
            //创建对象
            Object o = clazz.newInstance();
            //强制装换
            GroupMessageEvent event = (GroupMessageEvent) o;
            MessageChain message = event.getMessage();
            OnlineMessageSource.Incoming.FromGroup source = event.getSource();
            int time = source.getTime();
            //转换时间
            LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
            //发送者名称
            String senderName = event.getSenderName();
            //发送者id
            long senderId = event.getSender().getId();
            //文本内容
            String s = message.contentToString();
            //群
            Group group = event.getGroup();
            //群id
            long groupId = group.getId();
            //群名称
            String groupName = group.getName();

            groupEventPojo.setLogOperation(logValue);
            groupEventPojo.setFromName(senderName);
            groupEventPojo.setFromId(senderId);
            groupEventPojo.setContent(s);
            groupEventPojo.setGroupId(groupId);
            groupEventPojo.setGroupName(groupName);
            groupEventPojo.setCreateTime(dateTime);
            groupEventPojo.setUpdateTime(dateTime);
            logger.info("一条群消息=====>{}",groupEventPojo);
            GroupMessageQueue.add(groupEventPojo);
        } catch (Exception e)  {
            logger.error(e.getCause().getLocalizedMessage());
        }
    }

}
