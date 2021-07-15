package com.xueyi.system.material.service;

import com.xueyi.system.api.domain.material.SysMaterialFolder;

import java.util.List;

/**
 * 素材分类Service接口
 *
 * @author xueyi
 */
public interface ISysMaterialFolderService {

    /**
     * 查询所有素材分类集合
     *
     * @return 素材分类集合
     */
    public List<SysMaterialFolder> selectList();

    /**
     * 查询素材分类列表
     *
     * @param materialFolder 素材分类
     * @return 素材分类集合
     */
    public List<SysMaterialFolder> selectMaterialFolderList(SysMaterialFolder materialFolder);

    /**
     * 查询素材分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类
     */
    public SysMaterialFolder selectMaterialFolderById(Long folderId);

    /**
     * 根据祖级列表查询所有父级分类
     *
     * @param ancestors 祖级列表
     * @return 素材分类列表
     */
    public List<SysMaterialFolder> selectParentMaterialFolderByAncestors(String ancestors);

    /**
     * 根据Id查询直属子分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类列表
     */
    public List<SysMaterialFolder> selectDirectChildrenMaterialFolderById(Long folderId);

    /**
     * 根据Id查询所有子分类
     *
     * @param folderId 素材分类Id
     * @return 素材分类列表
     */
    public List<SysMaterialFolder> selectChildrenMaterialFolderById(Long folderId);

    /**
     * 新增素材分类
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    public int insertMaterialFolder(SysMaterialFolder materialFolder);

    /**
     * 修改素材分类
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    public int updateMaterialFolder(SysMaterialFolder materialFolder);

    /**
     * 删除素材分类信息
     *
     * @param folderId 素材分类Id
     * @return 结果
     */
    public int deleteMaterialFolderById(Long folderId);

    /**
     * 批量删除素材分类信息
     *
     * @param folderIds 素材分类Id集合
     * @return 结果
     */
    public int deleteMaterialFolderByIds(Long[] folderIds);
}
