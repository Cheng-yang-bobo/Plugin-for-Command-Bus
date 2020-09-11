package com.example.demo.assmbler;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.FatalBeanException;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: YangCheng
 * @program:command
 * @title: AssemblerUtils
 * @description: copy新增枚举
 * @data 2020/9/8 0008 17:11
 */
public class AssemblerUtils {


    static ConcurrentHashMap<Class<?>, Method> ofCache = new ConcurrentHashMap<>(32);

    /**
     * 实现Coder的枚举类型的字段该方法将会处理
     * @param from
     * @param targetClass
     * @param <F>
     * @param <T>
     * @return
     */
    public static <F, T> T doCopy(F from, Class<T> targetClass, String... ignoreParameterNames) {

        T t = (T) ReflectUtils.newInstance(targetClass);

        PropertyDescriptor[] backProperties = BeanUtils.getPropertyDescriptors(targetClass);
        //Set<String> ignorePropertyNames = new HashSet<>();

        Class<?> fromClass = from.getClass();
        Arrays.stream(backProperties)
                .forEach(e->{
                    try {
                        PropertyDescriptor fromProperty = BeanUtils.getPropertyDescriptor(fromClass, e.getName());
                        if (fromProperty !=null && isCodeEnum(e.getPropertyType()) && !fromProperty.getPropertyType().isEnum() ) {

                            Method readMethod = fromProperty.getReadMethod();
                            if (!Modifier.isPublic(readMethod.getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(from);
                            if (value != null) {
                                Method writeMethod = e.getWriteMethod();
                                if (!Modifier.isPublic(writeMethod.getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }

                                Method of = ofCache.computeIfAbsent(e.getPropertyType(), key-> ReflectionUtils.findMethod(key, "of", null));
                                if (of == null) {
                                    throw new NoSuchMethodException(String.format("%s implements Coder must have static method of", e.getPropertyType().getName()));
                                }
                                Object code = of.invoke(null, value);
                                writeMethod.invoke(t, code);
                            }
                        }
                        else if (fromProperty != null && isCodeEnum(fromProperty.getPropertyType()) && !e.getPropertyType().isEnum()) {
                            Method readMethod = fromProperty.getReadMethod();
                            if (!Modifier.isPublic(readMethod.getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(from);
                            if (value != null) {
                                Object code = ((Coder) value).code();
                                if (code != null) {
                                    Method writeMethod = e.getWriteMethod();
                                    if (!Modifier.isPublic(writeMethod.getModifiers())) {
                                        writeMethod.setAccessible(true);
                                    }
                                    writeMethod.invoke(t, code);
                                }
                            }
                        }
                    } catch (Exception ex) {
                        throw new FatalBeanException("error copy bean property", ex);
                    }
                });
        BeanUtils.copyProperties(from, t, ignoreParameterNames);
        return t;
    }

    private static boolean isCodeEnum(Class<?> type) {
        return type.isEnum() && Coder.class.isAssignableFrom(type);
    }

}
