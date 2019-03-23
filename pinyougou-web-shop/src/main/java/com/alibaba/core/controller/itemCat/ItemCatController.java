package com.alibaba.core.controller.itemCat;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/itemCat")
public class ItemCatController {
    //注入service
    @Reference
    private ItemCatService itemCatService;

    /**
     * 商品分类列表查询
     *
     * @param parentId
     * @return
     */
    @RequestMapping("/findByParentId.do")
    public List<ItemCat> findByParentId(Long parentId) {
        List<ItemCat> list = itemCatService.findByParentId(parentId);
        return list;
    }

    /**
     * 新增商品选择三级分类加载模板id
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public ItemCat findOne(Long id) {
        return itemCatService.findOne(id);
    }


    /**
     * 查询所有分类
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }
}
