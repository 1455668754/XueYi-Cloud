package com.xueyi.system.dict.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.xueyi.common.core.constant.basic.BaseConstants;
import com.xueyi.common.core.constant.basic.CacheConstants;
import com.xueyi.common.redis.service.RedisService;
import com.xueyi.common.web.entity.service.impl.SubBaseServiceImpl;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.api.dict.domain.dto.SysDictTypeDto;
import com.xueyi.system.dict.manager.SysDictTypeManager;
import com.xueyi.system.dict.mapper.SysDictDataMapper;
import com.xueyi.system.dict.mapper.SysDictTypeMapper;
import com.xueyi.system.dict.service.ISysDictDataService;
import com.xueyi.system.dict.service.ISysDictTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 字典类型管理 业务层处理
 *
 * @author xueyi
 */
@Service
public class SysDictTypeServiceImpl extends SubBaseServiceImpl<SysDictTypeDto, SysDictTypeManager, SysDictTypeMapper, SysDictDataDto, ISysDictDataService, SysDictDataMapper> implements ISysDictTypeService {

    @Autowired
    private RedisService redisService;

    /**
     * 项目启动时，初始化字典到缓存
     */
    @PostConstruct
    public void init() {
        loadingDictCache();
    }

    /**
     * 新增数据对象
     *
     * @param dictType 数据对象
     * @return 结果
     */
    @Override
    @DSTransactional
    public int insert(SysDictTypeDto dictType) {
        int row = super.insert(dictType);
        if (row > 0)
            redisService.setCacheMapValue(CacheConstants.SYS_DICT_KEY, dictType.getCode(), dictType.getSubList());
        return row;
    }

    /**
     * 根据Id删除参数对象
     *
     * @param id Id
     * @return 结果
     */
    @Override
    public int deleteById(Serializable id) {
        SysDictTypeDto dict = baseManager.selectById(id);
        deleteDictCache(dict.getCode());
        return baseManager.deleteById(id);
    }

    /**
     * 根据Id集合删除参数对象
     *
     * @param idList Id集合
     * @return 结果
     */
    @Override
    public int deleteByIds(Collection<? extends Serializable> idList) {
        List<SysDictTypeDto> dictList = baseManager.selectListByIds(idList);
        dictList.forEach(item -> deleteDictCache(item.getCode()));
        return baseManager.deleteByIds(idList);
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<SysDictTypeDto> dictTypeList = baseManager.selectListExtra(null);
        Map<String, List<SysDictDataDto>> dataMap = new HashMap<>();
        dictTypeList.forEach(item -> dataMap.put(item.getCode(), item.getSubList()));
        redisService.setCacheMap(CacheConstants.SYS_DICT_KEY, dataMap);
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        redisService.deleteObject(CacheConstants.SYS_DICT_KEY);
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 校验字典编码是否唯一
     *
     * @param Id       字典类型Id
     * @param dictCode 字典类型编码
     * @return 结果 | true/false 唯一/不唯一
     */
    @Override
    public boolean checkDictCodeUnique(Long Id, String dictCode) {
        return ObjectUtil.isNotNull(baseManager.checkDictCodeUnique(ObjectUtil.isNull(Id) ? BaseConstants.NONE_ID : Id, dictCode));
    }

    /**
     * 根据编码删除字典缓存
     */
    private void deleteDictCache(String code) {
        redisService.deleteCacheMapHKey(CacheConstants.SYS_DICT_KEY, code);
    }

    /**
     * 设置子数据的外键值
     */
    @Override
    protected void setForeignKey(Collection<SysDictDataDto> dictDataList, SysDictDataDto dictData, SysDictTypeDto dictType, Serializable key) {
        String code = ObjectUtil.isNotNull(dictType) ? dictType.getCode() : (String) key;
        if (ObjectUtil.isNotNull(dictData))
            dictData.setCode(code);
        else
            dictDataList.forEach(sub -> sub.setCode(code));
    }
}