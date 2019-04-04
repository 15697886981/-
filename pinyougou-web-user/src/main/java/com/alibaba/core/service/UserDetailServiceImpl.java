package com.alibaba.core.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName UserDetailServiceImpl
 * @Description 授权
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 15:12 2019/4/2
 * @Version 2.1
 **/

public class UserDetailServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // 添加访问权限
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(grantedAuthority);
        User user = new User(username, "", authorities);
        return user;
    }
}

