package com.alibaba.core.service.addr;

import com.alibaba.core.dao.address.AddressDao;
import com.alibaba.core.pojo.address.Address;
import com.alibaba.core.pojo.address.AddressQuery;
import com.alibaba.dubbo.config.annotation.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName AddrServiceImpl
 * @Description 收件人地址实现
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 22:36 2019/4/4
 * @Version 2.1
 **/
@Service
public class AddrServiceImpl implements AddrService {

    @Resource
    private AddressDao addressDao;

    /**
     * 根据当前收件人地址列表
     *
     * @param userId
     * @return
     */
    @Override
    public List<Address> findListByLoginUser(String userId) {
        AddressQuery query = new AddressQuery();
        query.createCriteria().andUserIdEqualTo(userId);
        return addressDao.selectByExample(query);
    }
}
