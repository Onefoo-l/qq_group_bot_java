package com.onefool.tasks;

import cn.hutool.core.collection.CollectionUtil;
import com.onefool.common.GroupMessageQueue;
import com.onefool.dto.GroupEventPojo;
import com.onefool.service.GroupEventService;
import jakarta.annotation.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author linjiawei
 * @Date 2024/3/7 21:44
 */
@Component
public class ScheduledTasks {

    private static final List<GroupEventPojo> LIST = new CopyOnWriteArrayList<>();

    @Resource
    public GroupEventService groupEventService;

    //1分钟的定时任务
    @Scheduled(fixedDelay = 1000 * 60)
    public void saveGroupLogTasks(){
        while (GroupMessageQueue.size() != 0){
            GroupEventPojo remove = GroupMessageQueue.remove();
            if (remove != null) LIST.add(remove);
        }
        if (CollectionUtil.isNotEmpty(LIST)) {
            groupEventService.saveMessageId(LIST);
        }
        if (!LIST.isEmpty()) {
            //清空
            LIST.subList(0, LIST.size()).clear();
        }
    }
}
