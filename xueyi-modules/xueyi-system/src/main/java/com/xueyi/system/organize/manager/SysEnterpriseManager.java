package com.xueyi.system.organize.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.organize.domain.dto.SysEnterpriseDto;
import com.xueyi.system.organize.mapper.SysEnterpriseMapper;
import org.springframework.stereotype.Component;

/**
 * 企业管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysEnterpriseManager extends BaseManager<SysEnterpriseDto, SysEnterpriseMapper> {

    /**
     * 根据名称查询状态正常企业对象
     *
     * @param name 名称
     * @return 企业对象
     */
    public SysEnterpriseDto selectByName(String name){
        return baseMapper.selectOne(
                Wrappers.<SysEnterpriseDto>query().lambda()
                        .eq(SysEnterpriseDto::getName, name)
                        .eq(SysEnterpriseDto::getStatus, BaseConstants.Status.NORMAL.getCode()));
    }
}
