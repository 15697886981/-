package com.alibaba.core.controller.seller;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.core.service.seller.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {
    //注入dao
    @Reference
    private SellerService sellerService;

    /**
     * 商家入驻
     *
     * @param seller
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Seller seller) {
        try {
            sellerService.add(seller);
            return new Result(true, "注册成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }
}
