package com.alibaba.core.service.search;

import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.item.ItemQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;

import javax.annotation.Resource;
import java.util.*;

@Service
public class ItemSearchServiceImpl implements ItemSearchService {

    @Resource
    private SolrTemplate solrTemplate;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private ItemDao itemDao;

    /**
     * 前台系统检索
     *
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap) {
        //创建一个大map,封装所有的结果集
        Map<String, Object> resultMap = new HashMap<>();
        //处理关键字中包含空格的问题
        String keywords = searchMap.get("keywords");
        if (keywords != null && !keywords.equals("")) {
            keywords = keywords.replace(" ", "");
            searchMap.put("keywords", keywords);
        }


        // 1、根据关键字检索并分页
        //Map<String, Object> map = searchForPage(searchMap);
        // 2、根据关键字检索并且分页  关键字进行高亮
        Map<String, Object> map = searchForHighLightPage(searchMap);

        resultMap.putAll(map);


        //3.加载分类结果集
        List<String> categoryList = searchForGroupPage(searchMap);
        if (categoryList != null && categoryList.size() > 0) {
            resultMap.put("categoryList", categoryList);

            //默认加载第一个分类下的品牌
            Map<String, Object> brandAndSpecMap = searchBrandAndSpecListByCategory(categoryList.get(0));

            resultMap.putAll(brandAndSpecMap);

        }
        return resultMap;
    }

    private Map<String, Object> searchBrandAndSpecListByCategory(String category) {
        Map<String, Object> brandAndSpecMap = new HashMap<>();
        // 从缓存中获取模板id
        Object typeId = redisTemplate.boundHashOps("itemCat").get(category);
        // 获取品牌
        List<Map> brandList = (List<Map>) redisTemplate.boundHashOps("brandList").get(typeId);
        // 获取规格
        List<Map> specList = (List<Map>) redisTemplate.boundHashOps("specList").get(typeId);


        brandAndSpecMap.put("brandList", brandList);
        brandAndSpecMap.put("specList", specList);
        return brandAndSpecMap;
    }

    //加载分类结果集
    private List<String> searchForGroupPage(Map<String, String> searchMap) {
        // 1、设置关键字条件
        Criteria criteria = new Criteria("item_keywords");
        String keywords = searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);
        // 2、设置分组条件
        GroupOptions groupOptions = new GroupOptions();
        groupOptions.addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        // 3、根据条件查询
        GroupPage<Item> groupPage = solrTemplate.queryForGroupPage(query, Item.class);


        // 4、将结果封装到list中
        List<String> list = new ArrayList<>();
        GroupResult<Item> groupResult = groupPage.getGroupResult("item_category");
        Page<GroupEntry<Item>> groupEntries = groupResult.getGroupEntries();
        for (GroupEntry<Item> groupEntry : groupEntries) {
            String groupValue = groupEntry.getGroupValue();
            list.add(groupValue);
        }
        return list;
    }

    //根据关键字检索高亮并分页
    private Map<String, Object> searchForHighLightPage(Map<String, String> searchMap) {
        //1.设置检索条件
        Criteria criteria = new Criteria("item_keywords");
        String keywords = searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleHighlightQuery query = new SimpleHighlightQuery(criteria);
        //2.设置分页条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer start = (pageNo - 1) * pageSize;
        query.setOffset(start);
        query.setRows(pageSize);
        //2.设置高亮条件
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");
        highlightOptions.setSimplePrefix("<b>");
        highlightOptions.setSimplePostfix("</b>");
        query.setHighlightOptions(highlightOptions);


        /*设置过滤条件*/
        //根据商品分类过滤
        String category = searchMap.get("category");
        if (category != null && !category.equals("")) {
            Criteria cri = new Criteria("item_category");
            cri.is(category);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }
        //根据商品品牌过滤
        String brand = searchMap.get("brand");
        if (brand != null && !brand.equals("")) {
            Criteria cri = new Criteria("item_brand");
            cri.is(brand);
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }
        //根据商品规格过滤
        String spec = searchMap.get("spec");
        if (spec != null && !"".equals(spec)) {
            Map<String, String> specMap = JSON.parseObject(spec, Map.class);
            Set<Map.Entry<String, String>> entries = specMap.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                Criteria cri = new Criteria("item_spec_" + entry.getKey());
                cri.is(entry.getValue());
                SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
                query.addFilterQuery(filterQuery);
            }
        }
        //根据商品价格过滤
        String price = searchMap.get("price");
        if (price != null && !"".equals(price)) {
            //价格区间[min TO max]
            String[] prices = price.split("-");
            Criteria cri = new Criteria("item_price");
            if (price.contains("*")) {//xxx以上
                cri.greaterThanEqual(prices[0]);
            } else {
                cri.between(prices[0], prices[1], true, true);
            }
            SimpleFilterQuery filterQuery = new SimpleFilterQuery(cri);
            query.addFilterQuery(filterQuery);
        }


        /*添加排序条件*/
        //根据新品,价格排序
        String sort = searchMap.get("sort");
        // 根据价格以及新品排序：sortField：排序字段    sort：传递的值
        if (sort != null && !sort.equals("")) {
            if ("ASC".equals(sort)) {
                Sort s = new Sort(Sort.Direction.ASC, "item_" + searchMap.get("sortField"));
                query.addSort(s);
            } else {
                Sort s = new Sort(Sort.Direction.DESC, "item_" + searchMap.get("sortField"));
                query.addSort(s);
            }
        }

        //3.根据条件检索
        HighlightPage<Item> highlightPage = solrTemplate.queryForHighlightPage(query, Item.class);

        //4.处理高亮条件
        List<HighlightEntry<Item>> highlighted = highlightPage.getHighlighted();
        if (highlighted != null && highlighted.size() > 0) {
            for (HighlightEntry<Item> itemHighlightEntry : highlighted) {
                Item item = itemHighlightEntry.getEntity();//普通的结果
                List<HighlightEntry.Highlight> highlights = itemHighlightEntry.getHighlights();
                if (highlights != null && highlights.size() > 0) {
                    String title = highlights.get(0).getSnipplets().get(0);
                    item.setTitle(title);
                }
            }
        }
        //5.封装结果集到map中
        Map<String, Object> map = new HashMap<>();
        map.put("totalPages", highlightPage.getTotalPages());
        map.put("total", highlightPage.getTotalElements());
        map.put("rows", highlightPage.getContent()); //结果集

        return map;
    }

    //根据关键字检索并分页
    private Map<String, Object> searchForPage(Map<String, String> searchMap) {
        //1.设置检索条件

        Criteria criteria = new Criteria("item_keywords");
        String keywords = searchMap.get("keywords");
        if (keywords != null && !"".equals(keywords)) {
            criteria.is(keywords);
        }
        SimpleQuery query = new SimpleQuery(criteria);
        //2.设置分页条件
        Integer pageNo = Integer.valueOf(searchMap.get("pageNo"));
        Integer pageSize = Integer.valueOf(searchMap.get("pageSize"));
        Integer start = (pageNo - 1) * pageSize;

        query.setOffset(start);     //起始页
        query.setRows(pageSize);    //每页显示的条数
        //3.根据条件查询
        ScoredPage<Item> scoredPage = solrTemplate.queryForPage(query, Item.class);
        //4.封装结果集到map中
        Map<String, Object> map = new HashMap<>();
        map.put("totalPages", scoredPage.getTotalPages());//总页数
        map.put("total", scoredPage.getTotalElements());//总条数
        map.put("rows", scoredPage.getContent()); //结果集

        return map;

    }

    /**
     * 商品上架--保存到索引中
     *
     * @param id
     */
    @Override
    public void addItemToSolr(Long id) {
        ItemQuery query = new ItemQuery();
        // 条件：根据商品id查询对应的库存，并且库存大于0的
        query.createCriteria().andGoodsIdEqualTo(id).andStatusEqualTo("1")
                .andIsDefaultEqualTo("1").andNumGreaterThan(0);
        List<Item> items = itemDao.selectByExample(query);
        if (items != null && items.size() > 0) {

            //设置动态字段
            for (Item item : items) {
                String spec = item.getSpec();
                Map<String, String> specMap = JSON.parseObject(spec, Map.class);
                item.setSpecMap(specMap);
            }
            solrTemplate.saveBeans(items);
            solrTemplate.commit();
        }
    }
}
