package com.xueyi.common.web.method;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.xueyi.common.web.enums.SqlMethod;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 批量插入 方法体
 *
 * @author xueyi
 */
public class InsertBatchMethod extends AbstractMethod {

    public InsertBatchMethod() {
        super(SqlMethod.INSERT_BATCH.getMethod());
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        KeyGenerator keyGenerator = NoKeyGenerator.INSTANCE;
        SqlMethod sqlMethod = SqlMethod.INSERT_BATCH;
        String keyProperty = null;
        String keyColumn = null;
        if (StringUtils.isNotBlank(tableInfo.getKeyProperty())) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                keyGenerator = Jdbc3KeyGenerator.INSTANCE;
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else if (null != tableInfo.getKeySequence()) {
                keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, this.builderAssistant);
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            }
        }
        final String fieldSql = prepareFieldSql(tableInfo);
        final String valueSql = prepareValuesSql(tableInfo);
        final String sqlResult = String.format(sqlMethod.getSql(), tableInfo.getTableName(), fieldSql, valueSql);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, sqlMethod.getMethod(), sqlSource, keyGenerator, keyProperty, keyColumn);
    }

    /**
     * 构建注入字段
     *
     * @param tableInfo 表信息
     * @return 注入字段
     */
    private String prepareFieldSql(TableInfo tableInfo) {
        StringBuilder fieldSql = new StringBuilder();
        if (StrUtil.isNotEmpty(tableInfo.getKeyColumn()))
            fieldSql.append(tableInfo.getKeyColumn()).append(",");
        tableInfo.getFieldList().forEach(x -> {
            if (!ObjectUtil.equals(FieldStrategy.NEVER, x.getInsertStrategy())) {
                fieldSql.append(x.getColumn()).append(",");
            }
        });
        fieldSql.delete(fieldSql.length() - 1, fieldSql.length());
        fieldSql.insert(0, "(");
        fieldSql.append(")");
        return fieldSql.toString();
    }

    /**
     * 构建注入值
     *
     * @param tableInfo 表信息
     * @return 注入值
     */
    private String prepareValuesSql(TableInfo tableInfo) {
        final StringBuilder valueSql = new StringBuilder();
        valueSql.append("<foreach collection=\"collection\" item=\"item\" index=\"index\" open=\"(\" separator=\"),(\" close=\")\">");
        if (StrUtil.isNotEmpty(tableInfo.getKeyColumn()))
            valueSql.append("#{item.").append(tableInfo.getKeyProperty()).append("},");
        tableInfo.getFieldList().forEach(x -> {
            if (!ObjectUtil.equals(FieldStrategy.NEVER, x.getInsertStrategy()))
                valueSql.append("#{item.").append(x.getProperty()).append("},");
        });
        valueSql.delete(valueSql.length() - 1, valueSql.length());
        valueSql.append("</foreach>");
        return valueSql.toString();
    }
}