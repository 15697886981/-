package com.alibaba.core.controller.goods;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.service.goods.GoodsService;
import com.alibaba.core.vo.GoodsVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class goodsController {
    //注入service
    @Reference
    private GoodsService goodsService;

    /**
     * 添加商品
     *
     * @param goodsVo
     */
    @RequestMapping("add.do")
    public Result add(@RequestBody GoodsVo goodsVo) {
        try {

            //设置商家id
            String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
            goodsVo.getGoods().setSellerId(sellerId);
            goodsService.add(goodsVo);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
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
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Goods goods) {
        //获取商家id
        String sellerId = SecurityContextHolder.getContext().getAuthentication().getName();
        goods.setSellerId(sellerId);

        return goodsService.search(page, rows, goods);
    }


    /**
     * 回显商品
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public GoodsVo findOne(Long id) {
        return goodsService.findOne(id);
    }


    /**
     * 修改商品
     *
     * @param goodsVo
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody GoodsVo goodsVo) {
        try {
            goodsService.update(goodsVo);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }
}
