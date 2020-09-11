package com.example.demo.event.hub;

import com.example.demo.event.Event;
import com.example.demo.event.handler.EventHandler;
import com.example.demo.event.listener.EnhancerEventListener;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.core.annotation.AnnotationUtils;

import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: YangCheng
 * @program:command
 * @title: EventHub
 * @description: 事件中心
 * @data 2020/9/8 0008 16:28
 */
public class EventHub implements DisposableBean {

    private Map<Class<?>, List<EnhancerEventListener>> eventHandlers = new HashMap<>();

    private Map<Class<?>, Class<?>> eventClassCache = new ConcurrentHashMap<>();

    @Override
    public void destroy() throws Exception {
        eventHandlers.clear();
        eventHandlers = null;
    }

    public void  registerEnhancerListener(@NotNull EnhancerEventListener listener){
        EventHandler handlerAnnotation= AnnotationUtils.findAnnotation(listener.getClass(),EventHandler.class);
        Class<? extends Event>[] commandClasses=handlerAnnotation.eventClass();
        for (Class<? extends Event> commandClass:commandClasses){
            List<EnhancerEventListener> eventListeners=eventHandlers.computeIfAbsent(commandClass,key ->new ArrayList<>());
            eventListeners.add(listener);
            Collections.sort(eventListeners);
        }
    }

    public List<EnhancerEventListener> getHandlers(Class<? extends Event> eventClass){
        return eventHandlers.getOrDefault(eventClass, Collections.emptyList());
    }
}
