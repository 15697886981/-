package com.alibaba.core.controller.seller;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.core.service.seller.SellerService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/seller")
public class SellerController {

    //注入service
    @Reference
    private SellerService sellerService;

    /**
     * 运营商  查询未审核商家
     *
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    @RequestMapping("/search")
    public PageResult search(Integer page, Integer rows, @RequestBody Seller seller) {
        return sellerService.search(page, rows, seller);
    }

    /**
     * 回显商家
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne")
    public Seller findOne(String id) {
        return sellerService.findOne(id);
    }
}
