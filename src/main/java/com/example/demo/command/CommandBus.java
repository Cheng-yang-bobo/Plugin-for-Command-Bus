package com.example.demo.command;

import javax.validation.constraints.NotNull;

/**
 * @author: YangCheng
 * @program:command
 * @title: Command
 * @description: 命令分发器
 * @data 2020/9/8 0008 15:40
 */
public interface CommandBus {

    /**
     * if no commandExecutor will throw {@link com.example.demo.command.exception.CommandExecutorException},
     * if The actual return type not matcher generic type T maybe throw {@link ClassCastException}
     *
     * @param command execute command
     * @param <C>
     * @return
     */
    <C extends Command,T> T fire(@NotNull C command);
}
