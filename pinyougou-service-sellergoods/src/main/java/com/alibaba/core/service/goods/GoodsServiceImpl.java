package com.alibaba.core.service.goods;

import com.alibaba.core.dao.good.GoodsDao;
import com.alibaba.core.dao.good.GoodsDescDao;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.pojo.good.GoodsDesc;
import com.alibaba.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;

@Service
public class GoodsServiceImpl implements GoodsService {

    //注入dao
    @Resource
    private GoodsDao goodsDao;
    @Resource
    private GoodsDescDao goodsDescDao;

    /**
     * 添加商品
     *
     * @param goodsVo
     */
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

        //TODO 保存商品库存信息
    }
}
