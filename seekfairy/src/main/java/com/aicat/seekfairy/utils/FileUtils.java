package com.aicat.seekfairy.utils;

import com.aicat.seekfairy.common.SeekFairyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
@Slf4j
public class FileUtils {

    public static String getFileEncode(String path) {
        String charset ="asci";
        byte[] first3Bytes = new byte[3];
        BufferedInputStream bis = null;
        try {
            boolean checked = false;
            bis = new BufferedInputStream(new FileInputStream(path));
            bis.mark(0);
            int read = bis.read(first3Bytes, 0, 3);
            if (read == -1)
                return charset;
            if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
                charset = "Unicode";//UTF-16LE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
                charset = "Unicode";//UTF-16BE
                checked = true;
            } else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB && first3Bytes[2] == (byte) 0xBF) {
                charset = "UTF8";
                checked = true;
            }
            bis.reset();
            if (!checked) {
                int len = 0;
                int loc = 0;
                while ((read = bis.read()) != -1) {
                    loc++;
                    if (read >= 0xF0)
                        break;
                    if (0x80 <= read && read <= 0xBF) //单独出现BF以下的，也算是GBK
                        break;
                    if (0xC0 <= read && read <= 0xDF) {
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF)
                            //双字节 (0xC0 - 0xDF) (0x80 - 0xBF),也可能在GB编码内
                            continue;
                        else
                            break;
                    } else if (0xE0 <= read && read <= 0xEF) { //也有可能出错，但是几率较小
                        read = bis.read();
                        if (0x80 <= read && read <= 0xBF) {
                            read = bis.read();
                            if (0x80 <= read && read <= 0xBF) {
                                charset = "UTF-8";
                                break;
                            } else
                                break;
                        } else
                            break;
                    }
                }
                //TextLogger.getLogger().info(loc + " " + Integer.toHexString(read));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ex) {
                }
            }
        }
        return charset;
    }

    private static String getEncode(int flag1, int flag2, int flag3) {
        String encode="";
        // txt文件的开头会多出几个字节，分别是FF、FE（Unicode）,
        // FE、FF（Unicode big endian）,EF、BB、BF（UTF-8）
        if (flag1 == 255 && flag2 == 254) {
            encode="Unicode";
        }
        else if (flag1 == 254 && flag2 == 255) {
            encode="UTF-16";
        }
        else if (flag1 == 239 && flag2 == 187 && flag3 == 191) {
            encode="UTF8";
        }
        else {
            encode="asci";// ASCII码
        }
        return encode;
    }

    /**
     * 通过路径获取文件的内容，这个方法因为用到了字符串作为载体，为了正确读取文件（不乱码），只能读取文本文件，安全方法！
     */
    public static String readFile(String path){
        String data = null;
        // 判断文件是否存在
        File file = new File(path);
        if(!file.exists()){
            return data;
        }
        // 获取文件编码格式
        String code = getFileEncode(path);
        InputStreamReader isr = null;
        try{
            // 根据编码格式解析文件
            if("asci".equals(code)){
                // 这里采用GBK编码，而不用环境编码格式，因为环境默认编码不等于操作系统编码
                // code = System.getProperty("file.encoding");
                code = "GBK";
            }
            isr = new InputStreamReader(new FileInputStream(file),code);
            // 读取文件内容
            int length = -1 ;
            char[] buffer = new char[1024];
            StringBuffer sb = new StringBuffer();
            while((length = isr.read(buffer, 0, 1024) ) != -1){
                sb.append(buffer,0,length);
            }
            data = new String(sb);
        }catch(Exception e){
            e.printStackTrace();
            log.info("getFile IO Exception:"+e.getMessage());
        }finally{
            try {
                if(isr != null){
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
                log.info("getFile IO Exception:"+e.getMessage());
            }
        }
        return data;
    }
    /**
     * 按照指定的路径和编码格式保存文件内容，这个方法因为用到了字符串作为载体，
     * 为了正确写入文件（不乱码），只能写入文本内容，安全方法
     * @param data
     *          将要写入到文件中的字节数据
     * @param path
     *          文件路径,包含文件名
     * @return boolean
     * 			当写入完毕时返回true;
     */
    public static boolean writeFile(byte data[], String path , String code){
        boolean flag = true;
        OutputStreamWriter osw = null;
        try{
            File file = new File(path);
            if(!file.exists()){
                file = new File(file.getParent());
                if(!file.exists()){
                    file.mkdirs();
                }
            }
            if("asci".equals(code)){
                code = "GBK";
            }
            osw = new OutputStreamWriter(new FileOutputStream(path),code);
            osw.write(new String(data,code));
            osw.flush();
        }catch(Exception e){
            e.printStackTrace();
            log.info("toFile IO Exception:"+e.getMessage());
            flag = false;
        }finally{
            try{
                if(osw != null){
                    osw.close();
                }
            }catch(IOException e){
                e.printStackTrace();
                log.info("toFile IO Exception:"+e.getMessage());
                flag = false;
            }
        }
        return flag;
    }
    /**
     * 读取文件
     * @param url
     * @param response
     */
    public void view(String url, HttpServletResponse response) {

        File file = new File(url);
        FileInputStream i = null;
        OutputStream o = null;

        try {
            i = new FileInputStream(file);
            o = response.getOutputStream();

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = i.read(buf)) > 0) {
                o.write(buf, 0, bytesRead);
                o.flush();
            }

            i.close();
            o.close();
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    /**
     * 重命名
     * @param url
     * @param toKey
     * @return
     */
    public String renameFile(String url, String toKey) {
        String result = copyFile(url, toKey);
        deleteFile(url);
        return result;
    }

    /**
     * 复制文件|字节流读取复制
     * @param url
     * @param toKey
     */
    public String copyFile(String url, String toKey) {

        File file = new File(url);
        FileInputStream i = null;
        FileOutputStream o = null;
        String result = "";
        try {
            i = new FileInputStream(file);
            o = new FileOutputStream(new File(file.getParentFile() + "/" + toKey));

            byte[] buf = new byte[1024];
            int bytesRead;

            while ((bytesRead = i.read(buf)) > 0) {
                o.write(buf, 0, bytesRead);
            }

            i.close();
            o.close();
            result = file.getParentFile() + "/" + toKey;
        } catch (IOException e) {
            log.error(e.toString());
        }
        return result;

    }

    /**
     * 删除文件
     * @param url
     */
    public void deleteFile(String url) {
        File file = new File(url);
        file.delete();
    }


    /**
     * 下载文件
     * @param originalFileName ：下载文件的原始文件名
     * @param file             ：下载的文件
     * @param response         ：相应对象
     */
    public static void downloadFile(String originalFileName, File file, HttpServletResponse response, HttpServletRequest request) {
        // 数据校验
        checkParam(originalFileName,file);

        //相应头的处理
        //清空response中的输出流
        //response.reset();
        //设置文件大小
        response.setContentLength((int) file.length());
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        String encodedFilename = getEncodedFilename(request, originalFileName);
        response.addHeader("Content-Disposition", "attachment;filename=" + encodedFilename);

        //将来文件流写入response中
        FileInputStream fileInputStream = null;
        ServletOutputStream outputStream = null;
        try {
            //获取文件输入流
            fileInputStream = new FileInputStream(file);
            //创建数据缓冲区
            byte[] buffers = new byte[1024];
            //通过response中获取ServletOutputStream输出流
            outputStream = response.getOutputStream();
            int length;
            while ((length = fileInputStream.read(buffers)) > 0) {
                //写入到输出流中
                outputStream.write(buffers, 0, length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //流的关闭
            if(fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 下载文件的参数的校验，如果参数不合法则抛出自定义异常
     * @param originalFileName ：文件原始文件名
     * @param file ：待下载的文件
     */
    private static void checkParam(String originalFileName, File file) {
        if(StringUtils.isBlank(originalFileName)){
            throw new SeekFairyException("输入的文件原始文件名为空");
        }
        if(file == null || !file.exists() ){
            throw new SeekFairyException("待在下载的文件不存在！");
        }
    }

    /**
     * 获取URL编码后的原始文件名
     * @param request ：客户端请求
     * @param originalFileName ：原始文件名
     * @return ：
     */
    private static String getEncodedFilename(HttpServletRequest request, String originalFileName) {
        String encodedFilename = null;
        String agent = request.getHeader("User-Agent");
        if(agent.contains("MSIE")){
            //IE浏览器
            try {
                encodedFilename = URLEncoder.encode(originalFileName, "utf-8");
                encodedFilename = encodedFilename.replace("+", " ");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(agent.contains("Firefox")){
            //火狐浏览器
            BASE64Encoder base64Encoder = new BASE64Encoder();
            encodedFilename = "=?utf-8?B?" + base64Encoder.encode(originalFileName.getBytes(StandardCharsets.UTF_8))+"?=";
        }else{
            //其他浏览器
            try {
                encodedFilename = URLEncoder.encode(originalFileName, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return encodedFilename;
    }
}
