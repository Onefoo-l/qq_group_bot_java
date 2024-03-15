package com.onefool.strategy;

import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author linjiawei
 * @Date 2024/3/7 0:20
 */
public class MapStrategy {

    private static final Logger logger = LoggerFactory.getLogger(MapStrategy.class);

    private static final Map<Class<?>,StrategyMode> STRATEGY_MAP = new ConcurrentHashMap<>();

    static {
        STRATEGY_MAP.put(GroupMessageEvent.class,new GroupMessageEventStrategy());
        STRATEGY_MAP.put(NudgeEvent.class,new NudgeEventStrategy());
    }

    public static StrategyMode getStrategy(Class<?> clazz){
        return STRATEGY_MAP.get(clazz);
    }
}
