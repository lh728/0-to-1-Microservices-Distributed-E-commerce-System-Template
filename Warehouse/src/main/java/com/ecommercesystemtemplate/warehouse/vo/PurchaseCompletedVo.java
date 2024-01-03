package com.ecommercesystemtemplate.warehouse.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author lhjls
 */
@Data
public class PurchaseCompletedVo {
    @NotNull
    private Long id;
    private List<PurchaseItemVo> items;


}
