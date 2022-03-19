package com.xueyi.system.material.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.web.entity.service.impl.SubTreeServiceImpl;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;
import com.xueyi.system.api.file.domain.dto.SysMaterialFolderDto;
import com.xueyi.system.material.manager.SysMaterialFolderManager;
import com.xueyi.system.material.mapper.SysMaterialFolderMapper;
import com.xueyi.system.material.mapper.SysMaterialMapper;
import com.xueyi.system.material.service.ISysMaterialFolderService;
import com.xueyi.system.material.service.ISysMaterialService;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Collection;

import static com.xueyi.common.core.constant.basic.TenantConstants.ISOLATE;

/**
 * 素材分类管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(ISOLATE)
public class SysMaterialFolderServiceImpl extends SubTreeServiceImpl<SysMaterialFolderDto, SysMaterialFolderManager, SysMaterialFolderMapper, SysMaterialDto, ISysMaterialService, SysMaterialMapper> implements ISysMaterialFolderService {

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysMaterialDto> materialList, SysMaterialDto material, SysMaterialFolderDto folder, Serializable id) {
        Long folderId = ObjectUtil.isNotNull(folder) ? folder.getId() : (Long) id;
        if (ObjectUtil.isNotNull(material))
            material.setFolderId(folderId);
        else
            materialList.forEach(sub -> sub.setFolderId(folderId));
    }
}
