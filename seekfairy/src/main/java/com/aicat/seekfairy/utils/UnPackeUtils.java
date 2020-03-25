package com.aicat.seekfairy.utils;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import lombok.extern.slf4j.Slf4j;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.outputstream.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;


@Slf4j
public class UnPackeUtils {
    /**
     *     解压密码
     */
    private static String PASSWORD="";

    /**
     * zip文件解压
     *
     * @param destPath 解压文件路径
     * @param zipFile  压缩文件
     * @param password 解压密码(如果有)
     */
    public static void unPackZip(File zipFile, String password, String destPath) {
        try {
            ZipFile zip = new ZipFile(zipFile);
            /*zip4j默认用GBK编码去解压*/
            zip.setCharset(Charset.forName("UTF-8"));
            log.info("begin unpack zip file....");
            zip.extractAll(destPath);
            // 如果解压需要密码
            if (zip.isEncrypted()) {
                zip.setPassword(password.toCharArray());
            }
        } catch (Exception e) {
            log.error("unPack zip file to " + destPath + " fail ....", e.getMessage(), e);
        }
    }

    /**
     * rar文件解压(不支持有密码的压缩包)
     *
     * @param rarFile  rar压缩包
     * @param destPath 解压保存路径
     */
    public static void unPackRar(File rarFile, String destPath) {
        try (Archive archive = new Archive(new FileInputStream(rarFile))) {
            if (null != archive) {
                FileHeader fileHeader = archive.nextFileHeader();
                File file = null;
                while (null != fileHeader) {
                    // 防止文件名中文乱码问题的处理
                    String fileName = fileHeader.getFileNameW().isEmpty() ? fileHeader.getFileNameString() : fileHeader.getFileNameW();
                    if (fileHeader.isDirectory()) {
                        //是文件夹
                        file = new File(destPath + File.separator + fileName);
                        file.mkdirs();
                    } else {
                        //不是文件夹
                        file = new File(destPath + File.separator + fileName.trim());
                        if (!file.exists()) {
                            if (!file.getParentFile().exists()) {
                                // 相对路径可能多级，可能需要创建父目录.
                                file.getParentFile().mkdirs();
                            }
                            file.createNewFile();
                        }
                        FileOutputStream os = new FileOutputStream(file);
                        archive.extractFile(fileHeader, os);
                        os.close();
                    }
                    fileHeader = archive.nextFileHeader();
                }
            }
        } catch (Exception e) {
            log.error("unpack rar file fail....", e.getMessage(), e);
        }
    }



    /**
     * @Description
     *            <p>把文件fromFile压缩为toFile</p>
     *            <p>注：如果不覆盖，则会继续将新文件写入原有的zip包中</p>
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)upload/test/demo.zip
     * @param cover -> 是否覆盖原有文件，true=覆盖
     */
    public static boolean zip(String fromFile, String toFile, boolean cover){
        File zipFile = new File(toFile);
        if (!zipFile.getParentFile().exists()) {
            zipFile.getParentFile().mkdirs();
        }
        if (zipFile.exists() && cover) {
            zipFile.delete(); // 覆盖原有 文件
        }
        return doZip(new File(fromFile), toFile);
    }
    public static void zipDownload(String originalFileName, String filePath,
                                   HttpServletResponse response, HttpServletRequest request){
        File file = new File(filePath);
        // 数据校验
        FileUtils.checkParam(originalFileName,file);

        //相应头的处理
        //清空response中的输出流
        response.reset();
        //设置文件大小
        response.setContentLength((int) file.length());
        //设置Content-Type头
        response.setContentType("application/octet-stream;charset=UTF-8");
        //设置Content-Disposition头 以附件形式解析
        String encodedFilename = FileUtils.getEncodedFilename(request, originalFileName);
        response.addHeader("Content-Disposition", "attachment;filename=" + encodedFilename);

        //设置压缩流：直接写入response，实现边压缩边下载
        ZipOutputStream zipos = null;
        //循环将文件写入压缩流
        DataOutputStream os = null;
        InputStream is = null;
        try {
            zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
            ZipParameters param = new ZipParameters();
            // 设置压缩方式（默认方式）
            param.setCompressionMethod(CompressionMethod.DEFLATE);
            // 设置压缩级别
            param.setCompressionLevel(CompressionLevel.NORMAL);
            param.setFileNameInZip(originalFileName);
            zipos.putNextEntry(param);

            os = new DataOutputStream(zipos);
            byte[] b = new byte[1024];
            int length = 0;
            is = new FileInputStream(file);
            while((length = is.read(b))!= -1){
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //流的关闭
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(zipos != null){
                try {
                    zipos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (os != null) {
                try {
                    os.flush();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * @Description
     *            <p>把文件fromFile压缩为toFile</p>
     * @param fromFile -> 需要进行压缩的文件或文件夹地址(完整路径)
     * @param toFile -> 压缩后的文件(完整路径)
     */
    private static boolean doZip(File fromFile, String toFile){
        try {
            ZipFile zip=new ZipFile(toFile);
            zip.setCharset(Charset.forName("UTF-8"));
            ZipParameters param = new ZipParameters();
            // 设置压缩方式（默认方式）
            param.setCompressionMethod(CompressionMethod.DEFLATE);
            // 设置压缩级别
            param.setCompressionLevel(CompressionLevel.NORMAL);
            setupPassword(param);
            if (fromFile.isFile()){
                zip.addFile(fromFile, param);
            }else{
                zip.addFolder(fromFile, param);
            }
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @Description:
     *            <p>设置解压密码</p>
     * @param param -> ZipParameters 参数对象
     */
    private static void setupPassword(ZipParameters param) {
        if (PASSWORD != null && !"".equals(PASSWORD.trim())){
            // 设置加密文件
            param.setEncryptFiles(true);
            // 设置加密方式（必须要有加密算法）
            param.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD_VARIANT_STRONG);
            // 设置秘钥长度
            param.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            param.setFileComment(PASSWORD);
        }
    }
}
