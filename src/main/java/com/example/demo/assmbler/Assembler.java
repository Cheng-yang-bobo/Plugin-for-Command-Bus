package com.example.demo.assmbler;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author: YangCheng
 * @program:command
 * @title: Assembler
 * @description:
 * @data 2020/9/8 0008 17:10
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Assembler {

    @AliasFor(annotation = Component.class)
    String value() default "";
}
