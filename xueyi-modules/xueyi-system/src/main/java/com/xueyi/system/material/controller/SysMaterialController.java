package com.xueyi.system.material.controller;

import com.xueyi.common.core.domain.R;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.controller.BaseController;
import com.xueyi.common.core.web.domain.AjaxResult;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.PreAuthorize;
import com.xueyi.system.api.RemoteFileService;
import com.xueyi.system.api.material.SysFile;
import com.xueyi.system.api.material.SysMaterial;
import com.xueyi.system.api.material.SysMaterialFolder;
import com.xueyi.system.material.service.ISysMaterialFolderService;
import com.xueyi.system.material.service.ISysMaterialService;
import javafx.scene.paint.Material;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 素材信息 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/material")
public class SysMaterialController extends BaseController {

    @Autowired
    private RemoteFileService remoteFileService;

    @Autowired
    private ISysMaterialService materialService;

    @Autowired
    private ISysMaterialFolderService materialFolderService;

    /**
     * 查询素材信息列表
     */
//    @PreAuthorize(hasPermi = "system:material:query")
    @GetMapping("/list")
    public AjaxResult list(SysMaterialFolder folder) {
        //1.判断是否有folderId传进来,如无则代表前端传值错误,直接做空返回
        if (folder.getFolderId() != null) {
            SysMaterialFolder materialFolder;
            List<SysMaterial> materialList;
            List<SysMaterialFolder> children;
            //2.当有名称搜索时，采用名称模糊查询
            if (folder.getFolderName() != null && !folder.getFolderName().equals("")) {
                materialFolder = new SysMaterialFolder();
                materialFolder.setFolderName("默认文件夹");

                SysMaterial materialListByNick = new SysMaterial();
                materialListByNick.setMaterialNick(folder.getFolderName());
                materialList = materialService.selectMaterialList(materialListByNick);

                SysMaterialFolder folderListByNick = new SysMaterialFolder();
                folderListByNick.setFolderName(folder.getFolderName());
                children = materialFolderService.selectMaterialFolderList(folderListByNick);
            } else {
                //3.当名称为null/""，且文件夹Id，进行首页查询
                if (folder.getFolderId().equals(0L)) {
                    materialFolder = new SysMaterialFolder();
                    materialFolder.setFolderName("默认文件夹");
                }
                //4.当名称为null/""，且文件夹Id不为0时，进行文件夹查询
                else {
                    materialFolder = materialFolderService.selectMaterialFolderById(folder.getFolderId());
                }
                materialList = materialService.selectMaterialListByFolderId(folder.getFolderId());
                children = materialFolderService.selectDirectChildrenMaterialFolderById(folder.getFolderId());
            }
            if (materialList.size() > 0) {
                List<Object> o = new ArrayList<>(materialList);
                materialFolder.setMaterialList(o);
            }
            if (children.size() > 0) {
                List<Object> c = new ArrayList<>(children);
                materialFolder.setChildren(c);
            }
            return AjaxResult.success(materialFolder);
        }
        return AjaxResult.success();
    }

    /**
     * 素材上传
     */
    @PreAuthorize(hasPermi = "system:material:add")
    @Log(title = "素材上传", businessType = BusinessType.UPDATE)
    @PostMapping("/upload")
    public AjaxResult material(@RequestParam(value = "folderId", required = false) Long
                                       folderId, @RequestParam("file") MultipartFile file) throws IOException {
        if (!file.isEmpty()) {
            //1.将 原图 传至文件服务模块进行保存处理
            R<SysFile> fileResult = remoteFileService.upload(file);
            if (StringUtils.isNull(fileResult) || StringUtils.isNull(fileResult.getData())) {
                return AjaxResult.error("文件服务异常，请稍后再试");
            }
            BigDecimal bigDecimal = new BigDecimal(file.getSize());
            BigDecimal MB = new BigDecimal("1000000");
            BigDecimal size = bigDecimal.divide(MB, 4, BigDecimal.ROUND_UP);
            SysMaterial material = new SysMaterial();
            material.setFolderId(folderId);
            material.setMaterialOriginalName(fileResult.getData().getName());
            material.setMaterialOriginalUrl(fileResult.getData().getUrl());
            material.setMaterialSize(size);
            if (!Objects.equals(file.getContentType(), "image/gif")) {
                //2.当文件格式不为gif时，创建压缩图片副本 | scale图片分辨率参数 1 = 原分辨率100% | outputQuality 图片质量参数 0.2 = 原大小20%
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                Thumbnails.of(file.getInputStream())
                        .scale(1)
                        .outputQuality(0.1)
                        .toOutputStream(outputStream);
                byte[] bytes = outputStream.toByteArray();
                InputStream inputStream = new ByteArrayInputStream(bytes);
                MultipartFile compressFile = new MockMultipartFile(Objects.requireNonNull(file.getOriginalFilename()), file.getOriginalFilename(), file.getContentType(), inputStream);
                //3.将 压缩图副本 传至文件服务模块进行保存处理
                R<SysFile> compressFileResult = remoteFileService.upload(compressFile);
                if (StringUtils.isNull(compressFileResult) || StringUtils.isNull(compressFileResult.getData())) {
                    return AjaxResult.error("文件服务异常，请稍后再试");
                }
                //初始状态默认nick=name
                material.setMaterialNick(compressFileResult.getData().getName());
                material.setMaterialName(compressFileResult.getData().getName());
                material.setMaterialUrl(compressFileResult.getData().getUrl());
            } else {
                //初始状态默认nick=name
                material.setMaterialNick(fileResult.getData().getName());
                material.setMaterialName(fileResult.getData().getName());
                material.setMaterialUrl(fileResult.getData().getUrl());
            }
            materialService.insertMaterial(material);
        }
        return AjaxResult.error("上传图片异常，请稍后再试");
    }

    /**
     * 修改素材信息
     */
    @PreAuthorize(hasPermi = "system:material:edit")
    @Log(title = "素材信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysMaterial material) {
        return toAjax(materialService.updateMaterial(material));
    }

    /**
     * 删除素材信息
     */
    @PreAuthorize(hasPermi = "system:material:remove")
    @Log(title = "素材信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{materialId}")
    public AjaxResult remove(@PathVariable Long materialId) {
        //删除素材文件
        SysMaterial material = materialService.selectMaterialById(materialId);
        remoteFileService.delete(material.getMaterialUrl());
        remoteFileService.delete(material.getMaterialOriginalUrl());
        return toAjax(materialService.deleteMaterialById(materialId));
    }
}
