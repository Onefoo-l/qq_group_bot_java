package com.onefool.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.onefool.mapper.GroupEventMapper;
import com.onefool.dto.GroupEventPojo;
import com.onefool.service.GroupEventService;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/3/7 21:20
 */
@Service
public class GroupEventServiceImpl extends ServiceImpl<GroupEventMapper, GroupEventPojo> implements GroupEventService {

    @Resource
    public RedisTemplate<String, String> redisTemplate;


    @Override
    @Transactional
    public void saveMessageId(List<GroupEventPojo> list) {
        list.forEach(l -> {
            Integer messageId = l.getMessageId();
            String strMessageId = (messageId == null) ? "0" : String.valueOf(messageId);
            Long formId = l.getFromId();
            String redisMessageId = redisTemplate.opsForValue().get(String.valueOf(formId));
            if (StringUtils.isEmpty(redisMessageId)) {
                redisTemplate.opsForValue().set(String.valueOf(formId), strMessageId);
            } else {
                int i = Integer.parseInt(redisMessageId);
                i = i++;
                redisTemplate.opsForValue().set(String.valueOf(formId), String.valueOf(i));
            }
        });
        this.saveBatch(list);
    }
}
