package com.example.demo.event.exception;

/**
 * @author: YangCheng
 * @program:command
 * @title: NotEventListenerException
 * @description:
 * @data 2020/9/8 0008 16:47
 */
public class NotEventListenerException extends RuntimeException{
    public NotEventListenerException(String message) {
        super(message);
    }
}
