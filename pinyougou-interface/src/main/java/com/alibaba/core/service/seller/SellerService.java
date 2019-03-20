package com.alibaba.core.service.seller;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.seller.Seller;

public interface SellerService {
    /**
     * 商家入驻
     *
     * @param seller
     */
    void add(Seller seller);

    /**
     * 运营商  查询未审核商家
     *
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    PageResult search(Integer page, Integer rows, Seller seller);

    /**
     * 回显商家
     *
     * @param sellerId
     * @return
     */
    Seller findOne(String sellerId);

    /**
     * 审核商家
     *
     * @param sellerId
     * @param status   0:待审核 1:审核通过 2:审核未通过 3关闭商家
     */
    void updateStatus(String sellerId, String status);
}
