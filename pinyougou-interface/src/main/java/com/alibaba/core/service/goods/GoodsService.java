package com.alibaba.core.service.goods;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.vo.GoodsVo;

public interface GoodsService {
    /**
     * 添加商品
     *
     * @param goodsVo
     */
    void add(GoodsVo goodsVo);


    /**
     * 查询商品列表信息
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    PageResult search(Integer page, Integer rows, Goods goods);

    /**
     * 回显商品
     *
     * @param id
     * @return
     */
    GoodsVo findOne(Long id);

    /**
     * 修改商品
     *
     * @param goodsVo
     */
    void update(GoodsVo goodsVo);

    /**
     * 运营商系统商品列表查询
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    PageResult searchForManager(Integer page, Integer rows, Goods goods);

    /**
     * 审核商品
     *
     * @param ids
     * @param status
     */
    void updateStatus(Long[] ids, String status);
}
