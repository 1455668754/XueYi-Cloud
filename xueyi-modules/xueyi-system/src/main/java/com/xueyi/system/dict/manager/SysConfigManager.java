package com.xueyi.system.dict.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.dict.mapper.SysConfigMapper;
import org.springframework.stereotype.Component;

/**
 * 参数配置管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysConfigManager extends BaseManager<SysConfigDto, SysConfigMapper> {

    /**
     * 根据参数编码查询参数对象
     *
     * @param code 参数编码
     * @return 参数对象
     */
    public SysConfigDto selectConfigByCode(String code) {
        return baseMapper.selectOne(
                Wrappers.<SysConfigDto>query().lambda()
                        .eq(SysConfigDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验参数编码是否唯一
     *
     * @param Id   参数Id
     * @param code 参数编码
     * @return 参数对象
     */
    public SysConfigDto checkConfigCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysConfigDto>query().lambda()
                        .ne(SysConfigDto::getId, Id)
                        .eq(SysConfigDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 校验是否为内置参数
     *
     * @param Id 参数Id
     * @return 参数对象
     */
    public SysConfigDto checkIsBuiltIn(Long Id) {
        return baseMapper.selectOne(
                Wrappers.<SysConfigDto>query().lambda()
                        .eq(SysConfigDto::getId, Id)
                        .eq(SysConfigDto::getType, BaseConstants.Whether.YES.getCode())
                        .last(SqlConstants.LIMIT_ONE));
    }

}
