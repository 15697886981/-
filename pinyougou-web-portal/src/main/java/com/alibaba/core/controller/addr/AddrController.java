package com.alibaba.core.controller.addr;

import com.alibaba.core.pojo.address.Address;
import com.alibaba.core.service.addr.AddrService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @ClassName AddrController
 * @Description 加载收件人地址列表
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 22:42 2019/4/4
 * @Version 2.1
 **/
@RestController
@RequestMapping("/address")
public class AddrController {
    @Reference
    private AddrService addrService;

    @RequestMapping("/findListByLoginUser")
    public List<Address> findListByLoginUser() {
        String userId = SecurityContextHolder.getContext().getAuthentication().getName();
        return addrService.findListByLoginUser(userId);
    }

}
