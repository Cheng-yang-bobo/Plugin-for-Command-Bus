package com.example.demo.command.core;

import com.example.demo.command.Command;
import com.example.demo.command.executor.CommandExecutor;
import com.example.demo.command.executor.ResponseCommandExecutor;
import com.example.demo.command.interceptor.CommandInterceptor;
import lombok.AccessLevel;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandInvocation
 * @description: 命令通信
 * @data 2020/9/8 0008 15:47
 */
@Slf4j
public class CommandInvocation<C extends Command> implements ResponseCommandExecutor<C> {


    private ResponseCommandExecutor<C> executor;

    private CommandExecutor<C> clearExecutor;

    @Setter(AccessLevel.PROTECTED)
    private List<CommandInterceptor<C>> interceptors;

    CommandInvocation(ResponseCommandExecutor<C> executor) {
        this.executor = executor;
    }

    public CommandInvocation(CommandExecutor<C> executor) {
        this.clearExecutor = executor;
    }

    protected Object executeInterval(C command) {
        if (Objects.nonNull(executor)) {
            return executor.doExecute(command);
        } else if (Objects.nonNull(clearExecutor)) {
            clearExecutor.execute(command);
            return null;
        }
        throw new IllegalArgumentException("Command executor is null");
    }


    private void beforeIntercept(C command) {
        for (CommandInterceptor<C> interceptor : interceptors) {
            interceptor.beforeHandle(command);
        }
    }

    private void afterIntercept(C command, Object result) {
        for (CommandInterceptor<C> interceptor : interceptors) {
            interceptor.afterHandle(command, result);
        }
    }

    private void onError(C command, Throwable exception) {
        for (CommandInterceptor<C> interceptor : interceptors) {
            interceptor.onError(command, exception);
        }
    }

    @Override
    public Object doExecute(C command) {
        beforeIntercept(command);
        if (log.isDebugEnabled()) {
            log.debug("Pears invoke {} start", command.getClass().getName());
            for (CommandInterceptor<C> interceptor : interceptors) {
                log.debug("command invoke interceptor {}", interceptor.getClass().getName());
            }
        }

        Object response;
        try {
            response = executeInterval(command);
        } catch (Throwable e) {
            onError(command, e);
            throw e;
        }

        afterIntercept(command, response);

        if (log.isDebugEnabled()) {
            log.debug("Pears invoke {} over", command.getClass().getName());
        }
        return response;
    }
}
