package com.xueyi.system.dict.service;

import com.xueyi.common.web.entity.service.IBaseService;
import com.xueyi.system.api.dict.domain.dto.SysConfigDto;

/**
 * 参数配置管理 服务层
 *
 * @author xueyi
 */
public interface ISysConfigService extends IBaseService<SysConfigDto> {

    /**
     * 根据参数编码查询参数值
     *
     * @param configCode 参数编码
     * @return 参数值
     */
    String selectConfigByCode(String configCode);

    /**
     * 校验参数编码是否唯一
     *
     * @param Id         参数Id
     * @param configCode 参数编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkConfigCodeUnique(Long Id, String configCode);

    /**
     * 校验是否为内置参数
     *
     * @param Id 参数Id
     * @return 结果 | true/false 是/否
     */
    boolean checkIsBuiltIn(Long Id);

    /**
     * 加载参数缓存数据
     */
    void loadingConfigCache();

    /**
     * 清空参数缓存数据
     */
    void clearConfigCache();

    /**
     * 重置参数缓存数据
     */
    void resetConfigCache();
}