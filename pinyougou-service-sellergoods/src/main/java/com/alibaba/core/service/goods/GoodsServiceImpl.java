package com.alibaba.core.service.goods;

import com.alibaba.core.dao.good.BrandDao;
import com.alibaba.core.dao.good.GoodsDao;
import com.alibaba.core.dao.good.GoodsDescDao;
import com.alibaba.core.dao.item.ItemCatDao;
import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.dao.seller.SellerDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.pojo.good.GoodsDesc;
import com.alibaba.core.pojo.good.GoodsQuery;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.item.ItemQuery;
import com.alibaba.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class GoodsServiceImpl implements GoodsService {

    //注入dao
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsDescDao goodsDescDao;
    @Resource
    private ItemDao itemDao;
    @Resource
    private ItemCatDao itemCatDao;
    @Resource
    private BrandDao brandDao;
    @Resource
    private SellerDao sellerDao;

    /**
     * 添加商品
     *
     * @param goodsVo
     */
    @Transactional
    @Override
    public void add(GoodsVo goodsVo) {
        //保存商品基本信息
        Goods goods = goodsVo.getGoods();
        goods.setAuditStatus("0");
        goodsDao.insertSelective(goods);//返回主键自增的id

        //保存商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        //获取主键id
        goodsDesc.setGoodsId(goods.getId());
        goodsDescDao.insertSelective(goodsDesc);

        //TODO 保存商品库存信息03/22

        /*保存商品库存信息*/
        //判断是否启用规格
        if ("1".equals(goods.getIsEnableSpec())) {
            //启用规格
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    //获取商品标题
                    String title = goods.getGoodsName() + " " + goods.getCaption();

                    //数据：{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();

                    Map map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    //商品标题
                    item.setTitle(title);

                    /*设置库存属性*/
                    //图片
                    String itemImages = goodsDesc.getItemImages();
                    List<Map> images = JSON.parseArray(itemImages, Map.class);
                    if (images != null && images.size() > 0) {
                        String image = images.get(0).get("url").toString();
                        item.setImage(image);
                    }


                    //三级分类id
                    item.setCategoryid(goods.getCategory3Id());
                    //库存商品的状态
                    item.setStatus("1");
                    //创建时间
                    item.setCreateTime(new Date());
                    //更新时间
                    item.setUpdateTime(new Date());
                    //spu的id
                    item.setGoodsId(goods.getId());
                    //商家id
                    item.setSellerId(goods.getSellerId());


                    //三级分类名称
                    item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
                    //品牌名称
                    item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
                    //商家的店铺名称
                    item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getName());



                    /*保存*/
                    itemDao.insertSelective(item);
                }
            }
        } else {
            //未启用的规格
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setIsDefault("1");
            item.setSpec("{}");


            //三级分类id
            item.setCategoryid(goods.getCategory3Id());
            //库存商品的状态
            item.setStatus("1");
            //创建时间
            item.setCreateTime(new Date());
            //更新时间
            item.setUpdateTime(new Date());
            //spu的id
            item.setGoodsId(goods.getId());
            //商家id
            item.setSellerId(goods.getSellerId());


            //三级分类名称
            item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
            //品牌名称
            item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
            //商家的店铺名称
            item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getName());


            itemDao.insertSelective(item);

        }
    }

    /**
     * 查询商品列表信息
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Goods goods) {

        // 设置分页条件
        PageHelper.startPage(page, rows);

        // 设置查询条件
        GoodsQuery query = new GoodsQuery();
        GoodsQuery.Criteria criteria = query.createCriteria();
        if (goods.getGoodsName() != null && !"".equals(goods.getGoodsName().trim())) {
            criteria.andGoodsNameEqualTo(goods.getGoodsName().trim());
        }

        if (goods.getSellerId() != null) {
            criteria.andSellerIdEqualTo(goods.getSellerId()); // 查询当前商家下的商品列表
        }

        //排序
        query.setOrderByClause("id desc");

        //封装到结果集
        Page<Goods> p = (Page<Goods>) goodsDao.selectByExample(query);

        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 回显商品
     *
     * @param id
     * @return
     */
    @Override
    public GoodsVo findOne(Long id) {
        GoodsVo goodsVo = new GoodsVo();

        //回显基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        goodsVo.setGoods(goods);

        //回显描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        goodsVo.setGoodsDesc(goodsDesc);

        //回显商品库存信息
        ItemQuery query = new ItemQuery();
        query.createCriteria().andGoodsIdEqualTo(id);
        List<Item> items = itemDao.selectByExample(query);
        goodsVo.setItemList(items);

        return goodsVo;
    }

    /**
     * 修改商品
     *
     * @param goodsVo
     */
    @Transactional
    @Override
    public void update(GoodsVo goodsVo) {
        Goods goods = goodsVo.getGoods();
        //商家修改后需要重新审核
        goods.setAuditStatus("0");
        goodsDao.updateByPrimaryKeySelective(goods);

        //更新商品描述信息
        GoodsDesc goodsDesc = goodsVo.getGoodsDesc();
        goodsDescDao.updateByPrimaryKeySelective(goodsDesc);


        //更新商品库存信息
        //先删除库存
        ItemQuery query = new ItemQuery();
        query.createCriteria().andGoodsIdEqualTo(goods.getId());
        itemDao.deleteByExample(query);

        //再插入
        //IsEnableSpec代表是否启用规格
        if ("1".equals(goods.getIsEnableSpec())) {
            //启用规格
            List<Item> itemList = goodsVo.getItemList();
            if (itemList != null && itemList.size() > 0) {
                for (Item item : itemList) {
                    //获取商品标题
                    String title = goods.getGoodsName() + " " + goods.getCaption();

                    //数据：{"机身内存":"16G","网络":"联通3G"}
                    String spec = item.getSpec();

                    Map map = JSON.parseObject(spec, Map.class);
                    Set<Map.Entry<String, String>> entries = map.entrySet();
                    for (Map.Entry<String, String> entry : entries) {
                        title += " " + entry.getValue();
                    }
                    //商品标题
                    item.setTitle(title);

                    /*设置库存属性*/
                    //图片
                    String itemImages = goodsDesc.getItemImages();
                    List<Map> images = JSON.parseArray(itemImages, Map.class);
                    if (images != null && images.size() > 0) {
                        String image = images.get(0).get("url").toString();
                        item.setImage(image);
                    }


                    //三级分类id
                    item.setCategoryid(goods.getCategory3Id());
                    //库存商品的状态
                    item.setStatus("1");
                    //创建时间
                    item.setCreateTime(new Date());
                    //更新时间
                    item.setUpdateTime(new Date());
                    //spu的id
                    item.setGoodsId(goods.getId());
                    //商家id
                    item.setSellerId(goods.getSellerId());


                    //三级分类名称
                    item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
                    //品牌名称
                    item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
                    //商家的店铺名称
                    item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getName());



                    /*保存*/
                    itemDao.insertSelective(item);
                }
            }
        } else {
//未启用的规格
            Item item = new Item();
            item.setTitle(goods.getGoodsName() + " " + goods.getCaption());
            item.setPrice(goods.getPrice());
            item.setNum(9999);
            item.setIsDefault("1");
            item.setSpec("{}");


            //三级分类id
            item.setCategoryid(goods.getCategory3Id());
            //库存商品的状态
            item.setStatus("1");
            //创建时间
            item.setCreateTime(new Date());
            //更新时间
            item.setUpdateTime(new Date());
            //spu的id
            item.setGoodsId(goods.getId());
            //商家id
            item.setSellerId(goods.getSellerId());


            //三级分类名称
            item.setCategory(itemCatDao.selectByPrimaryKey(goods.getCategory3Id()).getName());
            //品牌名称
            item.setBrand(brandDao.selectByPrimaryKey(goods.getBrandId()).getName());
            //商家的店铺名称
            item.setSeller(sellerDao.selectByPrimaryKey(goods.getSellerId()).getName());


            itemDao.insertSelective(item);
        }
    }
}
