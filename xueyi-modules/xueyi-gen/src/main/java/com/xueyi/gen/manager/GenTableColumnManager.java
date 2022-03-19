package com.xueyi.gen.manager;

import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.mapper.GenTableColumnMapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 业务字段管理 数据封装层
 *
 * @author xueyi
 */
@Component
public class GenTableColumnManager extends BaseManager<GenTableColumnDto, GenTableColumnMapper> {

    /**
     * 根据表名称查询数据库表列信息
     *
     * @param tableName 表名称
     * @return 数据库表列信息
     */
    public List<GenTableColumnDto> selectDbTableColumnsByName(String tableName){
        return baseMapper.selectDbTableColumnsByName(tableName);
    }

}
