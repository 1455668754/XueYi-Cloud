package com.xueyi.file.service;

import com.xueyi.file.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 本地文件存储
 *
 * @author xueyi
 */
@Primary
@Service
public class LocalSysFileServiceImpl implements ISysFileService {

    /** 资源映射路径 前缀 */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /** 域名或本机访问地址 */
    @Value("${file.domain}")
    public String domain;

    /** 上传文件存储在本地的根路径 */
    @Value("${file.path}")
    private String localFilePath;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        String name = FileUploadUtils.upload(localFilePath, file);
        return domain + localFilePrefix + name;
    }

    /**
     * 文件删除接口
     *
     * @param url 文件地址
     * @return 结果
     */
    public Boolean deleteFile(String url) throws Exception {
        String localPath = url.replace(domain + localFilePrefix, localFilePath);
        File file = new File(localPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            return true;
        }
        return false;
    }
}
