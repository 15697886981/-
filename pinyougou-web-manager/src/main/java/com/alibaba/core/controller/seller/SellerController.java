package com.alibaba.core.controller.seller;

import com.alibaba.core.entity.PageResult;
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
        //回显
        return sellerService.findOne(id);
    }

    /**
     * 审核商家
     *
     * @param sellerId
     * @param status   0:待审核 1:审核通过 2:审核未通过 3关闭商家
     */
    @RequestMapping("/updateStatus")
    public Result updateStatus(String sellerId, String status) {
        try {
            sellerService.updateStatus(sellerId, status);
            return new Result(true, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "操作失败");
        }
    }
}
