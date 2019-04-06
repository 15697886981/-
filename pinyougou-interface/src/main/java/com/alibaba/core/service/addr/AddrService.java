package com.alibaba.core.service.addr;

import com.alibaba.core.pojo.address.Address;

import java.util.List;

/**
 * @ClassName AddrService
 * @Description 收件人地址服务
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 22:30 2019/4/4
 * @Version 2.1
 **/

public interface AddrService {
    /**
     * 根据当前收件人地址列表
     *
     * @param userId
     * @return
     */
    List<Address> findListByLoginUser(String userId);
}
