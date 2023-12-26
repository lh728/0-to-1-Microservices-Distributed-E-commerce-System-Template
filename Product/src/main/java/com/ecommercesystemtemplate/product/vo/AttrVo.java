package com.ecommercesystemtemplate.product.vo;

import lombok.Data;

@Data
public class AttrVo {
    /**
     * attr_id
     */
    private Long attrId;
    /**
     * attr_name
     */
    private String attrName;
    /**
     * Does it need to be retrieved? [0- not required, 1- required]
     */
    private Integer searchType;
    /**
     * value_type[0-single，1-multiple choice]
     */
    private Integer valueType;
    /**
     * Attribute ICONS
     */
    private String icon;
    /**
     * List of possible values [separated by commas]
     */
    private String valueSelect;
    /**
     * Attribute type [0- sales attribute, 1- basic attribute, 2- both sales attribute and basic attribute]
     */
    private Integer attrType;
    /**
     * Enabled status [0 - disabled, 1 - enabled]
     */
    private Long enable;
    /**
     * catelog_id
     */
    private Long catelogId;
    /**
     * Quick display [whether to show on the introduction; 0- No 1- yes], can still be adjusted in the sku
     */
    private Integer showDesc;

    /**
     * attr_group_id
     */
    private Long attrGroupId;

}
