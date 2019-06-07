package overun.utils;

import cn.minsin.core.tools.StringUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import overun.entity.UploadFileEntity;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Date;
import java.util.Iterator;

/**
 * @ClassName: UploadKit
 * @Description: 文件上传工具类
 * @author: 薏米滴答-西安-ZhangPY
 * @version: V1.0
 * @date: 2019/6/7 12:51
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
public class UploadKit {

    /**
     * 单文件上传
     * @param request 请求对象
     * @param filePath 文件存放文件夹
     * @param useNewName 是否使用原文件名称还是使用随机生成的文件名称
     * @return
     */
    public static UploadFileEntity getFile(HttpServletRequest request, String filePath, boolean useNewName) {

        /** 创建一个通用的多部分解析器 */
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());

        try {
            /** 判断 request 是否有文件上传,即多部分请求 */
            if (multipartResolver.isMultipart(request)) {
                /** 转换成多部分request */
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                /** 取得request中的所有文件名 */
                Iterator<String> fileNames = multiRequest.getFileNames();
                while (fileNames.hasNext()) {
                    UploadFileEntity f = transferFile(fileNames.next(),filePath,multiRequest,useNewName);
                    if(f!=null){
                        return f;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 封装文件信息
     * @param path
     * @param filePath
     * @param multiRequest
     * @param useNewName
     * @return
     * @throws Exception
     */
    private static UploadFileEntity transferFile(String path,String filePath,MultipartHttpServletRequest multiRequest, boolean useNewName) throws Exception{

        /** 取得上传文件 */
        String diskFileName = null;
        MultipartFile file = multiRequest.getFile(path);
        if (StringUtil.isBlank(file)) {
            return null;
        }
        String fileMD5 = fileMd5(file.getBytes());
        /** 原名称 带后缀 */
        String originalName = file.getOriginalFilename();
        /** 后缀名 */
        String fileSuffix = originalName.substring(originalName.lastIndexOf("."));
        /** 新名称 */
        String newName =  System.currentTimeMillis() + fileSuffix;
        /** 文件存放在磁盘上的最终名称、若文件使用原名称、使用时间缀解决文件重名问题 */
        if (!useNewName) {
            diskFileName = "["+System.currentTimeMillis() +"] - "+originalName;
        } else {
            diskFileName = newName;
        }
        /** 文件大小 */
        Long fileSize = file.getSize();
        File directory = new File(filePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        file.transferTo(new File(directory,diskFileName));
        UploadFileEntity upload = new UploadFileEntity();
        upload.setFileSize(fileSize.doubleValue());
        upload.setNewName(newName);
        upload.setOrigName(originalName);
        upload.setFileMd5(fileMD5);
        upload.setUploadTime(new Date());
        upload.setServerPath(filePath.concat(diskFileName));
        return upload;
    }


    /**
     * 返回上传文件的MD5
     * @param request
     * @return
     */
    public static String getFileMd5(HttpServletRequest request){
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        try {
            if (multipartResolver.isMultipart(request)) {
                MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
                Iterator<String> iter = multiRequest.getFileNames();
                while (iter.hasNext()) {
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if(null != file){
                        return fileMd5(file.getBytes());
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 计算MD5
     * @param bytes
     * @return
     */
    public static String fileMd5(byte[] bytes) {
        String value = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(bytes);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return value;
    }



    /**
     * 删除文件
     * @param path  文件路径
     * @return
     */
    public static boolean deleteFile(String path){
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }

}