package com.alibaba.core.service.pay;

import java.util.Map;

/**
 * @ClassName PayService
 * @Description 生成支付二维码接口
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 16:02 2019/4/6
 * @Version 2.1
 **/

public interface PayService {
    /**
     * 生成支付二维码
     *
     * @param username
     * @return
     * @throws Exception
     */
    Map<String, String> createNative(String username) throws Exception;

    /**
     * 查询订单
     *
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    Map<String, String> queryPayStatus(String out_trade_no) throws Exception;
}
