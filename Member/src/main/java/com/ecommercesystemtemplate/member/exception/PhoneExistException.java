package com.ecommercesystemtemplate.member.exception;

public class PhoneExistException extends RuntimeException {
    public PhoneExistException() {
        super("Phone Number Already Exists");
    }
}
