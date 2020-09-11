package com.example.demo.command.core;

import com.example.demo.command.Command;
import com.example.demo.command.interceptor.CommandInterceptor;
import lombok.AccessLevel;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandHub
 * @description: 命令中心
 * @data 2020/9/8 0008 15:45
 */
public class CommandHub {
    @Getter(AccessLevel.PROTECTED)
    private Map<Class<?>, CommandInvocation> executorMap = new HashMap<>(32);

    @Getter(AccessLevel.PROTECTED)
    private List<CommandInterceptor> globalInterceptors = new ArrayList<>();

    @Getter(AccessLevel.PROTECTED)
    private Map<Class<?>, List<CommandInterceptor>> interceptors = new HashMap<>();

    protected CommandInvocation getCommandInvocation(Class<? extends Command> commandClass) {
        return executorMap.get(commandClass);
    }

}
