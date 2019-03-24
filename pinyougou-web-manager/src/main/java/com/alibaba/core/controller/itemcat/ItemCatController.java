package com.alibaba.core.controller.itemcat;

import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.item.ItemCat;
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
     * 保存分类
     *
     * @param itemCat
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody ItemCat itemCat) {
        try {
            itemCatService.add(itemCat);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    @RequestMapping("/findAll.do")
    public List<ItemCat> findAll() {
        return itemCatService.findAll();
    }
}
