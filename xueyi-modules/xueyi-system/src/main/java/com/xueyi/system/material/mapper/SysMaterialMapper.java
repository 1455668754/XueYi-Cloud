package com.xueyi.system.material.mapper;

import com.xueyi.common.datascope.annotation.DataScope;
import com.xueyi.system.api.material.SysMaterial;
import com.xueyi.system.api.utilTool.SysSearch;

import java.util.List;

/**
 * 素材表|素材信息表 数据层
 *
 * @author xueyi
 */
public interface SysMaterialMapper {

    /**
     * 查询所有素材信息集合
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | null
     * @return 素材信息集合
     */
    @DataScope(enterpriseAlias = "m")
    public List<SysMaterial> selectList(SysSearch search);

    /**
     * 查询素材信息列表
     * 访问控制 m 租户查询
     *
     * @param material 素材信息
     * @return 素材信息集合
     */
    @DataScope(enterpriseAlias = "m")
    public List<SysMaterial> selectMaterialList(SysMaterial material);

    /**
     * 查询指定文件夹的素材信息列表
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 素材信息
     */
    @DataScope(enterpriseAlias = "m")
    public List<SysMaterial> selectMaterialListByFolderId(SysSearch search);

    /**
     * 查询素材信息
     * 访问控制 m 租户查询
     *
     * @param search 万用组件 | materialId 素材信息Id
     * @return 素材信息
     */
    @DataScope(enterpriseAlias = "m")
    public SysMaterial selectMaterialById(SysSearch search);

    /**
     * 新增素材信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param material 素材信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int insertMaterial(SysMaterial material);

    /**
     * 修改素材信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param material 素材信息
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int updateMaterial(SysMaterial material);

    /**
     * 删除素材信息信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | materialId 素材信息Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteMaterialById(SysSearch search);

    /**
     * 批量删除素材信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | materialIds 素材信息Id集合
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteMaterialByIds(SysSearch search);

    /**
     * 根据folderId删除素材信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | folderId 素材分类Id
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteMaterialByFolderId(SysSearch search);

    /**
     * 根据folderId集合批量删除素材信息
     * 访问控制 empty 租户更新（无前缀）
     *
     * @param search 万用组件 | folderIds 素材分类Id集合
     * @return 结果
     */
    @DataScope(updateEnterpriseAlias = "empty")
    public int deleteMaterialByFolderIds(SysSearch search);
}
