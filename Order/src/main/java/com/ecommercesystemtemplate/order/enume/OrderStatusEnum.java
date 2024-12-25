package com.ecommercesystemtemplate.order.enume;

public enum  OrderStatusEnum {
    CREATE_NEW(0,"Payment pending"),
    PAID(1,"Paid"),
    SENDED(2,"Shipped"),
    RECIEVED(3,"Completed"),
    CANCLED(4,"Canceled"),
    SERVICING(5,"After-sales"),
    SERVICED(6,"After-sales completion");
    private Integer code;
    private String msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
