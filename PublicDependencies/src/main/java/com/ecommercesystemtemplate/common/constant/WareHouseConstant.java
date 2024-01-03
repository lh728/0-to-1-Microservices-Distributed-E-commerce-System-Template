package com.ecommercesystemtemplate.common.constant;

import lombok.Getter;

public class WareHouseConstant {

    @Getter
    public enum PurchaseStatusEnum {
        CREATED(0, "NEW"), ASSIGNED(1, "ASSIGNED"), RECEIVED(2, "RECEIVED"),
        COMPLETED(3, "COMPLETED"), ERROR(4, "ERROR");

        private int code;
        private String msg;

        PurchaseStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

    }

    @Getter
    public enum PurchaseDetailStatusEnum {
        CREATED(0, "NEW"), ASSIGNED(1, "ASSIGNED"), PURCHASING(2, "PURCHASING"),
        COMPLETED(3, "COMPLETED"), FAILED(4, "FAILED");

        private int code;
        private String msg;

        PurchaseDetailStatusEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

    }
}
