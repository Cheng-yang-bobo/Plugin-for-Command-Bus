package com.example.demo.command.executor;

import com.example.demo.command.Command;

/**
 * @author: YangCheng
 * @program:command
 * @title: ResponseCommandExecutor
 * @description: 命令调度
 * @data 2020/9/8 0008 15:48
 */
public interface ResponseCommandExecutor<C extends Command> {

    /**
     * @param command 调度
     * @return
     */
    Object doExecute(C command);

}
