package com.alibaba.core.controller.goods;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.service.goods.GoodsService;
import com.alibaba.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    //注入service
    @Reference
    private GoodsService goodsService;
    @Reference
    private ItemCatService itemCatService;

    /**
     * 运营商系统商品列表查询
     *
     * @param page
     * @param rows
     * @param goods
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods) {
        return goodsService.search(page, rows, goods);
    }

    /**
     * 审核商品
     *
     * @param ids
     * @param status
     */
    @RequestMapping("/updateStatus.do")
    public Result updateStatus(Long[] ids, String status) {
        try {
            goodsService.updateStatus(ids, status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }

}
