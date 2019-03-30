package com.alibaba.core.task;

import com.alibaba.core.dao.item.ItemCatDao;
import com.alibaba.core.dao.specification.SpecificationOptionDao;
import com.alibaba.core.dao.template.TypeTemplateDao;
import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.pojo.specification.SpecificationOption;
import com.alibaba.core.pojo.specification.SpecificationOptionQuery;
import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class RedisTask {

    @Resource
    private ItemCatDao itemCatDao;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Resource
    private TypeTemplateDao typeTemplateDao;
    @Resource
    private SpecificationOptionDao specificationOptionDao;

    //将分类的数据写到缓存中
    @Scheduled(cron = "10 08 17 29 03 *")
    public void autoItemToRedis() {
        //查询所有分类放入缓存:分类名称----模板id
        List<ItemCat> list = itemCatDao.selectByExample(null);
        if (list != null && list.size() > 0) {
            for (ItemCat itemCat : list) {
                redisTemplate.boundHashOps("itemCat").put(itemCat.getName(), itemCat.getTypeId());
            }
        }
    }

    //将模板的数据写到缓存中
    @Scheduled(cron = "10 08 17 29 03 *")
    public void autoTempToRedis() {
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
    }

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
