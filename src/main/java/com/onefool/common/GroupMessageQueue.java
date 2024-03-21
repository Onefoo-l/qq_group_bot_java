package com.onefool.common;

import com.onefool.dto.GroupEventPojo;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Author linjiawei
 * @Date 2024/3/7 21:25
 */
public class GroupMessageQueue {

    private static final LinkedBlockingQueue<GroupEventPojo> GROUP_MESSAGE_QUEUE = new LinkedBlockingQueue<>();

    /**
     * 添加消息
     * @param groupEventPojo
     * @throws InterruptedException
     */
    public static void add(GroupEventPojo groupEventPojo) throws InterruptedException {
        GROUP_MESSAGE_QUEUE.put(groupEventPojo);
    }

    /**
     * 移除消息
     */
    public static GroupEventPojo remove(){
        return GROUP_MESSAGE_QUEUE.poll();
    }

    /**
     * 获取队列长度
     * @return
     */
    public static int size(){
        return GROUP_MESSAGE_QUEUE.size();
    }

}
