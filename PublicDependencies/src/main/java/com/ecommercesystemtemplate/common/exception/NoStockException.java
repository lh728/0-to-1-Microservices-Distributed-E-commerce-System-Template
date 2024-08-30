package com.ecommercesystemtemplate.common.exception;

public class NoStockException extends RuntimeException {
    private Long skuId;
    public NoStockException(Long skuId) {
        super("product id :" + skuId + " no stock");
    }

    public NoStockException(String msg) {
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
