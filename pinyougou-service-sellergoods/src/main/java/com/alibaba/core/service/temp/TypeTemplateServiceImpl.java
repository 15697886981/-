package com.alibaba.core.service.temp;

import com.alibaba.core.dao.template.TypeTemplateDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    //注入dao
    @Resource
    private TypeTemplateDao typeTemplateDao;

    /**
     * 模版列表查询
     *
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, TypeTemplate typeTemplate) {
        //设置分页条件
        PageHelper.startPage(page, rows);

        //设置查询条件
        TypeTemplateQuery query = new TypeTemplateQuery();

        if (typeTemplate.getName() != null && !"".equals(typeTemplate.getName().trim())) {
            query.createCriteria().andNameLike("%" + typeTemplate.getName() + "%");
        }
        query.setOrderByClause("id desc");
        //查询
        Page<TypeTemplate> p = (Page<TypeTemplate>) typeTemplateDao.selectByExample(query);
        //封装结果集
        return new PageResult(p.getTotal(), p.getResult());

    }

    /**
     * 新增模板
     *
     * @param typeTemplate
     */
    @Transactional
    @Override
    public void add(TypeTemplate typeTemplate) {
        typeTemplateDao.insertSelective(typeTemplate);
    }


    /**
     * 模板回显
     *
     * @param id
     * @return
     */
    @Override
    public TypeTemplate findOne(Long id) {
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        return typeTemplate;
    }

    /**
     * 修改模板
     *
     * @param typeTemplate
     */
    @Transactional
    @Override
    public void update(TypeTemplate typeTemplate) {
        typeTemplateDao.updateByPrimaryKeySelective(typeTemplate);
    }

    /**
     * 删除模板
     *
     * @param ids
     */
    @Override
    public void delete(Long[] ids) {
        if (ids != null && ids.length > 0) {
//            for (Long id : ids) {
//                typeTemplateDao.deleteByPrimaryKey(id);
//            }
            typeTemplateDao.deleteByPrimaryKeys(ids);
        }
    }
}
