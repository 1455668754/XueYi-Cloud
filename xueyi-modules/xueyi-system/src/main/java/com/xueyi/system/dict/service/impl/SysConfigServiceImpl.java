package com.xueyi.system.dict.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.entity.service.impl.BaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;
import com.xueyi.system.dict.manager.SysConfigManager;
import com.xueyi.system.dict.mapper.SysConfigMapper;
import com.xueyi.system.dict.service.ISysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.xueyi.common.core.constant.basic.TenantConstants.MASTER;

/**
 * 参数配置管理 服务层实现
 *
 * @author xueyi
 */
@Service
@DS(MASTER)
public class SysConfigServiceImpl extends BaseServiceImpl<SysConfigDto, SysConfigManager, SysConfigMapper> implements ISysConfigService {

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化参数到缓存
     */
    @PostConstruct
    public void init() {
        loadingConfigCache();
    }

    /**
     * 根据参数编码查询参数值
     *
     * @param configCode 参数编码
     * @return 参数值
     */
    @Override
    public String selectConfigByCode(String configCode) {
        SysConfigDto config = baseManager.selectConfigByCode(configCode);
        return ObjectUtil.isNotNull(config) ? config.getValue() : StrUtil.EMPTY;
    }

    /**
     * 新增参数配置
     *
     * @param config 参数对象
     * @return 结果
     */
    @Override
    public int insert(SysConfigDto config) {
        return refreshCache(baseManager.insert(config), config.getCode(), config.getValue());
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @Override
    public int update(SysConfigDto config) {
        return refreshCache(baseManager.update(config), config.getCode(), config.getValue());
    }

    /**
     * 根据Id删除参数对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        SysConfigDto config = baseManager.selectById(id);
        int row = baseManager.deleteById(id);
        if (row > 0)
            redisService.deleteCacheMapHKey(CacheConstants.SYS_CONFIG_KEY, config.getCode());
        return row;
    }

    /**
     * 根据Id集合删除参数对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        List<SysConfigDto> configList = baseManager.selectListByIds(idList);
        int rows = baseManager.deleteByIds(idList);
        if (rows > 0)
            configList.forEach(config -> redisService.deleteCacheMapHKey(CacheConstants.SYS_CONFIG_KEY, config.getCode()));
        return rows;
    }

    /**
     * 校验参数编码是否唯一
     *
     * @param Id         参数Id
     * @param configCode 参数编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkConfigCodeUnique(Long Id, String configCode) {
        return ObjectUtil.isNotNull(baseManager.checkConfigCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, configCode));
    }

    /**
     * 校验是否为内置参数
     *
     * @param Id 参数Id
     * @return 结果 | true/false 是/否
     */
    @Override
    public boolean checkIsBuiltIn(Long Id) {
        return ObjectUtil.isNotNull(baseManager.checkIsBuiltIn(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id));
    }

    /**
     * 加载参数缓存数据
     */
    @Override
    public void loadingConfigCache() {
        Map<String, String> dataMap = new HashMap<>();
        List<SysConfigDto> configList = baseManager.selectList(null);
        configList.forEach(config -> dataMap.put(config.getCode(), config.getValue()));
        redisService.setCacheMap(CacheConstants.SYS_CONFIG_KEY, dataMap);
    }

    /**
     * 清空参数缓存数据
     */
    @Override
    public void clearConfigCache() {
        redisService.deleteObject(CacheConstants.SYS_CONFIG_KEY);
    }

    /**
     * 重置参数缓存数据
     */
    @Override
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 新增/修改缓存
     *
     * @param rows  结果
     * @param code  参数编码
     * @param value 参数值
     * @return 结果
     */
    private int refreshCache(int rows, String code, String value) {
        if (rows > 0)
            redisService.setCacheMapValue(CacheConstants.SYS_CONFIG_KEY, code, value);
        return rows;
    }
}
