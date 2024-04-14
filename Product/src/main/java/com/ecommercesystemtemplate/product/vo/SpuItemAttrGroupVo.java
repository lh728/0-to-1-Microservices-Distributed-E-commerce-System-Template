package com.ecommercesystemtemplate.product.vo;

import lombok.Data;
import lombok.ToString;

import java.util.List;
@Data
@ToString
public class SpuItemAttrGroupVo {

    private String groupName;
    private List<Attr> attrs;
}
