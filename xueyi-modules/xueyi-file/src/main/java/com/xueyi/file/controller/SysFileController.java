package com.xueyi.file.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.file.FileUtils;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.file.api.domain.SysFile;
import com.xueyi.file.service.ISysFileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件请求处理
 *
 * @author xueyi
 */
@RestController
public class SysFileController {

    private static final Logger log = LoggerFactory.getLogger(SysFileController.class);

    @Autowired
    private ISysFileService sysFileService;

    /**
     * 文件上传 | 内部调用
     */
    @PostMapping("/inner/upload")
    public R<SysFile> uploadInner(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = sysFileService.uploadFile(file);
            SysFile sysFile = new SysFile();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setUrl(url);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 删除文件 | 内部调用
     */
    @DeleteMapping(value = "/inner/delete/{url}")
    public R<Boolean> deleteInner(@PathVariable String url) {
        try {
            Boolean result = sysFileService.deleteFile(url);
            return R.ok(result);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return R.fail(e.getMessage());
        }
    }


    /**
     * 文件上传请求
     */
    @PostMapping("/upload")
    public AjaxResult upload(MultipartFile file) {
        R<SysFile> R = uploadInner(file);
        return R.isOk()
                ? AjaxResult.success("上传成功！", R.getResult().getUrl())
                : AjaxResult.error("上传失败！");
    }
}