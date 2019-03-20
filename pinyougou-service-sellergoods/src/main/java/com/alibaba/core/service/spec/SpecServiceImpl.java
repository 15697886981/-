package com.alibaba.core.service.spec;

import com.alibaba.core.dao.specification.SpecificationDao;
import com.alibaba.core.dao.specification.SpecificationOptionDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.specification.Specification;
import com.alibaba.core.pojo.specification.SpecificationOption;
import com.alibaba.core.pojo.specification.SpecificationOptionQuery;
import com.alibaba.core.pojo.specification.SpecificationQuery;
import com.alibaba.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.opensaml.xml.signature.Q;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class SpecServiceImpl implements SpecService {

    //注入dao
    @Resource
    private SpecificationDao specificationDao;
    @Resource
    private SpecificationOptionDao specificationOptionDao;


    /**
     * 规格列表查询
     *
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Specification specification) {
        //设置分页条件
        PageHelper.startPage(page, rows);
        //设置查询条件
        SpecificationQuery query = new SpecificationQuery();
        if (specification.getSpecName() != null && !"".equals(specification.getSpecName().trim())) {
            query.createCriteria().andSpecNameLike("%" + specification.getSpecName().trim() + "%");
        }
        query.setOrderByClause("id desc");

        //查询
        Page<Specification> p = (Page<Specification>) specificationDao.selectByExample(query);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 保存规格
     *
     * @param specVo
     */
    @Transactional
    @Override
    public void add(SpecVo specVo) {

        //保存规格插入数据后需要返回自增主键的id
        Specification specification = specVo.getSpecification();
        specificationDao.insertSelective(specification);//返回主键id

        //保存 规格选项
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if (specificationOptionList != null && specificationOptionList.size() > 0) {

            for (SpecificationOption specificationOption : specificationOptionList) {
                //获取主键
                specificationOption.setSpecId(specification.getId());
                //specificationOptionDao.insertSelective(specificationOption);
            }

            specificationOptionDao.insertSelectives(specificationOptionList);

        }
    }

    /**
     * 规格回显
     *
     * @param id
     * @return
     */
    @Override
    public SpecVo findOne(Long id) {
        //获取规格
        Specification specification = specificationDao.selectByPrimaryKey(id);
        //获取规格选项
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(id);
        List<SpecificationOption> specificationOptionList = specificationOptionDao.selectByExample(query);
        //封装到vo中
        SpecVo specVo = new SpecVo();
        specVo.setSpecification(specification);
        specVo.setSpecificationOptionList(specificationOptionList);

        return specVo;
    }

    /**
     * 修改规格
     *
     * @param specVo
     */
    @Transactional
    @Override
    public void update(SpecVo specVo) {
        //更新规格
        Specification specification = specVo.getSpecification();
        specificationDao.updateByPrimaryKeySelective(specification);

        //更新规格选项
        //先删除规格选项
        SpecificationOptionQuery query = new SpecificationOptionQuery();
        query.createCriteria().andSpecIdEqualTo(specification.getId());
        specificationOptionDao.deleteByExample(query);

        //再插入
        List<SpecificationOption> specificationOptionList = specVo.getSpecificationOptionList();
        if (specificationOptionList != null && specificationOptionList.size() > 0) {
            for (SpecificationOption specificationOption : specificationOptionList) {
                //先获取主键id
                specificationOption.setSpecId(specification.getId());
            }
        }
        //批量插入
        specificationOptionDao.insertSelectives(specificationOptionList);
    }

    /**
     * 删除
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0) {
            for (Long id : ids) {
                //删除规格
                specificationDao.deleteByPrimaryKey(id);

                //删除规格
                SpecificationOptionQuery optionQuery = new SpecificationOptionQuery();
                optionQuery.createCriteria().andSpecIdEqualTo(id);
                specificationOptionDao.deleteByExample(optionQuery);
            }
        }
    }

    /**
     * 添加模板 下拉选择产品规格
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        return specificationDao.selectOptionList();
    }
}
