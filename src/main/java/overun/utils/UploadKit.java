package overun.utils;


import org.apache.commons.lang3.StringUtils;
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
     * @param multipartFile 请求对象
     * @param filePath 文件存放文件夹
     * @param useNewName 是否使用原文件名称还是使用随机生成的文件名称
     * @return
     */
    public static UploadFileEntity uploadFile(MultipartFile multipartFile, String filePath, boolean useNewName) {

        try {
            UploadFileEntity f = transferFile(filePath,multipartFile,useNewName);
            if(f!=null){
                return f;
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
     * @param file
     * @param useNewName
     * @return
     * @throws Exception
     */
    private static UploadFileEntity transferFile(String filePath,MultipartFile file, boolean useNewName) throws Exception{

        /** 取得上传文件 */
        String diskFileName = null;

        if (StringUtils.isBlank(file.getOriginalFilename())) {
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
