package com.alibaba.core.controller.temp;


import com.alibaba.core.entity.PageResult;
import com.alibaba.core.entity.Result;
import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.core.service.temp.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    //注入service
    @Reference
    private TypeTemplateService typeTemplateService;

    /**
     * 模版列表查询
     *
     * @param page
     * @param rows
     * @param typeTemplate
     * @return
     */
    @RequestMapping("/search.do")
    public PageResult search(Integer page, Integer rows, @RequestBody TypeTemplate typeTemplate) {
        return typeTemplateService.search(page, rows, typeTemplate);
    }

    /**
     * 新增模板
     *
     * @param typeTemplate
     */
    @RequestMapping("/add.do")
    public Result add(@RequestBody TypeTemplate typeTemplate) {
        try {
            typeTemplateService.add(typeTemplate);
            return new Result(true, "添加成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "添加失败");

        }
    }

    /**
     * 模板回显
     *
     * @param id
     * @return
     */
    @RequestMapping("/findOne.do")
    public TypeTemplate findOne(Long id) {
        return typeTemplateService.findOne(id);
    }

    /**
     * 修改模板
     *
     * @param typeTemplate
     */
    @RequestMapping("/update.do")
    public Result update(@RequestBody TypeTemplate typeTemplate) {
        try {
            typeTemplateService.update(typeTemplate);
            return new Result(true, "修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "修改失败");

        }
    }

    /**
     * 删除模板
     *
     * @param ids
     * @return
     */
    @RequestMapping("/delete.do")
    public Result delete(Long[] ids) {
        try {
            typeTemplateService.delete(ids);
            return new Result(true, "删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败");
        }
    }
}
