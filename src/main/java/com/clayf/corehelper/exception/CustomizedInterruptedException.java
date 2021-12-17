package com.clayf.corehelper.exception;

/**
 * 自定义异常
 * created by f at 8/14/21 01:08
 */
public class CustomizedInterruptedException extends RuntimeException {

    /**
     * 构造函数
     * @param message 错误消息
     */
    public CustomizedInterruptedException(String message) {
        super(message);
    }
}
