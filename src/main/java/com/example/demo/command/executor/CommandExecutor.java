package com.example.demo.command.executor;

import com.example.demo.command.Command;

import javax.validation.constraints.NotNull;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandExecutor
 * @description: 执行
 * @data 2020/9/8 0008 16:00
 */
@FunctionalInterface
public interface CommandExecutor<C extends Command> {

    void execute(@NotNull C command);
}
