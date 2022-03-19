package com.xueyi.tenant.source.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.basic.DictConstants;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.tenant.api.source.domain.dto.TeSourceDto;
import com.xueyi.tenant.source.manager.TeSourceManager;
import com.xueyi.tenant.source.mapper.TeSourceMapper;
import com.xueyi.tenant.source.service.ITeSourceService;
import org.springframework.stereotype.Service;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 数据源管理 服务层处理
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class TeSourceServiceImpl extends BaseServiceImpl<TeSourceDto, TeSourceManager, TeSourceMapper> implements ITeSourceService {

    /**
     * 校验数据源是否为默认数据源
     *
     * @param id 数据源id
     * @return 结果 | true/false 是/不是
     */
    @Override
    public boolean checkIsDefault(Long id) {
        TeSourceDto source = baseManager.selectById(id);
        return ObjectUtil.isNotNull(source) && StrUtil.equals(source.getIsDefault(), DictConstants.DicYesNo.YES.getCode());
    }
}