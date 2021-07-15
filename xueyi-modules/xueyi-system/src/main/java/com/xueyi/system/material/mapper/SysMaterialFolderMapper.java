package com.xueyi.system.material.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.domain.material.SysMaterialFolder;
import com.xueyi.system.api.utilTool.SysSearch;

import java.util.List;

/**
 * 素材分类Mapper接口
 *
 * @author xueyi
 */
public interface SysMaterialFolderMapper {

    /**
     * 查询所有素材分类集合
     * 访问控制 f 租户查询
     *
     * @param search 万用组件 | null
     * @return 素材分类集合
     */
    @DataScope(eAlias = "f")
    public List<SysMaterialFolder> selectList(SysSearch search);

    /**
     * 查询素材分类列表
     * 访问控制 f 租户查询
     *
     * @param materialFolder 素材分类
     * @return 素材分类集合
     */
    @DataScope(eAlias = "f")
    public List<SysMaterialFolder> selectMaterialFolderList(SysMaterialFolder materialFolder);

    /**
     * 查询素材分类
     * 访问控制 f 租户查询
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 素材分类
     */
    @DataScope(eAlias = "f")
    public SysMaterialFolder selectMaterialFolderById(SysSearch search);

    /**
     * 根据祖级列表查询所有父级分类
     * 访问控制 f 租户查询
     *
     * @param search 万用组件 | ancestors 祖级列表
     * @return 素材分类列表
     */
    @DataScope(eAlias = "f")
    public List<SysMaterialFolder> selectParentMaterialFolderByAncestors(SysSearch search);

    /**
     * 根据Id查询直属子分类
     * 访问控制 f 租户查询
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 素材分类列表
     */
    @DataScope(eAlias = "f")
    public List<SysMaterialFolder> selectDirectChildrenMaterialFolderById(SysSearch search);

    /**
     * 根据Id查询所有子分类
     * 访问控制 f 租户查询
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 素材分类列表
     */
    @DataScope(eAlias = "f")
    public List<SysMaterialFolder> selectChildrenMaterialFolderById(SysSearch search);

    /**
     * 新增素材分类
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int insertMaterialFolder(SysMaterialFolder materialFolder);

    /**
     * 修改素材分类
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param materialFolder 素材分类
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int updateMaterialFolder(SysMaterialFolder materialFolder);

    /**
     * 根据Id删除素材分类
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteMaterialFolderById(SysSearch search);

    /**
     * 批量删除素材分类
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | folderIds 素材分类Id集合
     * @return 结果
     */
    @DataScope(ueAlias = "empty")
    public int deleteMaterialFolderByIds(SysSearch search);
}
