package com.alibaba.core.service;

import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.core.service.seller.SellerService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.HashSet;
import java.util.Set;


public class UserDetailServiceImpl implements UserDetailsService {

    //注入sellerService

    private SellerService sellerService;


    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    /**
     * 认证+授权
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //认证
        Seller seller = sellerService.findOne(username);
        //商家必须存在,并且商家审核通过
        if (seller != null && "1".equals(seller.getStatus())) {
            /*认证成功,开始授权*/
            Set<GrantedAuthority> authorities = new HashSet<>();
            SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_SELLER");

            authorities.add(grantedAuthority);
            User user = new User(username, seller.getPassword(), authorities);
            return user;
        }
        return null;
    }
}
