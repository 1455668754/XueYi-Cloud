package com.xueyi.system.material.service;

import com.xueyi.system.api.domain.material.SysMaterial;

import java.util.List;

/**
 * 素材 业务层
 *
 * @author xueyi
 */
public interface ISysMaterialService {

    /**
     * 查询所有素材信息集合
     *
     * @return 素材信息集合
     */
    public List<SysMaterial> selectList();

    /**
     * 查询素材信息列表
     *
     * @param material 素材信息
     * @return 素材分类集合
     */
    public List<SysMaterial> selectMaterialList(SysMaterial material);

    /**
     * 查询指定文件夹Id的素材信息列表
     *
     * @param folderId 素材分类Id
     * @return 素材信息
     */
    public List<SysMaterial> selectMaterialListByFolderId(Long folderId);

    /**
     * 查询素材信息
     *
     * @param materialId 素材信息Id
     * @return 素材信息
     */
    public SysMaterial selectMaterialById(Long materialId);

    /**
     * 新增保存素材信息
     *
     * @param material 素材信息
     * @return 结果
     */
    public int insertMaterial(SysMaterial material);

    /**
     * 修改素材信息
     *
     * @param material 素材信息
     * @return 结果
     */
    public int updateMaterial(SysMaterial material);

    /**
     * 删除素材信息
     *
     * @param materialId 素材信息Id
     * @return 结果
     */
    public int deleteMaterialById(Long materialId);

    /**
     * 批量删除素材信息
     *
     * @param materialIds 素材信息Id集合
     * @return 结果
     */
    public int deleteMaterialByIds(Long[] materialIds);

    /**
     * 根据folderId删除素材信息
     *
     * @param folderId 素材分类Id
     * @return 结果
     */
    public int deleteMaterialByFolderId(Long folderId);

    /**
     * 根据folderId集合批量删除素材信息
     *
     * @param folderIds 素材分类Id集合
     * @return 结果
     */
    public int deleteMaterialByFolderIds(Long[] folderIds);
}
