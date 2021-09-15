package com.xueyi.system.source.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.TenantConstants;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.redis.utils.DataSourceUtils;
import com.xueyi.system.api.domain.source.Source;
import com.xueyi.system.source.mapper.DataSourceMapper;
import com.xueyi.system.source.service.IDataSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 数据源信息 业务层处理
 *
 * @author xueyi
 */
@Service
@DS("#main")
public class DataSourceServiceImpl implements IDataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    /**
     * 项目启动时，初始化数据源策略组到缓存
     */
    @PostConstruct
    public void init() {
        loadingSourceCache();
    }

    /**
     * 加载数据源策略组缓存数据
     */
    @Override
    public void loadingSourceCache() {
        List<Source> SourceList = dataSourceMapper.mainSelectDataSourceCacheList();
        for (Source source : SourceList) {
            for(Source value: source.getValues()){
                if(StringUtils.equals(TenantConstants.IS_MAIN_TRUE,value.getIsMain())){
                    source.setMaster(value.getMaster());
                    source.setSlave(value.getSlave());
                }
            }
            DataSourceUtils.refreshCache(source.getStrategyId(),source);
        }
    }

    /**
     * 根据策略Id刷新策略缓存 cache
     */
    @Override
    public void mainRefreshSource(Long strategyId){
        Source source = dataSourceMapper.mainSelectDataSourceCacheByStrategyId(strategyId);
        for(Source value: source.getValues()){
            if(StringUtils.equals(TenantConstants.IS_MAIN_TRUE,value.getIsMain())){
                source.setMaster(value.getMaster());
                source.setSlave(value.getSlave());
            }
        }
        DataSourceUtils.refreshCache(source.getStrategyId(),source);
    }
}
