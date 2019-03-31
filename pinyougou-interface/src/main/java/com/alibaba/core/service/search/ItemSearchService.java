package com.alibaba.core.service.search;

import java.util.Map;

public interface ItemSearchService {
    /**
     * 前台系统检索
     *
     * @param searchMap
     * @return
     */
    Map<String, Object> search(Map<String, String> searchMap);


    /**
     * 商品上架--保存到索引中
     *
     * @param id
     */
    void addItemToSolr(Long id);

    /**
     * 商品下架--从索引库中删除
     *
     * @param id
     */
    void deleteItemFromSolr(Long id);
}
