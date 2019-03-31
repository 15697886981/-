package com.alibaba.core.service.staticpage;

import com.alibaba.core.dao.good.GoodsDao;
import com.alibaba.core.dao.good.GoodsDescDao;
import com.alibaba.core.dao.item.ItemCatDao;
import com.alibaba.core.dao.item.ItemDao;
import com.alibaba.core.pojo.good.Goods;
import com.alibaba.core.pojo.good.GoodsDesc;
import com.alibaba.core.pojo.item.Item;
import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.pojo.item.ItemQuery;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName StaticPageServiceImpl
 * @Description 生成静态页的实现
 * @Author 传智播客
 * @Date 10:00 2019/3/28
 * @Version 2.1
 **/
public class StaticPageServiceImpl implements StaticPageService, ServletContextAware{

    // StaticPageServiceImpl的bean：通过配置文件去管理该bean。

    @Resource
    private GoodsDao goodsDao;

    @Resource
    private GoodsDescDao goodsDescDao;

    @Resource
    private ItemDao itemDao;

    @Resource
    private ItemCatDao itemCatDao;

    private Configuration configuration;
    // 注入FreeMarkerConfigurer，好处：获取configuration 指定模板位置
    public void setFreeMarkerConfigurer(FreeMarkerConfigurer freeMarkerConfigurer) {
        this.configuration = freeMarkerConfigurer.getConfiguration();
    }

    private ServletContext servletContext;
    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }
    /**
     * @author 栗子
     * @Description 生成静态页
     * @Date 10:00 2019/3/28
     * @param id
     * @return void
     **/
    @Override
    public void getHtml(Long id) {

        try {
            // 1、创建Configuration并且指定模板的位置
            // new Configuration：指定模板的位置，硬编码了。 此路不通
            // 模板的位置硬编码了：联想---在配置文件中指定模板的位置--->springmvc视图解析器
            // springmvc支持的视图：jsp、其他（freemarker、velocity等）
            // 2、获取该位置下的模板
            Template template = configuration.getTemplate("item.ftl");
            // 3、准备数据
            Map<String, Object> dataModel = getDataModel(id);
            // 创建file：指定静态页生成的位置
            // 生成的静态页位置：可以直接访问。存在项目发布的真实的路径下
            // 获取项目发布的真实路径：
            // request.getRealPath(xxx)：此路不通
            // 其他的路：ServletContext.getRealPath(xxx)
            String pathname = "/" + id + ".html";
            String path = servletContext.getRealPath(pathname);
            File file = new File(path);
            // 4、模板（模板：编码格式） + 数据 = 输出（静态页：编码格式一致）
            template.process(dataModel, new OutputStreamWriter(new FileOutputStream(file),"UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // 获取模板需要的数据
    private Map<String,Object> getDataModel(Long id) {
        Map<String,Object> map = new HashMap<>();
        // 商品基本信息
        Goods goods = goodsDao.selectByPrimaryKey(id);
        map.put("goods", goods);
        // 商品描述信息
        GoodsDesc goodsDesc = goodsDescDao.selectByPrimaryKey(id);
        map.put("goodsDesc", goodsDesc);
        // 商品分类信息
        ItemCat itemCat1 = itemCatDao.selectByPrimaryKey(goods.getCategory1Id());
        ItemCat itemCat2 = itemCatDao.selectByPrimaryKey(goods.getCategory2Id());
        ItemCat itemCat3 = itemCatDao.selectByPrimaryKey(goods.getCategory3Id());
        map.put("itemCat1", itemCat1);
        map.put("itemCat2", itemCat2);
        map.put("itemCat3", itemCat3);
        // 商品库存信息
        ItemQuery itemQuery = new ItemQuery();
        itemQuery.createCriteria().andGoodsIdEqualTo(id);
        List<Item> itemList = itemDao.selectByExample(itemQuery);
        map.put("itemList", itemList);
        return map;
    }


}
