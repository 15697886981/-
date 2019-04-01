package com.alibaba.core.controller;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.user.User;
import com.alibaba.core.service.user.UserService;
import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.utils.core.checkphone.PhoneFormatCheckUtils;
import org.springframework.web.bind.annotation.RequestBody;
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

    /**
     * 用户注册
     *
     * @param smscode
     * @param user
     */
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

    @RequestMapping("add.do")
    public Result add(String smscode, @RequestBody User user) {
        try {
            userService.add(smscode, user);
            return new Result(true, "注册成功");
        } catch (RuntimeException e) {
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "注册失败");
        }
    }

}
