package com.alibaba.core.service.content;

import com.alibaba.core.dao.ad.ContentDao;
import com.alibaba.core.entity.PageResult;
import com.alibaba.core.pojo.ad.Content;
import com.alibaba.core.pojo.ad.ContentQuery;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private ContentDao contentDao;

    @Override
    public List<Content> findAll() {
        List<Content> list = contentDao.selectByExample(null);
        return list;
    }

    @Override
    public PageResult findPage(Content content, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<Content> page = (Page<Content>) contentDao.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public void add(Content content) {
        contentDao.insertSelective(content);
    }

    @Override
    public void edit(Content content) {
        contentDao.updateByPrimaryKeySelective(content);
    }

    @Override
    public Content findOne(Long id) {
        Content content = contentDao.selectByPrimaryKey(id);
        return content;
    }

    @Override
    public void delAll(Long[] ids) {
        if (ids != null) {
            for (Long id : ids) {
                contentDao.deleteByPrimaryKey(id);
            }
        }
    }

    /**
     * 首页大广告轮播图
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<Content> findByCategoryId(Long categoryId) {

        // 设置条件：根据广告分类查询，并且是可用的
        ContentQuery query = new ContentQuery();
        query.createCriteria().andCategoryIdEqualTo(categoryId).andStatusEqualTo("1");


        List<Content> list = contentDao.selectByExample(query);

        //设置排序
        query.setOrderByClause("sort_order desc");

        //设置查询
        return list;
    }


}
