package com.aicat.seekfairy.controller;

import com.aicat.common.enums.FileTypeEnum;
import com.aicat.common.utils.R;
import com.aicat.common.utils.UnPackeUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@RestController
@RequestMapping("package")
public class UnpackController {

    @PostMapping("pack")
    R pack(@RequestParam String fromFile, String toFile){
        File file = new File(fromFile);
        if(!file.exists()) return R.error("文件不存在");
        boolean b = UnPackeUtils.zip(fromFile, toFile,false);
        return R.operate(b);
    }
    @GetMapping("packDown")
    void packDown(@RequestParam String fromFile, HttpServletRequest request, HttpServletResponse response){
        File file = new File(fromFile);
        String fileName = file.getName();
        UnPackeUtils.zipDownload(fileName+".zip",fromFile,response,request);
    }
    @PostMapping("unpack")
    R unpack(@RequestParam MultipartFile zipFile, String destPath,String password){
        if (null == zipFile) {
            return R.error("请上传压缩文件!");
        }
        boolean isZipPack = true;
        String fileContentType = zipFile.getContentType();
        //将压缩包保存在指定路径
        String packFilePath = destPath + File.separator + zipFile.getName();
        if (FileTypeEnum.FILE_TYPE_ZIP.type.equals(fileContentType)) {
            //zip解压缩处理
            packFilePath += FileTypeEnum.FILE_TYPE_ZIP.fileStufix;
        } else if (FileTypeEnum.FILE_TYPE_RAR.type.equals(fileContentType)) {
            //rar解压缩处理
            packFilePath += FileTypeEnum.FILE_TYPE_RAR.fileStufix;
            isZipPack = false;
        } else {
            return R.error("上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        }
        File file = new File(packFilePath);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            zipFile.transferTo(file);
        } catch (IOException e) {
            log.error("zip file save to " + destPath + " error", e.getMessage(), e);
            return R.error("保存压缩文件到:" + destPath + " 失败!");
        }
        if (isZipPack) {
            //zip压缩包
            UnPackeUtils.unPackZip(file, password, destPath);
        } else {
            //rar压缩包
            UnPackeUtils.unPackRar(file, destPath);
        }
        //解压完，删除压缩包，遍历解压后的文件
        file.delete();
        liFile(file.getParentFile());
        return R.ok("解压成功");
    }
    //递归遍历文件
    private void liFile(@NotNull File file){
        if(file.isFile()){
            log.info("文件名={}",file.getName());
        }else {
            File[] files = file.listFiles();
            Arrays.stream(files).forEach(e->liFile(e));
        }
    }

}
