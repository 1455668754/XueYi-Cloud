package com.xueyi.system.material.controller;

import com.xueyi.common.web.entity.controller.BaseController;
import com.xueyi.system.api.file.domain.dto.SysMaterialDto;
import com.xueyi.system.material.service.ISysMaterialService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 素材管理 业务处理
 *
 * @author xueyi
 */
@RestController
@RequestMapping("/material")
public class SysMaterialController extends BaseController<SysMaterialDto, ISysMaterialService> {

    /** 定义节点名称 */
    protected String getNodeName() {
        return "素材";
    }
}
