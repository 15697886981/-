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

    /**
     * 保存分类
     *
     * @param itemCat
     */
    void add(ItemCat itemCat);

    /**
     * 新增商品选择三级分类加载模板id
     *
     * @param id
     * @return
     */
    ItemCat findOne(Long id);


    /**
     * 查询所有分类
     *
     * @return
     */
    List<ItemCat> findAll();

}
