package com.f.core.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * created by f at 8/19/21 23:46
 */
public class LogTest {
    public static void main(String[] args) {
        Logger logger  = LoggerFactory.getLogger(LogTest.class);
        logger.info("hello");
    }
}
