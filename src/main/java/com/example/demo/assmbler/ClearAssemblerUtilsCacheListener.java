package com.example.demo.assmbler;

import com.example.demo.assmbler.AssemblerUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * @author: YangCheng
 * @program:command
 * @title: ClearAssemblerUtilsCacheListener
 * @description:
 * @data 2020/9/8 0008 17:15
 */
public class ClearAssemblerUtilsCacheListener implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        AssemblerUtils.ofCache.clear();
    }
}
