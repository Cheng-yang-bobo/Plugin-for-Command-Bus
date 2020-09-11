package com.example.demo.command.annotation;

import com.example.demo.command.Command;
import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandActuator
 * @description: 命令执行器
 * @data 2020/9/8 0008 16:18
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface CommandActuator {


    @AliasFor(annotation = Component.class) String value() default "";

    Class<? extends Command> commandClass();
}
