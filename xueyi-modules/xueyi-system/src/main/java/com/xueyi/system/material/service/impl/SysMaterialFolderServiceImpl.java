package com.xueyi.system.material.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.system.api.domain.material.SysMaterialFolder;
import com.xueyi.system.api.utilTool.SysSearch;
import com.xueyi.system.material.mapper.SysMaterialFolderMapper;
import com.xueyi.system.material.service.ISysMaterialFolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xueyi.common.core.constant.TenantConstants.ISOLATE;

/**
 * 素材文件夹信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysMaterialFolderServiceImpl implements ISysMaterialFolderService {

    @Autowired
    private SysMaterialFolderMapper materialFolderMapper;

    /**
     * 查询所有素材分类集合
     *
     * @return 素材分类集合
     */
    @Override
    public List<SysMaterialFolder> selectList() {
        SysSearch search = new SysSearch();
        return materialFolderMapper.selectList(search);
    }

    /**
     * 查询素材分类列表
     *
     * @param materialFolder 素材分类
     * @return 素材分类
     */
    @Override
    public List<SysMaterialFolder> selectMaterialFolderList(SysMaterialFolder materialFolder) {
        return materialFolderMapper.selectMaterialFolderList(materialFolder);
    }

    /**
     * 查询素材分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类
     */
    @Override
    public SysMaterialFolder selectMaterialFolderById(Long folderId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("folderId", folderId);
        return materialFolderMapper.selectMaterialFolderById(search);
    }

    /**
     * 根据祖级列表查询所有父级分类
     *
     * @param ancestors 祖级列表
     * @return 素材分类列表
     */
    @Override
    public List<SysMaterialFolder> selectParentMaterialFolderByAncestors(String ancestors) {
        SysSearch search = new SysSearch();
        search.getSearch().put("ancestors", ancestors);
        return materialFolderMapper.selectParentMaterialFolderByAncestors(search);
    }

    /**
     * 根据Id查询直属子分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类列表
     */
    @Override
    public List<SysMaterialFolder> selectDirectChildrenMaterialFolderById(Long folderId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("folderId", folderId);
        return materialFolderMapper.selectDirectChildrenMaterialFolderById(search);
    }

    /**
     * 根据Id查询所有子分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类列表
     */
    @Override
    public List<SysMaterialFolder> selectChildrenMaterialFolderById(Long folderId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("folderId", folderId);
        return materialFolderMapper.selectChildrenMaterialFolderById(search);
    }

    /**
     * 新增素材分类
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    @Override
    public int insertMaterialFolder(SysMaterialFolder materialFolder) {
        return materialFolderMapper.insertMaterialFolder(materialFolder);
    }

    /**
     * 修改素材分类
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    @Override
    public int updateMaterialFolder(SysMaterialFolder materialFolder) {
        return materialFolderMapper.updateMaterialFolder(materialFolder);
    }

    /**
     * 删除素材分类信息
     *
     * @param folderId 素材分类Id
     * @return 结果
     */
    @Override
    public int deleteMaterialFolderById(Long folderId) {
        SysSearch search = new SysSearch();
        search.getSearch().put("folderId", folderId);
        return materialFolderMapper.deleteMaterialFolderById(search);
    }

    /**
     * 批量删除素材分类信息
     *
     * @param folderIds 素材分类Id集合
     * @return 结果
     */
    @Override
    public int deleteMaterialFolderByIds(Long[] folderIds) {
        SysSearch search = new SysSearch();
        search.getSearch().put("folderIds", folderIds);
        return materialFolderMapper.deleteMaterialFolderByIds(search);
    }
}
