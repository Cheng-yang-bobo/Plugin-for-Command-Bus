package com.example.demo.assmbler;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;


/**
 * @author: YangCheng
 * @program:command
 * @title: code
 * @description: 暂不拓展
 *
 * implements this interface should contain static method "of" convert T to Enum
 * and "of" method only have one parameter
 *  <pre>
 *     public enum UserType implements Coder<Integer> {
 *         public static UserType of(Integer t) {
 *              //.....
 *         }
 *     }
 * </pre>
 * @data 2020/9/8 0008 17:12
 */
public interface Coder<T> {


    /**
     * same to {@link this#getCode()}
     * @see this#getCode()
     * @return T
     */
    @JsonIgnore
    default T code() {
        return getCode();
    }

    /**
     * same to {@link this#code()}
     * @see this#code()
     * @return T
     */
    @JsonValue
    T getCode();
}
