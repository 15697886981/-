package com.alibaba.core.service.brand;

import com.alibaba.core.dao.good.BrandDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.good.Brand;
import com.alibaba.core.pojo.good.BrandQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class BrandServiceImpl implements BrandService {
    //注入dao
    @Resource
    private BrandDao brandDao;

    @Override
    public List<Brand> findAll() {
        List<Brand> brands = brandDao.selectByExample(null);
        return brands;
    }

    /**
     * 分页查询所有品牌
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer pageNum, Integer pageSize) {
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);

        //查询结果集
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(null);

        //创建PageResult
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return PageResult
     */
    @Override
    public PageResult search(Integer pageNo, Integer pageSize, Brand brand) {
        // 1、设置分页条件
        PageHelper.startPage(pageNo, pageSize);
        // 2、设置查询条件：封装查询条件对象XxxQuery
        BrandQuery brandQuery = new BrandQuery();
        BrandQuery.Criteria criteria = brandQuery.createCriteria(); // 封装具体的查询条件对象
        // select * from tb_brand where name like "%xxxx%"
        // 拼接sql语句
        if (brand.getName() != null && !"".equals(brand.getName().trim())) {
            criteria.andNameLike("%" + brand.getName().trim() + "%");
        }
        if (brand.getFirstChar() != null && !"".equals(brand.getFirstChar().trim())) {
            criteria.andFirstCharEqualTo(brand.getFirstChar().trim());
        }
        // 根据id排序
        brandQuery.setOrderByClause("id desc");
        // 3、进行查询
        Page<Brand> page = (Page<Brand>) brandDao.selectByExample(brandQuery);
        // 4、将结果封装到PageResult中
        PageResult pageResult = new PageResult(page.getTotal(), page.getResult());
        return pageResult;
    }

    /**
     * 保存品牌
     *
     * @param brand
     * @return
     */
    @Transactional
    @Override
    public void add(Brand brand) {
        brandDao.insertSelective(brand);
    }

    /**
     * 回显品牌
     *
     * @param id
     * @return
     */
    @Override
    public Brand findOne(Long id) {
        return brandDao.selectByPrimaryKey(id);
    }

    /**
     * 修改品牌
     *
     * @param brand
     */
    @Transactional
    @Override
    public void update(Brand brand) {
        brandDao.updateByPrimaryKeySelective(brand);
    }

    /**
     * 删除品牌
     *
     * @param ids
     */
    @Transactional
    @Override
    public void delete(Long[] ids) {
        //判断
        if (ids != null && ids.length > 0) {
//            for (Long id : ids) {
//                brandDao.deleteByPrimaryKey(id);
//            }
            //批量删除
            brandDao.deleteByPrimaryKeys(ids);
        }
    }

    /**
     * 新增模板 初始化下拉列表
     *
     * @return
     */
    @Override
    public List<Map<String, String>> selectOptionList() {
        List<Map<String, String>> maps = brandDao.selectOptionList();
        return maps;
    }
}
