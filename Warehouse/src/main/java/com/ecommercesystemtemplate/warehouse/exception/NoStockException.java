package com.ecommercesystemtemplate.warehouse.exception;

public class NoStockException extends RuntimeException {
    private Long skuId;
    public NoStockException( Long skuId) {
        super("product id :" + skuId + " no stock");
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }
}
