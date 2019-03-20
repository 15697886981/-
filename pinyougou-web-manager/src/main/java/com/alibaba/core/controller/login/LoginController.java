package com.alibaba.core.controller.login;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    /**
     * 显示当前的登录人
     *
     * @return
     */
    @RequestMapping("/showName.do")
    public Map<String, String> showName() {
        // 用户信息在哪：springsecurity容器中
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        return map;
    }
}
