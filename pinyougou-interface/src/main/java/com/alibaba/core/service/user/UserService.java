package com.alibaba.core.service.user;

import com.alibaba.core.pojo.user.User;

/**
 * @ClassName UserService
 * @Description 用户服务
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 19:14 2019/3/31
 * @Version 2.1
 **/

public interface UserService {
    /**
     * 发送验证码
     *
     * @param phone
     */
    void sendCode(String phone);

    /**
     * 用户注册
     * @param smscode
     * @param user
     */
    void add(String smscode, User user);
}
