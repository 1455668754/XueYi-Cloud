package com.xueyi.system.dict.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.xueyi.common.core.web.result.AjaxResult;
import com.xueyi.common.core.web.validate.V_A;
import com.xueyi.common.core.web.validate.V_E;
import com.xueyi.common.log.annotation.Log;
import com.xueyi.common.log.enums.BusinessType;
import com.xueyi.common.security.annotation.RequiresPermissions;
import com.xueyi.common.security.auth.Auth;
import com.xueyi.common.security.utils.DictUtils;
import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.dict.domain.dto.SysDictDataDto;
import com.xueyi.system.dict.service.ISysDictDataService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 字典数据管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/dict/data")
public class SysDictDataController extends BaseController<SysDictDataDto, ISysDictDataService> {

    /** 定义节点名称 */
    @Override
    protected String getNodeName() {
        return "字典数据";
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/type/{code}")
    public AjaxResult listByCode(@PathVariable String code) {
        List<SysDictDataDto> data = baseService.selectListByCode(code);
        return AjaxResult.success(ObjectUtil.isNotNull(data) ? data : new ArrayList<SysDictDataDto>());
    }

    /**
     * 根据字典类型查询字典数据信息
     */
    @GetMapping(value = "/types/{codeList}")
    public AjaxResult listByCodeList(@PathVariable List<String> codeList) {
        if (CollUtil.isEmpty(codeList))
            AjaxResult.error("请传入编码后再查询字典");
        HashMap<String, List<SysDictDataDto>> map = new HashMap<>();
        for (String code : codeList)
            map.put(code, DictUtils.getDictCache(code));
        return AjaxResult.success(map);
    }

    /**
     * 查询字典数据列表
     */
    @Override
    @GetMapping("/list")
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    public AjaxResult list(SysDictDataDto dictData) {
        return super.list(dictData);
    }

    /**
     * 查询字典数据详细
     */
    @Override
    @GetMapping(value = "/{id}")
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    public AjaxResult getInfoExtra(@PathVariable Serializable id) {
        return super.getInfoExtra(id);
    }

    /**
     * 字典数据导出
     */
    @Override
    @PostMapping("/export")
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    @Log(title = "字典数据管理", businessType = BusinessType.EXPORT)
    public void export(HttpServletResponse response, SysDictDataDto dictData) {
        super.export(response, dictData);
    }

    /**
     * 字典数据新增
     */
    @Override
    @PostMapping
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    @Log(title = "字典数据管理", businessType = BusinessType.INSERT)
    public AjaxResult add(@Validated({V_A.class}) @RequestBody SysDictDataDto dictData) {
        return super.add(dictData);
    }

    /**
     * 字典数据修改
     */
    @Override
    @PutMapping
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    @Log(title = "字典数据管理", businessType = BusinessType.UPDATE)
    public AjaxResult edit(@Validated({V_E.class}) @RequestBody SysDictDataDto dictData) {
        return super.edit(dictData);
    }

    /**
     * 字典数据修改状态
     */
    @Override
    @PutMapping("/status")
    @RequiresPermissions({Auth.SYS_DICT_DICT})
    @Log(title = "字典数据管理", businessType = BusinessType.UPDATE_STATUS)
    public AjaxResult editStatus(@RequestBody SysDictDataDto dictData) {
        return super.editStatus(dictData);
    }

    /**
     * 字典数据批量删除
     */
    @Override
    @DeleteMapping("/batch/{idList}")
    @RequiresPermissions(Auth.SYS_DICT_DICT)
    @Log(title = "字典数据管理", businessType = BusinessType.DELETE)
    public AjaxResult batchRemove(@PathVariable List<Long> idList) {
        return super.batchRemove(idList);
    }
}
