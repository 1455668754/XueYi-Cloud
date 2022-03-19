package com.xueyi.system.dict.manager;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.core.constant.basic.SqlConstants;
import com.xueyi.common.web.entity.manager.SubBaseManager;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import com.xueyi.system.dict.mapper.SysDictTypeMapper;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 字典类型管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysDictTypeManager extends SubBaseManager<SysDictTypeDto, SysDictTypeMapper, SysDictDataDto, SysDictDataMapper> {

    /**
     * 校验字典编码是否唯一
     *
     * @param Id   字典类型Id
     * @param code 字典类型编码
     * @return 字典类型对象
     */
    public SysDictTypeDto checkDictCodeUnique(Long Id, String code) {
        return baseMapper.selectOne(
                Wrappers.<SysDictTypeDto>query().lambda()
                        .ne(SysDictTypeDto::getId, Id)
                        .eq(SysDictTypeDto::getCode, code)
                        .last(SqlConstants.LIMIT_ONE));
    }

    /**
     * 设置主子表中子表外键值
     */
    @Override
    protected void setForeignKey(LambdaQueryWrapper<SysDictDataDto> queryWrapper, LambdaUpdateWrapper<SysDictDataDto> updateWrapper, SysDictTypeDto dictType, Serializable code) {
        Serializable Code = ObjectUtil.isNotNull(dictType) ? dictType.getCode() : code;
        if (ObjectUtil.isNotNull(queryWrapper))
            queryWrapper.eq(SysDictDataDto::getCode, Code);
        else
            updateWrapper.eq(SysDictDataDto::getCode, Code);
    }
}
