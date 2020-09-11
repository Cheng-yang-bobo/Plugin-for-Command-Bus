package com.example.demo.event.bus;

import com.example.demo.event.Event;
import com.example.demo.event.hub.EventHub;
import com.example.demo.event.listener.EnhancerEventListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author: YangCheng
 * @program:command
 * @title: EventBus
 * @description: 事件总线
 * @data 2020/9/8 0008 16:27
 */
@Slf4j
public class EventBus {
    @Resource
    private EventHub eventHub;
    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    private Map<Class<?>, List<EnhancerEventListener>> listenerCache = new ConcurrentHashMap<>();

    private final Object lock = new Object();



    public <E extends Event> void publish(@NotNull E event) {
        final E e = event;

        List<EnhancerEventListener> handlers = getHandlerDelegates(e);

        if (log.isDebugEnabled()) {
            log.debug("-------Pears Event listener Get-------");
            log.debug("Event {} get listener {} :", e.getClass().getName(), handlers.size());
            for (EnhancerEventListener handler : handlers) {
                log.debug("{} order is {}", handler.getClass().getName(), handler.getOrder());
            }
            log.debug("-------Pears Event listener Get-------");
        }
        handler(e, handlers);

    }

    public <E extends Event> void asyncPublish(@NotNull E event) {
        final E e = event;

        List<EnhancerEventListener> handlers = getHandlerDelegates(e);

        if (log.isDebugEnabled()) {
            log.debug("-------Pears Event listener Get-------");
            log.debug("Event {} get listener {} :", e.getClass().getName(), handlers.size());
            for (EnhancerEventListener handler : handlers) {
                log.debug("{} order is {}", handler.getClass().getName(), handler.getOrder());
            }
            log.debug("-------Pears Event listener Get-------");
        }

        log.debug("Async publish event {}", event);

        try {
            threadPoolTaskExecutor.execute(()->{
                handler(e, handlers);
            });
        } catch (RejectedExecutionException ex) {
            //被拒绝改用同步方式处理
            log.warn("ThreadPoolTasKExecutor reject ");
            handler(e, handlers);
        }
    }

    private <E extends Event> void handler(E event, List<EnhancerEventListener> handlers) {
        E newEvent = null;
        for (EnhancerEventListener handler : handlers) {
            Object enhancerEvent = handler.enhancerExecute(Objects.nonNull(newEvent) ? newEvent : event);
            if (Objects.nonNull(enhancerEvent) && event.getClass().isAssignableFrom(enhancerEvent.getClass())) {
                newEvent = (E) enhancerEvent;
            }
        }
    }

    private List<EnhancerEventListener> getHandlerDelegates(Event e) {
        List<EnhancerEventListener> delegates = listenerCache.get(e.getClass());
        if (delegates == null) {
            synchronized (lock) {
                delegates = listenerCache.get(e.getClass());
                if (delegates != null) {
                    return delegates;
                }
                List<Class<?>> eventClasses = new ArrayList<>();
                eventClasses.add(e.getClass());
                Class<?> eventClass = e.getClass();
                while (Event.class.isAssignableFrom(eventClass.getSuperclass())) {
                    eventClasses.add(eventClass.getSuperclass());
                    eventClass = eventClass.getSuperclass();
                }
                delegates = new ArrayList<>();
                for (Class<?> aClass : eventClasses) {
                    List<EnhancerEventListener> handlers = eventHub.getHandlers((Class<? extends Event>) aClass);
                    if (handlers != null) {
                        delegates.addAll(handlers);
                    }
                }
                listenerCache.put(e.getClass(), delegates);
                return delegates;
            }
        }
        return delegates;
    }
}
