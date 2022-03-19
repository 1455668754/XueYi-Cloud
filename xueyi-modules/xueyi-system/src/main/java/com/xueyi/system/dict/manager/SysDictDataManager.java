package com.xueyi.system.dict.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 字典数据管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class SysDictDataManager extends BaseManager<SysDictDataDto, SysDictDataMapper> {

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    public List<SysDictDataDto> selectListByCode(String code) {
        return baseMapper.selectList(
                Wrappers.<SysDictDataDto>query().lambda()
                        .eq(SysDictDataDto::getCode, code));
    }
}
