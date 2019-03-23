package com.alibaba.core.controller.temp;


import com.alibaba.core.pojo.template.TypeTemplate;
import com.alibaba.core.service.temp.TypeTemplateService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/typeTemplate")
public class TypeTemplateController {
    //注入service
    @Reference
    private TypeTemplateService typeTemplateService;

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


    @RequestMapping("/findBySpecList.do")
    public List<Map> findBySpecList(Long id) {
        List<Map> list = typeTemplateService.findBySpecList(id);
        return list;
    }

}
