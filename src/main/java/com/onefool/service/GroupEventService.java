package com.onefool.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.onefool.pojo.GroupEventPojo;

import java.util.List;

/**
 * @Author linjiawei
 * @Date 2024/3/7 21:20
 */
public interface GroupEventService extends IService<GroupEventPojo> {

    void saveMessageId(List<GroupEventPojo> groupEventPojoList);
}
