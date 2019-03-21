package com.alibaba.core.controller.itemcat;

import com.alibaba.core.pojo.item.ItemCat;
import com.alibaba.core.service.itemcat.ItemCatService;
import com.alibaba.dubbo.config.annotation.Reference;
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
}
