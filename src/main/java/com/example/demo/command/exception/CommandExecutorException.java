package com.example.demo.command.exception;

/**
 * @author: YangCheng
 * @program:command
 * @title: CommandExecutorException
 * @description: Command异常捕获器
 * @data 2020/9/8 0008 16:19
 */
public class CommandExecutorException extends RuntimeException{

    public CommandExecutorException() {
        super();
    }

    public CommandExecutorException(String message) {
        super(message);
    }

}
