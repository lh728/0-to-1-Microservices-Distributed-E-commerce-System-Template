package com.ecommercesystemtemplate.product.vo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.ecommercesystemtemplate.product.entity.AttrEntity;
import lombok.Data;

import java.util.List;

@Data
public class AttrGroupWithAttrsVo {
    /**
     * attr_group_id
     */
    private Long attrGroupId;
    /**
     * attr_group_name
     */
    private String attrGroupName;
    /**
     * sort
     */
    private Integer sort;
    /**
     * descript
     */
    private String descript;
    /**
     * group icon
     */
    private String icon;
    /**
     * catelog_id
     */
    private Long catelogId;

    private List<AttrEntity> attrs;
}
