package com.xueyi.system.material.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;
import com.xueyi.system.material.manager.SysMaterialManager;
import com.xueyi.system.material.mapper.SysMaterialMapper;
import com.xueyi.system.material.service.ISysMaterialService;
import org.springframework.stereotype.Service;

/**
 * 素材管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysMaterialServiceImpl extends BaseServiceImpl<SysMaterialDto, SysMaterialManager, SysMaterialMapper> implements ISysMaterialService {
}
