package com.xueyi.file.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.file.FileUtils;
import com.xueyi.file.service.ISysFileService;
import com.xueyi.system.api.domain.material.SysFile;

/**
 * 文件请求处理
 *
 * @author ruoyi
 */
@RestController
public class SysFileController
{
    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<SysFile> upload(MultipartFile file)
    {
        try
        {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        }
        catch (Exception e)
        {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 文件上传请求
     */
    @PostMapping("delete")
    public R<Boolean> delete(String url)
    {
        try
        {
            Boolean result = sysFileService.deleteFile(url);
            return R.ok(result);
        }
        catch (Exception e)
        {
            log.error("文件删除失败", e);
            return R.fail(e.getMessage());
        }
    }
}