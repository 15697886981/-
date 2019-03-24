package com.alibaba.core.service.content;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.ad.Content;

import java.util.List;

public interface ContentService {

    List<Content> findAll();

    PageResult findPage(Content content, Integer pageNum, Integer pageSize);

    void add(Content content);

    void edit(Content content);

    Content findOne(Long id);

    void delAll(Long[] ids);

    /**
     * 首页大广告轮播图
     *
     * @param categoryId
     * @return
     */
    List<Content> findByCategoryId(Long categoryId);

}
