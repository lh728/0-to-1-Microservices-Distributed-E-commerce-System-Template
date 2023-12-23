package com.ecommercesystemtemplate.common.exception;

/**
   error code and message definition:
    1. code must be 5 digits
    2. first two digits: business scenario
    last three digits: error code ex. 10001. 10: common; 001: validation exception
    3. exception description:
        10: common
            001: validation exception
        11: product
        12: order
        13: cart
        14: delivery

 */
public enum BizCodeEnume {

    unknownException(10000, "Unknown exception"),
    validException(10001, "Validation exception");

    private Integer code;
    private String message;

    BizCodeEnume(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
