package com.alibaba.core.dao.specification;

import com.alibaba.core.pojo.specification.SpecificationOption;
import com.alibaba.core.pojo.specification.SpecificationOptionQuery;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SpecificationOptionDao {
    int countByExample(SpecificationOptionQuery example);

    int deleteByExample(SpecificationOptionQuery example);

    int deleteByPrimaryKey(Long id);

    int insert(SpecificationOption record);

    int insertSelective(SpecificationOption record);

    List<SpecificationOption> selectByExample(SpecificationOptionQuery example);

    SpecificationOption selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SpecificationOption record, @Param("example") SpecificationOptionQuery example);

    int updateByExample(@Param("record") SpecificationOption record, @Param("example") SpecificationOptionQuery example);

    int updateByPrimaryKeySelective(SpecificationOption record);

    int updateByPrimaryKey(SpecificationOption record);

    //批量添加
    void insertSelectives(List<SpecificationOption> specificationOptionList);


}