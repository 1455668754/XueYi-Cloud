package com.xueyi.gen.service.impl;

import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.gen.domain.dto.GenTableColumnDto;
import com.xueyi.gen.manager.GenTableColumnManager;
import com.xueyi.gen.mapper.GenTableColumnMapper;
import com.xueyi.gen.service.IGenTableColumnService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 业务字段管理 服务层实现
 *
 * @author xueyi
 */
@Service
public class GenTableColumnServiceImpl extends BaseServiceImpl<GenTableColumnDto, GenTableColumnManager, GenTableColumnMapper> implements IGenTableColumnService {

	/**
	 * 根据表名称查询数据库表列信息
	 *
	 * @param tableName 表名称
	 * @return 数据库表列信息
	 */
	@Override
	public List<GenTableColumnDto> selectDbTableColumnsByName(String tableName){
		return baseManager.selectDbTableColumnsByName(tableName);
	}
}