package com.xueyi.common.web.entity.manager.handle;

import com.xueyi.common.core.web.entity.base.TreeEntity;
import com.xueyi.common.web.entity.manager.BaseManager;
import com.xueyi.common.web.entity.mapper.TreeMapper;

/**
 * 数据封装层 操作方法 树型通用数据处理
 *
 * @param <D>  Dto
 * @param <DM> DtoMapper
 * @author xueyi
 */
public class TreeHandleManager<D extends TreeEntity<D>, DM extends TreeMapper<D>> extends BaseManager<D, DM> {
}
