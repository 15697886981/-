package com.alibaba.core.service.seller;

import com.alibaba.core.dao.seller.SellerDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.seller.Seller;
import com.alibaba.core.pojo.seller.SellerQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class SellerServiceImpl implements SellerService {
    //注入dao
    @Resource
    private SellerDao sellerDao;

    /**
     * 商家入驻
     *
     * @param seller
     */
    @Transactional
    @Override
    public void add(Seller seller) {
        //设置商家审核状态
        seller.setStatus("0");
        //注册日期
        seller.setCreateTime(new Date());
        //密码
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(seller.getPassword());
        seller.setPassword(password);
        //保存
        sellerDao.insertSelective(seller);
    }

    /**
     * 运营商  查询未审核商家
     *
     * @param page
     * @param rows
     * @param seller
     * @return
     */
    @Override
    public PageResult search(Integer page, Integer rows, Seller seller) {
        //设置分页参数
        PageHelper.startPage(page, rows);

        //设置查询条件
        SellerQuery query = new SellerQuery();
        if (seller.getStatus() != null && !"".equals(seller.getStatus().trim())) {
            query.createCriteria().andStatusEqualTo(seller.getStatus().trim());
        }
        //封装结果集
        Page<Seller> p = (Page<Seller>) sellerDao.selectByExample(query);
        return new PageResult(p.getTotal(), p.getResult());
    }

    /**
     * 回显商家
     *
     * @param sellerId
     * @return
     */
    @Override
    public Seller findOne(String sellerId) {

        return sellerDao.selectByPrimaryKey(sellerId);
    }

    /**
     * 审核商家
     *
     * @param sellerId
     * @param status   0:待审核 1:审核通过 2:审核未通过 3关闭商家
     */
    @Transactional
    @Override
    public void updateStatus(String sellerId, String status) {
        Seller seller = new Seller();

        seller.setStatus(status);
        seller.setSellerId(sellerId);

        sellerDao.updateByPrimaryKeySelective(seller);
    }
}
