package com.example.demo.command.interceptor;

import com.example.demo.command.Command;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: YangCheng
 * @program:command
 * @title: Interceptor
 * @description: 拦截注解
 * @data 2020/9/8 0008 16:14
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Interceptor {
    Class<? extends Command>[] commands() default {};

    @AliasFor(annotation = Component.class)
    String value() default "";
}
