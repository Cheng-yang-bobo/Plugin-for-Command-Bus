package com.example.demo.event.listener;

import com.example.demo.event.Event;
import org.springframework.core.Ordered;

import javax.validation.constraints.NotNull;

/**
 * @author: YangCheng
 * @program:command
 * @title: EnhancerEventListener
 * @description: 可以增加event的监听器，增加之后的event会继续向下传递,增强监听器
 * @data 2020/9/8 0008 16:30
 */
public interface EnhancerEventListener <E extends Event,EE extends E>extends Ordered,Comparable<EnhancerEventListener>{


    int ORDER_1 = 1;

    int ORDER_2 = 2;

    int ORDER_3 = 3;

    int ORDER_4 = 4;

    int ORDER_5 = 5;

    int ORDER_6 = 6;

    int ORDER_7 = 7;

    int ORDER_8 = 8;

    /**
     * 处理event并增加event，增强之后的event必须是给定event的子类
     * @param event 接收到的event
     * @return 增强之后的event返回空则不会继续向下传递
     */
    EE enhancerExecute(E event);

    @Override
    default int compareTo(@NotNull EnhancerEventListener o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }
}
