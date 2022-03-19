package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;

import java.util.List;

/**
 * 字典数据管理 服务层
 *
 * @author xueyi
 */
public interface ISysDictDataService extends IBaseService<SysDictDataDto> {

    /**
     * 查询字典数据对象列表
     *
     * @param code 字典编码
     * @return 字典数据对象集合
     */
    List<SysDictDataDto> selectListByCode(String code);
}
