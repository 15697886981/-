package com.alibaba.core.controller.content;

import com.alibaba.core.pojo.ad.Content;
import com.alibaba.core.service.content.ContentService;
import com.alibaba.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/content")
public class ContentController {
    //注入service
    @Reference
    private ContentService contentService;

    /**
     * 首页大广告轮播图
     *
     * @param categoryId
     * @return
     */
    @RequestMapping("/findByCategoryId.do")
    public List<Content> findByCategoryId(Long categoryId) {
        return contentService.findByCategoryId(categoryId);
    }


}
