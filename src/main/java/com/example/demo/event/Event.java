package com.example.demo.event;

import javax.validation.constraints.NotNull;

/**
 * @author: YangCheng
 * @program:command
 * @title: Event
 * @description: 事件抽象
 * @data 2020/9/8 0008 16:26
 */
public abstract class Event {

    /**
     * cast to given class type
     * @param eventClass return event type class
     * @param <E> generic type
     * @return this
     */
    public <E extends Event> E unwrap(@NotNull Class<E> eventClass) {
        return eventClass.cast(this);
    }
}
