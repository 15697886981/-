package com.alibaba.core.dao.good;

import com.alibaba.core.pojo.good.Brand;
import com.alibaba.core.pojo.good.BrandQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BrandDao {
    int countByExample(BrandQuery example);

    int deleteByExample(BrandQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    int insertSelective(Brand record);

    List<Brand> selectByExample(BrandQuery example);

    Brand selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Brand record, @Param("example") BrandQuery example);

    int updateByExample(@Param("record") Brand record, @Param("example") BrandQuery example);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);

    void updateByExampleSelective(Brand brand);

    //批量删除
    void deleteByPrimaryKeys(Long[] ids);

    /**
     * 新增模板 初始化下拉列表
     *
     * @return
     */
    List<Map<String, String>> selectOptionList();
}