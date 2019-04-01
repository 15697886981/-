package com.alibaba.core.controller;

import com.alibaba.core.entity.Result;
import com.alibaba.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.utils.core.checkphone.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName UserController
 * @Description
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 20:44 2019/3/31
 * @Version 2.1
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;

    @RequestMapping("sendCode.do")
    public Result sendCode(String phone) {

        boolean chinaPhoneLegal = PhoneFormatCheckUtils.isPhoneLegal(phone);
        if (!chinaPhoneLegal) {
            return new Result(false, "手机号不合法!");
        }


        try {
            userService.sendCode(phone);
            return new Result(true, "发送成功!");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "发送失败!");
        }

    }


}
