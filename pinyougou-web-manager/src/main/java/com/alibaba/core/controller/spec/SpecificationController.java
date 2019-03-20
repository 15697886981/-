package com.alibaba.core.controller.spec;

import com.alibaba.core.entity.PageResult;
import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.specification.Specification;
import com.alibaba.core.service.spec.SpecService;
import com.alibaba.core.vo.SpecVo;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/specification")
public class SpecificationController {

    //注入service
    @Reference
    private SpecService specService;

    /**
     * 规格列表查询
     *
     * @param page
     * @param rows
     * @param specification
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody Specification specification) {
        PageResult pageResult = specService.search(page, rows, specification);
        return pageResult;
    }

    /**
     * 保存规格
     *
     * @param specVo
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody SpecVo specVo) {
        try {
            specService.add(specVo);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");
        }
    }

    /**
     * 规格回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public SpecVo findOne(Long id) {
        return specService.findOne(id);
    }

    /**
     * 修改规格
     *
     * @param specVo
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody SpecVo specVo) {
        try {
            specService.update(specVo);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");
        }
    }

    /**
     * 删除
     *
     * @param ids
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            specService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }

    /**
     * 添加模板 下拉选择产品规格
     *
     * @return
     */
    @RequestMapping("/selectOptionList")
    public List<Map<String, String>> selectOptionList() {
        return specService.selectOptionList();
    }

}
