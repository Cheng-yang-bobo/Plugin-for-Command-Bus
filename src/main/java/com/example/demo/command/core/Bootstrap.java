package com.example.demo.command.core;

import com.example.demo.command.core.CommandBusImpl;
import com.example.demo.command.core.CommandHub;
import com.example.demo.command.core.RegisterDelegate;
import com.example.demo.event.bus.EventBus;
import com.example.demo.event.hub.EventHub;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;

/**
 * @author: YangCheng
 * @program:command
 * @title: Bootstrap
 * @description: 初始化工具类
 * @data 2020/9/8 0008 16:20
 */
@Configuration
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class Bootstrap {


    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }

    @Bean
    public EventHub eventHub() {
        return new EventHub();
    }

    @Bean
    public CommandBusImpl commandBus() {
        return new CommandBusImpl();
    }

    @Bean(initMethod = "init")
    public RegisterDelegate registerDelegate() {
        return new RegisterDelegate();
    }

    @Bean
    public CommandHub commandHub() {
        return new CommandHub();
    }

}
