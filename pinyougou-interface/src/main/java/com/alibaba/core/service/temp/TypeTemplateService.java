package com.alibaba.core.service.temp;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.template.TypeTemplate;

public interface TypeTemplateService {
    /**
     * 模版列表查询
     *
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate);


    /**
     * 新增模板
     *
     * @param typeTemplate
     */
    void add(TypeTemplate typeTemplate);


    /**
     * 模板回显
     *
     * @param id
     * @return
     */
    TypeTemplate findOne(Long id);

    /**
     * 修改模板
     *
     * @param typeTemplate
     */
    void update(TypeTemplate typeTemplate);

    /**
     * 删除模板
     *
     * @param ids
     */
    void delete(Long[] ids);
}
