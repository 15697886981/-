package com.alibaba.core.service.itemcat;

import com.alibaba.core.pojo.item.ItemCat;

import java.util.List;

public interface ItemCatService {
    /**
     * 商品分类列表查询
     *
     * @param parentId
     * @return
     */
    List<ItemCat> findByParentId(Long parentId);
}
