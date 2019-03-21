package com.alibaba.core.controller.goods;

import com.alibaba.core.entity.Result;
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
}
