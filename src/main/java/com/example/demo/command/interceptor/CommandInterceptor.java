package com.example.demo.command.interceptor;

import com.example.demo.command.Command;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandInterceptor
 * @description: 命令拦截
 * @data 2020/9/8 0008 15:53
 */
public interface CommandInterceptor<C extends Command> {


    /**
     * invoke before command execute
     * @param command cmd
     */
    void beforeHandle(C command);

    /**
     * invoke after command execute if
     * @param command cmd
     * @param response response maybe null
     */
    void afterHandle(C command, Object response);

    /**
     * if occur error this method will be invoked </br>
     * warn: don't rethrow ex on interceptor
     * @param command cmd
     * @param ex exception
     */
    default void onError(C command, Throwable ex) {

    }
}
