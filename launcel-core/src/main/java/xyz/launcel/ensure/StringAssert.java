/**
 * Copyright (C), 2017
 * Author: Launcel
 * Date: 17.11.18
 * Version: 1.0
 */

package xyz.launcel.ensure;

import xyz.launcel.exception.ExceptionFactory;
import xyz.launcel.lang.StringUtils;

public class StringAssert {
    private String flat;
    
    StringAssert(String f) {
        flat = f;
    }
    
    public void isEmpty(String message) {
        if (StringUtils.isEmpty(flat)) {
            ExceptionFactory.create(message);
        }
    }
    
    public void isBlank(String message) {
        if (StringUtils.isBlank(flat)) {
            ExceptionFactory.create(message);
        }
    }
    
    public void notEmpty(String message) {
        if (!StringUtils.isNotEmpty(flat)) {
            ExceptionFactory.create(message);
        }
    }
    
    public void notBlank(String message) {
        if (StringUtils.isNotBlank(flat)) {
            ExceptionFactory.create(message);
        }
    }
}
