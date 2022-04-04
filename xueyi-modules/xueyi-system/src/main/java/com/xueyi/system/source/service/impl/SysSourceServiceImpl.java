package com.xueyi.system.source.service.impl;

import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.source.domain.Source;
import com.xueyi.system.source.manager.SysSourceManager;
import com.xueyi.system.source.mapper.SysSourceMapper;
import com.xueyi.system.source.service.ISysSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 策略组管理 服务层处理
 *
 * @author xueyi
 */
@Service
public class SysSourceServiceImpl extends BaseServiceImpl<Source, SysSourceManager, SysSourceMapper> implements ISysSourceService {

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化策略组到缓存
     */
    @PostConstruct
    public void init() {
        loadingSourceCache();
    }

    /**
     * 加载策略组缓存数据
     */
    @Override
    public void loadingSourceCache() {
        List<Source> sourceList = baseManager.selectSourceList();
        Map<String, Source> sourceMap = new HashMap<>();
        for (Source source : sourceList)
            sourceMap.put(source.getId().toString(), source);
        setSourceMapCache(sourceMap);
    }

    /**
     * 清空策略组缓存数据
     */
    @Override
    public void clearSourceCache() {
        deleteAllSourceCache();
    }

    /**
     * 重置策略组缓存数据
     */
    @Override
    public void resetSourceCache() {
        clearSourceCache();
        loadingSourceCache();
    }

    /**
     * 根据Id更新策略组缓存数据
     *
     * @param id 源策略Id
     */
    @Override
    public void refreshSourceCache(Long id) {
        Source source = baseManager.selectById(id);
        setSourceCache(source.getId(), source);
    }

    /**
     * 源策略存储
     *
     * @param sourceMap 源策略Map
     */
    private void setSourceMapCache(Map<String, Source> sourceMap) {
        redisService.setCacheMap(CacheConstants.DATA_SOURCE_KEY, sourceMap);
    }

    /**
     * 更新源策略缓存
     *
     * @param id     源策略Id
     * @param source 源策略
     */
    private void setSourceCache(Serializable id, Source source) {
        redisService.setCacheMapValue(CacheConstants.DATA_SOURCE_KEY, id.toString(), source);
    }

    /**
     * 删除全部源策略缓存
     */
    private void deleteAllSourceCache() {
        redisService.deleteObject(CacheConstants.DATA_SOURCE_KEY);
    }
}
