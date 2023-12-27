package com.ecommercesystemtemplate.common.constant;

public class ProductConstant {

    public enum AttrEnum {
        ATTR_TYPE_BASE(1, "base attribute"), ATTR_TYPE_SALE(0, "sales attribute");

        private int code;
        private String msg;

        AttrEnum(int code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public static AttrEnum getAttrTypeByCode(int code) {
            AttrEnum[] values = AttrEnum.values();
            for (AttrEnum value : values) {
                if (value.getCode() == code) {
                    return value;
                }
            }
            return null;
        }

        public int getCode() {
            return code;
        }

        public String getMsg() {
            return msg;
        }
    }
}
