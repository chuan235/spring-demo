package com.gc.crud.components;

import org.springframework.stereotype.Component;

@Component
public class UserNotExistException extends RuntimeException{

    public UserNotExistException() {
        super("用户不存在....");
    }
}
