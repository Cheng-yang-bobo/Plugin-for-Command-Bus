package com.example.demo.command.core;

import com.example.demo.command.Command;
import com.example.demo.command.CommandBus;
import com.example.demo.command.exception.CommandExecutorException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandBusImpl
 * @description: 分发器实现
 * @data 2020/9/8 0008 15:44
 */
@Component
public class CommandBusImpl implements CommandBus {

    @Resource
    private CommandHub commandHub;

    @Override
    public <C extends Command, T> T fire(@NotNull C command) {
        CommandInvocation<C> commandInvocation = commandHub.getCommandInvocation(command.getClass());
        if (commandInvocation == null) {
            throw new CommandExecutorException(String.format("No commandExecutor for %s", command.getClass()));
        }
        return (T) commandInvocation.doExecute(command);
    }
}
