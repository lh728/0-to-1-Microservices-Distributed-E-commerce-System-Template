package com.ecommercesystemtemplate.member.exception;

public class UserNameExistException extends RuntimeException{
    public UserNameExistException() {
        super("UserName Already Exists");
    }
}
