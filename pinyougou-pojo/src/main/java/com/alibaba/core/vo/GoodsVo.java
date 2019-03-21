package com.alibaba.core.vo;

import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.pojo.good.GoodsDesc;
import com.alibaba.core.pojo.item.Item;

import java.io.Serializable;
import java.util.List;

public class GoodsVo implements Serializable {

    private Goods goods;            // 商品基本信息
    private GoodsDesc goodsDesc;    // 商品描述信息
    private List<Item> itemList;    // 商品库存信息

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public GoodsDesc getGoodsDesc() {
        return goodsDesc;
    }

    public void setGoodsDesc(GoodsDesc goodsDesc) {
        this.goodsDesc = goodsDesc;
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}

