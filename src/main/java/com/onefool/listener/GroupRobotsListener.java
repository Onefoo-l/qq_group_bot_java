package com.onefool.listener;

import com.onefool.utils.HttpClientGptApi;
import lombok.extern.java.Log;
import net.itbaima.robot.event.RobotListener;
import net.itbaima.robot.event.RobotListenerHandler;
import net.itbaima.robot.listener.MessageListener;

import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.contact.UserOrBot;
import net.mamoe.mirai.event.events.*;

import net.mamoe.mirai.message.action.Nudge;
import net.mamoe.mirai.message.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;


import java.io.File;
import java.util.Map;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author linjiawei
 * @Date 2024/3/6 23:18
 */
@RobotListener //添加监听器类
public class GroupRobotsListener extends MessageListener{

    private static final Logger logger = LoggerFactory.getLogger(GroupRobotsListener.class);

    private static final Map<Long, Integer> MESSAGE_COUNT = new ConcurrentHashMap<>();

    @Autowired
    Bot bot;

//    @RobotListenerHandler(contactId = {704457989}) //添加监听器的处理方法   980433181   704457989
//    public void RobotsListenerl(GroupMessageEvent event) {
//        logger.info("GroupMessageEvent============>");
//        Group group = event.getGroup();
//        group.sendMessage("lkkkkkkkkkkkk");
//        String s = event.getMessage().contentToString();
//        System.out.println(s);
////        System.out.println(senderString);
////        logger.info("消息为============>" + senderString);
//    }

//    @RobotListenerHandler
//    public void RobotsListenerl(FriendMessageEvent event) {
//        logger.info("FriendMessageEvent============>");
//        event.getSender()
//                .sendMessage("ss");
//        String s = event.getMessage().contentToString();
//        System.out.println(s);
////        System.out.println(senderString);
////        logger.info("消息为============>" + senderString);
//    }

    @RobotListenerHandler(contactId = {855344385})  //添加监听器的处理方法   855344385
    public void RobotsListener(GroupMessageEvent event) {
        //获取qq号码
        long senderId = event.getSender().getId();
        MessageChain messageChain = event.getMessage();
        String senderString = messageChain.contentToString();
        logger.info("消息为============>" + senderString);
        extracted(event, senderString, messageChain, senderId);
    }

    private void extracted(GroupMessageEvent event, String senderString, MessageChain messageChain, long senderId) {
        if (StringUtils.isEmpty(senderString)) return;
        if (senderString.contains("@980433181")) {  //980433181  974362219
            for (int i = 0; i < messageChain.size(); i++) {
                if (messageChain.get(i) instanceof At) {
                    String contentStr = messageChain.get(i + 1).contentToString();
                    logger.info("截取后的数据====>" +  contentStr);
                    if (contentStr.equals(" 玩法大全")) {
                        event.getGroup().sendMessage("请发送@ + 我的昵称，后面跟着问题，我将会调用CharGpt的API为您解答 " +
                                "又或者您可以自行探索功能使用");
                    } else {
                        logger.info("异步调用数据=======>" + contentStr);
                        var future = CompletableFuture.supplyAsync(() -> {
                            var response = HttpClientGptApi.openAi(contentStr);
                            return response;
                        });
                        At at = new At(senderId);
//                            event.getGroup().sendMessage(at);
                            event.getGroup().sendMessage("正在查询中稍后!!");

                        future.thenAccept(value -> {
                            event.getGroup().sendMessage(value);
                            logger.info("返回给前台数据完成========>");
                        });
                    }
                }
            }
        } else {
            String path = "E:\\360downloads\\foot";
            logger.info("进入到路径=======》");
            if (senderString.contains("黑丝")) {
                var f = new File(path + "\\foot" + (new Random().nextInt(2) + 1) + ".jpg");
                Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), f);
                event.getGroup().sendMessage(image);
            } else if (senderString.contains("白丝")) {
                event.getGroup().sendMessage("暂时没有这个资源");
            } else if (senderString.contains("玉足")) {
                var f = new File(path + "\\foot" + (new Random().nextInt(4) + 1) + ".jpg");
                Image image = net.mamoe.mirai.contact.Contact.uploadImage(event.getSubject(), f);
                event.getGroup().sendMessage(image);
            }
            }
        }


    //机器人被戳事件
//    @RobotListenerHandler(contactId = {704457989})  //添加监听器的处理方法 855344385
//    @Log("群聊天记录")
    @RobotListenerHandler(contactId = {704457989})
    public void RobotsListenerNudgeEvent(NudgeEvent event) {
        logger.info("NudgeEvent======>");
        long id = event.getFrom().getId();
        if (MESSAGE_COUNT.get(id) == 0 || MESSAGE_COUNT.get(id) == null) MESSAGE_COUNT.put(id, 0);
        else MESSAGE_COUNT.put(id, MESSAGE_COUNT.get(id) + 1);

        UserOrBot userOrBot = event.component1();
        //群成员的昵称
        String nick = userOrBot.getNick();
        //发送消息
        if (MESSAGE_COUNT.get(id) == 3) {
            event.getSubject().sendMessage(nick + "爱达咩");
        } else if (MESSAGE_COUNT.get(id) == 6) {
            event.getSubject().sendMessage(nick + "，好了，别戳了，再戳人就要傻了");
        } else if (MESSAGE_COUNT.get(id) == 10) {
            event.getSubject().sendMessage("反弹");
            Nudge nudge = userOrBot.nudge();
            nudge.sendTo(event.getSubject());
            MESSAGE_COUNT.put(id, 0);
        } else {
            event.getSubject().sendMessage(nick + " 达咩");
        }
    }
}
