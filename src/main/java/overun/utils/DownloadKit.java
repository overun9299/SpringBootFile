package overun.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
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
        FileInputStream in = null;
        BufferedInputStream bis = null;
        OutputStream out = null;
        try {
            /** 解决下载文件名乱码 */
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/force-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            in = new FileInputStream(sourceFile);
            bis =new BufferedInputStream(in);
            out = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int readTmp = bis.read(buffer);
            while (readTmp != -1) {
                out.write(buffer, 0, readTmp);
                readTmp = bis.read(buffer);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(fileName + " 下载出错,若有问题请与管理员联系");
        } finally {
            out.close();
            in.close();
            bis.close();
        }
        return true;
    }

    /**
     * Java通过URL下载网络文件，获取文件流并修改文件名
     * @param urlM
     * @param name
     * @param request
     * @param response
     * @return 参考https://blog.csdn.net/wu8439512/article/details/78481479
     * @throws Exception
     */
    public static void downloadNetFile(String urlM, String name, HttpServletRequest request , HttpServletResponse response) throws Exception{

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        /** 如果不存在乱码的情况可以忽略，由于请求参数文件名为中文，到后台会乱码，考虑操作系统和客户端浏览器默认编码 */
        /** 判断服务器操作系统，本地开发使用windows */
        String os = System.getProperty("os.name");
        if(os.toLowerCase().indexOf("windows") != -1){
            name = new String(name.getBytes("GBK"), "ISO-8859-1");
        }else{
            /** 判断浏览器 */
            String userAgent = request.getHeader("User-Agent").toLowerCase();
            if(userAgent.indexOf("msie") > 0){
                name = URLEncoder.encode(name, "ISO-8859-1");
            }
        }
        /** 响应二进制流 */
        response.setContentType("application/octet-stream");
        /** 清除response中的缓存 */
        response.reset();
        /** 根据网络文件地址创建URL */
        URL url = new URL(urlM);
        /** 获取此路径的连接 */
        URLConnection conn = url.openConnection();
        /** 获取文件大小 */
        Long fileLength = conn.getContentLengthLong();
        /** 设置reponse响应头，真实文件名重命名，就是在这里设置，设置编码 */
        response.setHeader("Content-Disposition",
                "attachment; filename=" + name);
        response.setHeader("Content-Length", String.valueOf(fileLength));
        /** 构造读取流 */
        bis = new BufferedInputStream(conn.getInputStream());
        /** 构造输出流 */
        bos = new BufferedOutputStream(response.getOutputStream());
        byte[] buff = new byte[1024];
        int bytesRead;
        /** 每次读取缓存大小的流，写到输出流 */
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            bos.write(buff, 0, bytesRead);
        }
        /** 将所有的读取的流返回给客户端 */
        response.flushBuffer();

        bis.close();

        bos.close();
    }
}
