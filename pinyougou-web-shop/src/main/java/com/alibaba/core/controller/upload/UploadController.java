package com.alibaba.core.controller.upload;

import com.alibaba.core.entity.Result;
import com.alibaba.utils.core.fdfs.FastDFSClient;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class UploadController {

    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;

    /**
     * 附件上传
     *
     * @param file
     * @return
     */
    @RequestMapping("/uploadFile.do")
    public Result uploadFile(MultipartFile file) {
        try {
            // 将附件上传到FastDFS上
            String conf = "classpath:fastDFS/fdfs_client.conf";
            FastDFSClient fastDFSClient = new FastDFSClient(conf);
            // 附件扩展名
            String filename = file.getOriginalFilename(); // xxx.jpg
            String extName = FilenameUtils.getExtension(filename);
            String path = fastDFSClient.uploadFile(file.getBytes(), extName, null);
            // http
            // http://192.168.200.128/
            // java：维护常量  配置文件、枚举、接口（对修改关闭，对扩展开发）
            String url = FILE_SERVER_URL + path;
//            String url = "http://192.168.200.128/" + path;
            return new Result(true, url);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "上传失败");
        }

    }
}
