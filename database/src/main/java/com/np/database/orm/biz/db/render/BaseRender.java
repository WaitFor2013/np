package com.np.database.orm.biz.db.render;

import com.np.database.exception.NpDbException;
import com.np.database.orm.biz.param.BizFullText;
import com.np.database.orm.biz.param.BizOrderBy;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.BizProperty;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.sql.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseRender {

    protected static final String BLACK_SPACE = " ";
    protected static final String AND = " and ";
    protected static final String OR = " or ";
    protected static final String VALUE_HOLDER_SPACE = " ? ";
    protected static final String SPLIT = ",";

    protected void fullText(boolean isNeedAnd,BizParam bizParam,StringBuilder whereCauseSql,List<ColumnMapping> queryParams,Map<String, Object> valueMap,Map<String, ColumnMapping> allParams){
        //full text supported base on like
        BizFullText fullTextProperty = bizParam.getFullTextProperty();
        if (null != fullTextProperty) {

            if (isNeedAnd) {
                whereCauseSql.append(AND);
            } else {
                whereCauseSql.append(BLACK_SPACE).append("WHERE").append(BLACK_SPACE);
            }

            whereCauseSql.append("(");
            String[] fulltextPs = fullTextProperty.getProperties();
            for (int i = 0; i < fulltextPs.length; i++) {

                ColumnMapping parameterMapping = allParams.get(fulltextPs[i]);
                if (null == parameterMapping) {
                    throw new NpDbException("parameter not set : " + fulltextPs[i]);
                }

                if (i != 0) {
                    whereCauseSql.append(OR);
                }
                whereCauseSql.append(appendPrefix(parameterMapping)).append(BLACK_SPACE).append("like").append(VALUE_HOLDER_SPACE);

                //param render
                //value append with %value%
                paramsRender(queryParams, valueMap, parameterMapping, new Object[]{interLikeValueRender(fullTextProperty.getValue())});

            }
            whereCauseSql.append(")");

        }
    }

    protected void orderSupport(boolean isCount,BizParam bizParam,Map<String, ColumnMapping> allParams,StringBuilder renderSql){
        if (!isCount && null != bizParam.getBizOrderByList()) {
            List<BizOrderBy> bizOrderByList = bizParam.getBizOrderByList();
            for (int j = 0; j < bizOrderByList.size(); j++) {
                BizOrderBy bizOrderBy = bizOrderByList.get(j);
                ColumnMapping parameterMapping = allParams.get(bizOrderBy.getColumn());
                if (null == parameterMapping) {
                    throw new NpDbException("order parameter not exit : " + bizOrderBy.getColumn());
                }
                if (j == 0) {
                    renderSql.append(" ORDER BY ").append(appendPrefix(parameterMapping)).append(BLACK_SPACE)
                            .append(bizOrderBy.getDirection().toString()).append(BLACK_SPACE);
                } else {
                    renderSql.append(" , ").append(appendPrefix(parameterMapping)).append(BLACK_SPACE)
                            .append(bizOrderBy.getDirection().toString()).append(BLACK_SPACE);
                }

            }
        }
    }

    protected void hasColumnsDo(StringBuilder whereCauseSql, BizProperty bizProperty,String operator,
                              Map<String, ColumnMapping> allParams
            , List<ColumnMapping> queryParams, Map<String, Object> valueMap) {
        for (int i = 0; i < bizProperty.getHasColumns().length; i++) {
            String hasColumn = bizProperty.getHasColumns()[i];
            ColumnMapping hasParameterMapping = allParams.get(hasColumn);
            if (null == hasParameterMapping) {
                throw new NpDbException("parameter not set : " + hasColumn);
            }
            if (i == 0) {
                whereCauseSql.append(" ( ");
            } else {
                whereCauseSql.append(OR);
            }

            whereCauseSql.append(appendPrefix(hasParameterMapping));

            if("between".equals(operator)){
                whereCauseSql.append(BLACK_SPACE).append(" between").append(VALUE_HOLDER_SPACE).append(AND).append(VALUE_HOLDER_SPACE);
            }else {
                whereCauseSql.append(BLACK_SPACE).append(operator).append(VALUE_HOLDER_SPACE);
            }
            paramsRender(queryParams, valueMap, hasParameterMapping, bizProperty.getValues());

        }
        whereCauseSql.append(" ) ");
    }

    //@Override
    public BoundSql renderUpdate(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param) {
        BizProperty[] properties = param.getProperties();
        List<BizProperty> whereProperties = validate(parameterMappings, properties, true);

        StringBuilder renderSql = new StringBuilder();
        List<ColumnMapping> queryParams = new ArrayList<>();
        Map<String, Object> valueMap = new HashMap<>();
        //update tableName set p=v where p=v
        renderSql.append("UPDATE ").append(tableName).append(BLACK_SPACE);
        renderSql.append("SET ");

        boolean needSplit = false;

        for (BizProperty property : properties) {
            if (!Boolean.TRUE.equals(property.getIsIndex())) {
                //set render
                if (needSplit) {
                    renderSql.append(SPLIT);
                }
                renderSql.append(property.getColumn()).append(" = ").append(VALUE_HOLDER_SPACE);
                needSplit = true;
                paramsRender(queryParams, valueMap, parameterMappings.get(property.getColumn()), property.getValues());
            }
        }

        renderUpdateOrDeleteWhereCause(renderSql, whereProperties, parameterMappings, queryParams, valueMap);

        return new BoundSql(renderSql.toString(), queryParams, valueMap);
    }

    protected void renderUpdateOrDeleteWhereCause(StringBuilder renderSql, List<BizProperty> whereProperties, Map<String, ColumnMapping> parameterMappings,
                                                List<ColumnMapping> queryParams, Map<String, Object> valueMap) {
        renderSql.append("WHERE ");
        //where render
        for (int i = 0; i < whereProperties.size(); i++) {
            BizProperty bizProperty = whereProperties.get(i);
            if (i != 0) {
                renderSql.append(AND);
            }
            renderSql.append(bizProperty.getColumn()).append(" = ").append(VALUE_HOLDER_SPACE);
            paramsRender(queryParams, valueMap, parameterMappings.get(bizProperty.getColumn()), bizProperty.getValues());
        }
    }

    //@Override
    public BoundSql renderDelete(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param) {
        BizProperty[] properties = param.getProperties();
        List<BizProperty> whereProperties = validate(parameterMappings, properties, true);

        StringBuilder renderSql = new StringBuilder();
        List<ColumnMapping> queryParams = new ArrayList<>();
        Map<String, Object> valueMap = new HashMap<>();
        //delete from tableName where p=v
        renderSql.append("DELETE FROM ").append(tableName).append(BLACK_SPACE);

        renderUpdateOrDeleteWhereCause(renderSql, whereProperties, parameterMappings, queryParams, valueMap);

        return new BoundSql(renderSql.toString(), queryParams, valueMap);
    }

    //@Override
    public BoundSql renderInsert(String tableName, Map<String, ColumnMapping> parameterMappings, BizParam param) {
        BizProperty[] properties = param.getProperties();
        validate(parameterMappings, properties, false);

        StringBuilder renderSql = new StringBuilder();
        List<ColumnMapping> queryParams = new ArrayList<>();
        Map<String, Object> valueMap = new HashMap<>();
        renderSql.append("INSERT INTO ").append(tableName).append(BLACK_SPACE);
        //insert into tableName(p,p) values (v,v)
        StringBuilder valuesBuilder = new StringBuilder(" values ");
        for (int i = 0; i < properties.length; i++) {
            BizProperty bizProperty = properties[i];
            //column render & values render
            if (i == 0) {
                renderSql.append(" ( ").append(bizProperty.getColumn());
                valuesBuilder.append("(").append(VALUE_HOLDER_SPACE);
            } else {
                renderSql.append(SPLIT).append(bizProperty.getColumn());
                valuesBuilder.append(SPLIT).append(VALUE_HOLDER_SPACE);
            }
            paramsRender(queryParams, valueMap, parameterMappings.get(bizProperty.getColumn()), bizProperty.getValues());
        }
        valuesBuilder.append(")");
        renderSql.append(")").append(valuesBuilder);

        return new BoundSql(renderSql.toString(), queryParams, valueMap);
    }

    protected List<BizProperty> validate(Map<String, ColumnMapping> parameterMappings, BizProperty[] properties, boolean whereCheck) {
        if (null == properties || properties.length == 0) {
            throw new NpDbException("BizProperty properties can't be null or empty");
        }

        List<BizProperty> whereProperties = new ArrayList<>();

        for (int i = 0; i < properties.length; i++) {
            BizProperty property = properties[i];
            if (null == property.getColumn() || null == property.getValues() || property.getValues().length != 1) {
                throw new NpDbException("BizProperty properties can't be null or empty,column values length must just be one");
            }
            if (!parameterMappings.containsKey(property.getColumn())) {
                throw new NpDbException("ParameterMappings don't contains column: " + property.getColumn());
            }
            if (Boolean.TRUE.equals(property.getIsIndex())) {
                whereProperties.add(property);
            }
        }

        if (whereCheck && whereProperties.isEmpty()) {
            throw new NpDbException("where cause can't be empty.");
        }

        return whereProperties;
    }


    protected String appendPrefix(ColumnMapping parameterMapping) {
        StringBuilder propertyBuilder = new StringBuilder();

        if (!StringUtils.isEmpty(parameterMapping.getColumnPrefix())) {
            propertyBuilder
                    .append(parameterMapping.getColumnPrefix())
                    .append(".");
        }

        //view extend
        String column = parameterMapping.getColumn();
        if (column.contains("$")) {
            column = column.substring(column.indexOf("$") + 1, column.length());
        }

        propertyBuilder.append(column);

        return propertyBuilder.toString();
    }

    protected void appendHolder(StringBuilder renderSql, BizProperty property) {
        Object[] values = property.getValues();
        boolean isFirst = true;
        for (int i = 0; i < values.length; i++) {
            if (isFirst) {
                renderSql.append(VALUE_HOLDER_SPACE);
                isFirst = false;
            } else {
                renderSql.append(",").append(VALUE_HOLDER_SPACE);
            }
        }
    }

    protected void checkValues(int length, BizProperty property, boolean eq) {
        Object[] values = property.getValues();

        if (eq) {
            if (null == values || values.length != length) {
                throw new NpDbException("BizProperty values can't be empty,and length should be " + length);
            }
        } else {
            if (null == values || values.length < length) {
                throw new NpDbException("BizProperty values can't be empty,and length should greater than" + length);
            }
        }
    }

    protected void paramsRender(List<ColumnMapping> queryParams, Map<String, Object> valueMap, ColumnMapping parameterMapping, Object[] values) {

        for (int i = 0; i < values.length; i++) {

            String newPropertyName = parameterMapping.getColumn() + "_" + queryParams.size() + "_" + i;

            ColumnMapping parameterMappingTmp = new ColumnMapping
                    .Builder(newPropertyName, parameterMapping.getTypeHandler())
                    .columnPrefix(parameterMapping.getColumnPrefix())
                    .javaType(parameterMapping.getJavaType())
                    .jdbcType(parameterMapping.getJdbcType())
                    .build();

            queryParams.add(parameterMappingTmp);
            valueMap.put(newPropertyName, values[i]);
        }

    }

    protected void likeValueRender(BizProperty property) {
        Object[] values = property.getValues();

        property.setValues(new Object[]{interLikeValueRender(values[0])});
    }

    protected String interLikeValueRender(Object value) {
        return new StringBuilder("%").append(value.toString()).append("%").toString();
    }
}
