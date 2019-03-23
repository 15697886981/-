package com.alibaba.core.service.itemcat;

import com.alibaba.core.dao.item.ItemCatDao;
import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.pojo.item.ItemCatQuery;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    //注入dao
    @Resource
    private ItemCatDao itemCatDao;

    /**
     * 商品分类列表查询
     *
     * @param parentId
     * @return
     */
    @Override
    public List<ItemCat> findByParentId(Long parentId) {
        //设置查询条件
        ItemCatQuery query = new ItemCatQuery();
        query.createCriteria().andParentIdEqualTo(parentId);

        //查询
        List<ItemCat> itemCats = itemCatDao.selectByExample(query);
        return itemCats;
    }

    /**
     * 保存分类
     *
     * @param itemCat
     */
    @Override
    public void add(ItemCat itemCat) {
        itemCatDao.insertSelective(itemCat);
    }

    /**
     * 新增商品选择三级分类加载模板id
     *
     * @param id
     * @return
     */
    @Override
    public ItemCat findOne(Long id) {
        ItemCat itemCat = itemCatDao.selectByPrimaryKey(id);
        return itemCat;
    }

    /**
     * 查询所有分类
     *
     * @return
     */
    @Override
    public List<ItemCat> findAll() {

        return itemCatDao.selectByExample(null);
    }
}
