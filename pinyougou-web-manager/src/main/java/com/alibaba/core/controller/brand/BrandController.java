package com.alibaba.core.controller.brand;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.good.Brand;
import com.alibaba.core.service.brand.BrandService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/brand")
public class BrandController {

    @Reference
    private BrandService brandService;

    /**
     * 查询所有品牌
     *
     * @return
     */
    @RequestMapping("/findAll.do")
    public List<Brand> findAll() {
        return brandService.findAll();
    }

    /**
     * 分页查询
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/findPage.do")
    public PageResult findPage(Integer pageNo, Integer pageSize) {
        PageResult pageResult = brandService.findPage(pageNo, pageSize);
        return pageResult;
    }

    /**
     * 条件查询
     *
     * @param pageNo
     * @param pageSize
     * @param brand
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer pageNo, Integer pageSize, @RequestBody Brand brand) {
        PageResult result = brandService.search(pageNo, pageSize, brand);
        return result;
    }

    /**
     * 保存品牌
     *
     * @param brand
     * @return
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody Brand brand) {
        try {
            brandService.add(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    /**
     * 回显品牌
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public Brand findOne(Long id) {
        Brand brand = brandService.findOne(id);
        return brand;
    }

    /**
     * 修改品牌
     *
     * @param brand
     * @return
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody Brand brand) {
        try {
            brandService.update(brand);
            return new Result(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "保存失败");
        }
    }

    /**
     * 删除品牌
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            brandService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 新增模板 初始化下拉列表
     *
     * @return
     */
    @RequestMapping("/selectOptionList.do")
    public List<Map<String, String>> selectOptionList() {
        List<Map<String, String>> maps = brandService.selectOptionList();
        return maps;
    }
}
