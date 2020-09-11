package com.example.demo.event.handler;

import com.example.demo.event.Event;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: YangCheng
 * @program:command
 * @title: EventHandler
 * @description: 监听操作--指定监听类
 * @data 2020/9/8 0008 16:37
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface EventHandler {

    Class<? extends Event>[] eventClass();

    @AliasFor(annotation = Component.class)
    String value() default "";
}
