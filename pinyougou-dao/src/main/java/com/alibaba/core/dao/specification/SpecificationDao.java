package com.alibaba.core.dao.specification;

import com.alibaba.core.pojo.specification.Specification;
import com.alibaba.core.pojo.specification.SpecificationQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpecificationDao {
    int countByExample(SpecificationQuery example);

    int deleteByExample(SpecificationQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(Specification record);

    int insertSelective(Specification record);

    List<Specification> selectByExample(SpecificationQuery example);

    Specification selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Specification record, @Param("example") SpecificationQuery example);

    int updateByExample(@Param("record") Specification record, @Param("example") SpecificationQuery example);

    int updateByPrimaryKeySelective(Specification record);

    int updateByPrimaryKey(Specification record);

    /**
     * 新增模板 初始化下拉列表
     *
     * @return
     */
    List<Map<String, String>> selectOptionList();
}