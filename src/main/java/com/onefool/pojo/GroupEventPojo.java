package com.onefool.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author linjiawei
 * @Date 2024/3/6 23:49
 */
@TableName("group_event_pojo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class GroupEventPojo implements Serializable {

    /**
     * 聊天记录的Id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 发送人
     */
    @TableField("from_name")
    private String fromName;

    /**
     * 发送人Id
     */
    @TableField("from_id")
    private Long fromId;
    /**
     * 群号
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 群名称
     */
    @TableField("group_name")
    private String groupName;

    /**
     * 消息内容
     */
    @TableField("content")
    private String content;

    /**
     * 消息id(用来表示用户发送消息的次数)
     */
    @TableField("message_id")
    private Integer messageId;

    /**
     * 日志操作
     */
    @TableField("log_operation")
    private String LogOperation;

    /**
     * 发送消息的时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新消息的时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;
}
