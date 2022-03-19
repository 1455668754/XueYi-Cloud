package com.xueyi.job.task;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;

/**
 * 定时任务调度测试
 *
 * @author xueyi
 */
@Component("ryTask")
public class RyTask {

    public void ryMultipleParams(Long enterpriseId, String isLessor, String sourceName, String s, Boolean b, Long l, Double d, Integer i) {
        System.out.println(StrUtil.format("执行多参方法：企业Id{}, 租户类型{}, 源{}, 字符串类型{}，布尔类型{}，长整型{}，浮点型{}，整形{}", enterpriseId, isLessor, sourceName, s, b, l, d, i));
    }

    public void ryParams(Long enterpriseId, String isLessor, String sourceName, String params) {
        System.out.println(StrUtil.format("执行有参方法：企业Id{}, 租户类型{}, 源{}, 参数{}", enterpriseId, isLessor, sourceName, params));
    }

    public void ryNoParams(Long enterpriseId, String isLessor, String sourceName) {
        System.out.println(StrUtil.format("执行无参方法, 企业Id{}, 租户类型{}, 源{}", enterpriseId, isLessor, sourceName));
    }
}
