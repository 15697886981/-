package com.alibaba.core.service.staticpage;

/**
 * @ClassName StaticPageService
 * @Description 生成静态页接口
 * @Author 阿里巴巴（Mr-Chen）
 * @Date 17:48 2019/3/29
 * @Version 2.1
 **/

public interface StaticPageService {
    /**
     * 获取静态页的方法
     *
     * @param id 商品id
     */
    void getHtml(Long id);
}
