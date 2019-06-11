package overun.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import overun.entity.UploadFileEntity;
import overun.utils.DownloadKit;
import overun.utils.UploadKit;
import sun.awt.SunHints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: fileController
 * @Description:
 * @author: 薏米滴答-西安-ZhangPY
 * @version: V1.0
 * @date: 2019/6/10 10:44
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */

@RestController
@RequestMapping(value = "/file")
@Api(value = "测试文件上传下载", tags = "测试文件上传下载")
public class fileController {


    @ApiOperation("测试文件上传")
    @PostMapping(value = "/upload" ,consumes = "multipart/*",headers="content-type=multipart/form-data")
    public void fileUpload(@ApiParam(value = "上传的文件"  , required = true) MultipartFile multipartFile , HttpServletRequest request) {
        UploadFileEntity file = UploadKit.uploadFile(multipartFile, "D:\\uploadTest", false);
        System.out.println(file.toString());
    }

    @ApiOperation("测试文件下载")
    @GetMapping(value = "/download")
    public void fileDownLoad(HttpServletResponse response) {
        try {
            DownloadKit.download(response,"D:\\uploadTest\\[1560147654953] - 20171229094846.png","586.png");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
