package com.xueyi.gen.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.gen.domain.dto.GenTableColumnDto;

import java.util.List;

/**
 * 业务字段管理 服务层
 *
 * @author xueyi
 */
public interface IGenTableColumnService extends IBaseService<GenTableColumnDto> {

    /**
     * 根据表名称查询数据库表列信息
     *
     * @param tableName 表名称
     * @return 数据库表列信息
     */
    List<GenTableColumnDto> selectDbTableColumnsByName(String tableName);
}