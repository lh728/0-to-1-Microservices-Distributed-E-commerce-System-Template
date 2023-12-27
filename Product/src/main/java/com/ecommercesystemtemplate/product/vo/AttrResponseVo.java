package com.ecommercesystemtemplate.product.vo;

import lombok.Data;

@Data
public class AttrResponseVo extends AttrVo{

    /**
     * attr_group_name
     */
    private String catelogName;

    /**
     * attr_group_name
     */
    private String groupName;

    /**
     * category path
     */
    private Long[] catelogPath;

}
