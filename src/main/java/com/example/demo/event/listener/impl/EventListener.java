package com.example.demo.event.listener.impl;

import com.example.demo.event.Event;
import com.example.demo.event.listener.EnhancerEventListener;

/**
 * @author: YangCheng
 * @program:command
 * @title: EventListener
 * @description: 事件监听
 * @data 2020/9/8 0008 16:32
 */
public interface EventListener<E extends Event> extends EnhancerEventListener<E,E>{

    /**
     * invoke event
     * @param event event can't use {@link Event} as parameter
     */
    void execute(E event);

    @Override
    default E enhancerExecute(E event) {
        execute(event);
        return event;
    }

    @Override
     default int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
