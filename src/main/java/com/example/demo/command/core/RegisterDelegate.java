package com.example.demo.command.core;

import com.example.demo.command.Command;
import com.example.demo.command.annotation.CommandActuator;
import com.example.demo.command.exception.CommandExecutorException;
import com.example.demo.command.executor.CommandExecutor;
import com.example.demo.command.executor.ResponseCommandExecutor;
import com.example.demo.command.interceptor.CommandInterceptor;
import com.example.demo.command.interceptor.Interceptor;
import com.example.demo.event.exception.NotEventListenerException;
import com.example.demo.event.handler.EventHandler;
import com.example.demo.event.hub.EventHub;
import com.example.demo.event.listener.EnhancerEventListener;
import com.example.demo.event.listener.impl.EventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author: YangCheng
 * @program:command
 * @title: RegisterDelegate
 * @description: 注册代理
 * @data 2020/9/8 0008 16:08
 */
@Slf4j
public class RegisterDelegate implements ApplicationContextAware {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private CommandHub commandHub;
    @Autowired
    private EventHub eventHub;


    public void init() {
        if (log.isDebugEnabled()) {
            log.debug("-------Pears init start-------");
        }
        Map<String, Object> interceptors = applicationContext.getBeansWithAnnotation(Interceptor.class);
        registerInterceptor(interceptors);

        Map<String, Object> commandExecutors = applicationContext.getBeansWithAnnotation(CommandActuator.class);
        registerCommandExecutor(commandExecutors);

        Map<String, Object> eventListeners = applicationContext.getBeansWithAnnotation(EventHandler.class);
        registerEventListener(eventListeners);
        if (log.isDebugEnabled()) {
            log.debug("-------Pears init over-------");
        }
    }

    private void registerInterceptor(Map<String, Object> interceptors) {
        for (Map.Entry<String, Object> entry : interceptors.entrySet()) {
            Interceptor interceptor = AnnotationUtils.findAnnotation(entry.getValue().getClass(), Interceptor.class);

            if (interceptor.commands() == null || interceptor.commands().length == 0) {
                commandHub.getGlobalInterceptors().add((CommandInterceptor) entry.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("Pears Get Global interceptor {}", entry.getValue().getClass().getName());
                }
            } else {
                for (Class<? extends Command> commandClass : interceptor.commands()) {
                    commandHub.getInterceptors().computeIfAbsent(commandClass, k -> new ArrayList<>()).add((CommandInterceptor) entry.getValue());
                }
                if (log.isDebugEnabled()) {
                    log.debug("Pears Get common interceptor {} for {}", entry.getValue().getClass().getName(), interceptor.commands());
                }
            }
        }
    }

    private void registerCommandExecutor(Map<String, Object> commandExecutors) {
        for (Map.Entry<String, Object> entry : commandExecutors.entrySet()) {
            CommandActuator commandActuator = AnnotationUtils.findAnnotation(entry.getValue().getClass(), CommandActuator.class);
            CommandInvocation commandInvocation;
            if (entry.getValue() instanceof ResponseCommandExecutor) {
                commandInvocation = new CommandInvocation((ResponseCommandExecutor) entry.getValue());
            } else if (entry.getValue() instanceof CommandExecutor) {
                commandInvocation = new CommandInvocation((CommandExecutor) entry.getValue());
            } else {
                throw new CommandExecutorException("Command executor must implements CommandExecutor or ResponseCommandExecutor");
            }


            List<CommandInterceptor> globalInterceptors = commandHub.getGlobalInterceptors();
            List<CommandInterceptor> specifiedInterceptors = commandHub.getInterceptors().getOrDefault(commandActuator.commandClass(), Collections.emptyList());

            List<CommandInterceptor> interceptors = new ArrayList<>(globalInterceptors);
            interceptors.addAll(specifiedInterceptors);

            //try get parent interceptor
            Class<?> commandClass = commandActuator.commandClass();
            while (Command.class.isAssignableFrom(commandClass.getSuperclass()) && !Command.class.equals(commandClass.getSuperclass())) {
                interceptors.addAll(commandHub.getInterceptors().getOrDefault(commandClass.getSuperclass(), Collections.emptyList()));
                commandClass = commandClass.getSuperclass();
            }
            commandInvocation.setInterceptors(interceptors);
            commandHub.getExecutorMap().put(commandActuator.commandClass(), commandInvocation);
        }
    }

    private void registerEventListener(Map<String, Object> eventListeners) {
        for (Map.Entry<String, Object> entry : eventListeners.entrySet()) {
            if (entry.getValue() instanceof EnhancerEventListener) {
                eventHub.registerEnhancerListener((EnhancerEventListener) entry.getValue());
                if (log.isDebugEnabled()) {
                    log.debug("Pears Get event listener {} order is {}", entry.getValue().getClass().getName(), ((EnhancerEventListener) entry.getValue()).getOrder());
                }
            } else {
                throw new NotEventListenerException(String.format("event listener must implements %s or %s", EventListener.class.getName(), EnhancerEventListener.class.getName()));
            }
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
