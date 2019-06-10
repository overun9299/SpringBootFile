package overun.utils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * @ClassName: DownloadKit
 * @Description: 文件下载
 * @author: 薏米滴答-西安-ZhangPY
 * @version: V1.0
 * @date: 2019/6/7 17:23
 * @Copyright: 2019 www.yimidida.com Inc. All rights reserved.
 */
public class DownloadKit {


    /**
     * 文件下载
     * @param response
     * @param sourcePath
     * @param fileName
     * @return
     */
    public static boolean download(HttpServletResponse response, String sourcePath, String fileName) throws IOException{
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new RuntimeException("您下载的资源已不存在");
        }
        DataInputStream   in = null;
        ServletOutputStream  out = null;
        try {
            /** 解决下载文件名乱码 */
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            in = new DataInputStream(new FileInputStream(sourceFile));
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int readTmp = 0;
            while ((readTmp = in.read(buffer)) != -1) {
                out.write(buffer, 0, readTmp);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(fileName + " 下载出错,若有问题请与管理员联系");
        } finally {
            out.close();
            in.close();
        }
        return true;
    }
}
