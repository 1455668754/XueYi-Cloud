package com.xueyi.common.web.entity.controller;

import com.github.pagehelper.PageInfo;
import com.xueyi.common.core.config.DemoProperties;
import com.xueyi.common.core.exception.DemoModeException;
import com.xueyi.common.core.utils.DateUtils;
import com.xueyi.common.core.utils.PageUtils;
import com.xueyi.common.core.utils.ServletUtils;
import com.xueyi.common.core.utils.StringUtils;
import com.xueyi.common.core.web.page.TableDataInfo;
import com.xueyi.common.core.web.result.AjaxResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * web层 通用数据处理
 *
 * @author xueyi
 */
public class BasisController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /* ---演示模式控制 生产环境可移除 且可同步移除DemoProperties文件 start--- */
    @Autowired
    private DemoProperties demoProperties;

    @ModelAttribute
    public void init(HttpServletRequest httpServletRequest, HttpServletResponse response) throws IOException {
        if (!demoProperties.isEnabled()) {
            return;
        }
        String url = Objects.requireNonNull(ServletUtils.getRequest()).getRequestURI();
        // 需要放开的url
        if (StringUtils.isNotEmpty(url) && (url.contains("/demo") || url.contains("/tool/gen") || url.contains("/operateLog") || url.contains("/loginLog")))
            return;
        // 增删改 请求
        if ("DELETE".equals(httpServletRequest.getMethod()) || "POST".equals(httpServletRequest.getMethod())
                || "PUT".equals(httpServletRequest.getMethod())) {
            throw new DemoModeException();
        }
    }
    /* ---演示模式控制 生产环境可移除 end--- */

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage() {
        PageUtils.startPage();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    protected AjaxResult getDataTable(List<?> list) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setItems(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        return AjaxResult.success(rspData);
    }

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows) {
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result) {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success() {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error() {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message) {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message) {
        return AjaxResult.error(message);
    }
}
