package com.alibaba.core.service.temp;

import com.alibaba.core.dao.specification.SpecificationOptionDao;
import com.alibaba.core.dao.template.TypeTemplateDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.specification.SpecificationOption;
import com.alibaba.core.pojo.specification.SpecificationOptionQuery;
import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.core.pojo.template.TypeTemplateQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class TypeTemplateServiceImpl implements TypeTemplateService {

    //注入dao
    @Resource
    private TypeTemplateDao typeTemplateDao;

    @Resource
    private SpecificationOptionDao specificationOptionDao;

    @Resource
    private RedisTemplate redisTemplate;

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
        // 将模板数据放入缓存
        List<TypeTemplate> list = typeTemplateDao.selectByExample(null);
        if (list != null && list.size() > 0) {
            for (TypeTemplate template : list) {
                //缓存该模板下的品牌
                //String brandIds=[{"id":37,"text":"花花公子"},{"id":38,"text":"七匹狼"}]
                List<Map> mapList = JSON.parseArray(template.getBrandIds(), Map.class);
                //List<Map> mapList={{key:37,value:花花公子},{key:38,value:七匹狼}}
                redisTemplate.boundHashOps("brandList").put(template.getId(), mapList);


                //缓存该模板下的规格
                List<Map> specList = findBySpecList(template.getId());
                redisTemplate.boundHashOps("specList").put(template.getId(), specList);
            }
        }


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

    /**
     * 新建分类时 加载模板列表
     *
     * @return
     */
    @Override
    public List<TypeTemplate> findAll() {
        List<TypeTemplate> typeTemplates = typeTemplateDao.selectByExample(null);
        return typeTemplates;
    }

    /**
     * 添加商品 选择加载规格
     *
     * @param id
     * @return
     */
    @Override
    public List<Map> findBySpecList(Long id) {
        //通过模板id获取规格
        TypeTemplate typeTemplate = typeTemplateDao.selectByPrimaryKey(id);
        //例子[{"id":27,"text":"网络"},{"id":32,"text":"机身内存"}]
        String specIds = typeTemplate.getSpecIds();
        List<Map> specList = JSON.parseArray(specIds, Map.class);

        //通过规格id获取规格id
        if (specList != null && specList.size() > 0) {
            for (Map map : specList) {
                Long specId = Long.parseLong(map.get("id").toString());
                //通过规格id获取规格id
                SpecificationOptionQuery query = new SpecificationOptionQuery();
                query.createCriteria().andSpecIdEqualTo(specId);
                List<SpecificationOption> options = specificationOptionDao.selectByExample(query);

                //封装到map
                map.put("options", options);
            }
        }
        return specList;
    }
}
