package com.alibaba.core.service.spec;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.specification.Specification;
import com.alibaba.core.vo.SpecVo;

import java.util.List;
import java.util.Map;

public interface SpecService {
    /**
     * 规格列表查询
     *
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    PageResult search(Integer page, Integer rows, Specification specification);

    /**
     * 保存规格
     *
     * @param specification
     */
    void add(SpecVo specVo);


    /**
     * 规格回显
     *
     * @param id
     * @return
     */
    SpecVo findOne(Long id);

    /**
     * 修改规格
     *
     * @param specVo
     */
    void update(SpecVo specVo);


    /**
     * 删除
     *
     * @param ids
     */
    void delete(Long[] ids);


    /**
     * 添加模板 下拉选择产品规格
     *
     * @return
     */
    List<Map<String, String>> selectOptionList();
}
