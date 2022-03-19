package com.xueyi.system.organize.service;

import com.xueyi.common.web.entity.service.ISubTreeService;
import com.xueyi.system.api.organize.domain.dto.SysDeptDto;
import com.xueyi.system.api.organize.domain.dto.SysPostDto;

/**
 * 部门管理 服务层
 *
 * @author xueyi
 */
public interface ISysDeptService extends ISubTreeService<SysDeptDto, SysPostDto> {

    /**
     * 新增部门 | 内部调用
     *
     * @param dept 部门对象
     * @return 结果
     */
    int addInner(SysDeptDto dept);

    /**
     * 校验部门编码是否唯一
     *
     * @param Id   部门Id
     * @param code 部门编码
     * @return 结果 | true/false 唯一/不唯一
     */
    boolean checkDeptCodeUnique(Long Id, String code);
}
