package com.alibaba.core.service.brand;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.good.Brand;

import java.util.List;
import java.util.Map;

public interface BrandService {

    /**
     * 查询所有品牌
     *
     * @return
     */
    List<Brand> findAll();

    /**
     * 分页查询所有品牌
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageResult findPage(Integer pageNum, Integer pageSize);

    /**
     * 条件查询
     *
     * @param pageNum
     * @param pageSize
     * @param brand
     * @return
     */
    PageResult search(Integer pageNum, Integer pageSize, Brand brand);

    /**
     * 保存品牌
     *
     * @param brand
     * @return
     */
    void add(Brand brand);

    /**
     * 回显品牌
     *
     * @param id
     * @return
     */
    Brand findOne(Long id);

    /**
     * 修改品牌
     *
     * @param brand
     */
    void update(Brand brand);

    /**
     * 删除品牌
     *
     * @param id
     */
    void delete(Long[] ids);

    /**
     * 新增模板 初始化下拉列表
     *
     * @return
     */
    List<Map<String, String>> selectOptionList();

}
