package com.alibaba.core.service.content;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.ad.ContentCategory;

import java.util.List;

public interface ContentCategoryService {

    List<ContentCategory> findAll();

    PageResult findPage(ContentCategory contentCategory, Integer pageNum, Integer pageSize);

    void add(ContentCategory contentCategory);

    void edit(ContentCategory contentCategory);

    ContentCategory findOne(Long id);

    void delAll(Long[] ids);


}
