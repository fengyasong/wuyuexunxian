package com.aicat.seekfairy.controller;

import com.aicat.seekfairy.utils.FileUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

@RestController
@RequestMapping("down")
public class DownloadController {
    /**
     * 下载文件
     */
    @RequestMapping("{url}")
    public void downloadFile(@PathVariable("url") String url ,HttpServletRequest request, HttpServletResponse response) {
        File file = new File(url);
        String fileName = file.getName();
        FileUtils.downloadFile(fileName,file,response,request);
    }

}
