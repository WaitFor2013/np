package com.np.database.orm.biz.db.render;


import com.np.database.NpViewDefinition;
import com.np.database.exception.NpDbException;
import com.np.database.orm.SqlCommandType;
import com.np.database.orm.biz.BizSqlRender;
import com.np.database.orm.biz.param.BizFullText;
import com.np.database.orm.biz.param.BizOrderBy;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.BizProperty;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.sql.PagerUtils;
import com.np.database.sql.util.JdbcConstants;
import com.np.database.sql.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * postgreSQL render
 */
@Slf4j
public class PostgreSQLRender implements BizSqlRender {

    private static final String BLACK_SPACE = " ";
    private static final String AND = " and ";
    private static final String OR = " or ";
    private static final String VALUE_HOLDER_SPACE = " ? ";
    private static final String SPLIT = ",";

    private String dbVersion = "11.6";

    private SqlCommandType sqlCommandType;

    private PostgreSQLRender() {
    }

    public static final PostgreSQLRender INSTANCE = new PostgreSQLRender();

    @Override
    public BoundSql renderQuery(String noWhereSql, Map<String, ColumnMapping> allParams, BizParam bizParam, boolean isCount) {

        StringBuilder whereCauseSql = new StringBuilder();
        List<ColumnMapping> queryParams = new ArrayList<>();
        Map<String, Object> valueMap = new HashMap<>();

        Boolean isNeedAnd = false;

        BizProperty[] properties = bizParam.getProperties();
        if (null != properties && properties.length > 0) {

            whereCauseSql.append(BLACK_SPACE).append("WHERE").append(BLACK_SPACE);

            for (BizProperty bizProperty : properties) {

                ColumnMapping parameterMapping = allParams.get(bizProperty.getColumn());
                if (null == parameterMapping) {
                    throw new NpDbException("parameter not set : " + bizProperty.getColumn());
                }

                if (isNeedAnd) {
                    whereCauseSql.append(AND);
                }

                if (null == bizProperty.getBizOperatorToken()) {
                    throw new NpDbException("BizOperatorToken can't be null:" + bizProperty.getColumn());
                }

                //KEY operate value
                switch (bizProperty.getBizOperatorToken()) {
                    case EQ:
                        checkValues(1, bizProperty, true);
                        //支持多个列查询
                        if (null != bizProperty.getHasColumns()) {
                            hasColumnsDo(whereCauseSql,bizProperty,"=",allParams,queryParams,valueMap);
                        } else {
                            whereCauseSql.append(appendPrefix(parameterMapping));
                            whereCauseSql.append(BLACK_SPACE).append("=").append(VALUE_HOLDER_SPACE);
                        }


                        break;
                    case NEQ:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append("!=").append(VALUE_HOLDER_SPACE);
                        break;
                    case LT:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append("<").append(VALUE_HOLDER_SPACE);
                        break;
                    case LTEQ:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append("<=").append(VALUE_HOLDER_SPACE);
                        break;
                    case GT:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append(">").append(VALUE_HOLDER_SPACE);
                        break;
                    case GTEQ:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append(">=").append(VALUE_HOLDER_SPACE);
                        break;
                    case LIKE:
                        checkValues(1, bizProperty, true);
                        //value append with %value%
                        likeValueRender(bizProperty);

                        if (null != bizProperty.getHasColumns()) {
                            hasColumnsDo(whereCauseSql,bizProperty,"like",allParams,queryParams,valueMap);
                        } else {
                            whereCauseSql.append(appendPrefix(parameterMapping));
                            whereCauseSql.append(BLACK_SPACE).append("like").append(VALUE_HOLDER_SPACE);
                        }
                        break;
                    case NOT_LIKE:
                        checkValues(1, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append("not like").append(VALUE_HOLDER_SPACE);
                        //value append with %value%
                        likeValueRender(bizProperty);
                        break;
                    case IS_BETWEEN:
                        checkValues(2, bizProperty, true);

                        if (null != bizProperty.getHasColumns()) {
                            hasColumnsDo(whereCauseSql,bizProperty,"between",allParams,queryParams,valueMap);
                        }else {
                            whereCauseSql.append(appendPrefix(parameterMapping));
                            whereCauseSql.append(BLACK_SPACE).append(" between").append(VALUE_HOLDER_SPACE).append(AND).append(VALUE_HOLDER_SPACE);
                        }
                        break;
                    case IS_NOT_BETWEEN:
                        checkValues(2, bizProperty, true);
                        whereCauseSql.append(appendPrefix(parameterMapping));
                        whereCauseSql.append(BLACK_SPACE).append(" not between").append(VALUE_HOLDER_SPACE).append(AND).append(VALUE_HOLDER_SPACE);
                        break;
                    case IS_IN:
                        checkValues(1, bizProperty, false);
                        whereCauseSql.append(appendPrefix(parameterMapping))
                                .append(BLACK_SPACE)
                                .append("in (");
                        appendHolder(whereCauseSql, bizProperty);
                        whereCauseSql.append(")");
                        break;
                    case IS_NOT_IN:
                        checkValues(1, bizProperty, false);
                        whereCauseSql.append(appendPrefix(parameterMapping))
                                .append(BLACK_SPACE)
                                .append("not in (");
                        appendHolder(whereCauseSql, bizProperty);
                        whereCauseSql.append(")");
                        break;
                    case ARRAY_CONTAINS:
                        checkValues(1, bizProperty, false);
                        whereCauseSql.append(appendPrefix(parameterMapping))
                                .append(BLACK_SPACE)
                                .append(" @> ");
                        appendHolder(whereCauseSql, bizProperty);
                        break;
                    case ARRAY_OVERLAP:
                        checkValues(1, bizProperty, false);
                        whereCauseSql.append(appendPrefix(parameterMapping))
                                .append(BLACK_SPACE)
                                .append(" && ");
                        appendHolder(whereCauseSql, bizProperty);
                        break;

                    default:
                        throw new NpDbException("unsupported OperatorToken " + bizProperty.getBizOperatorToken());
                }

                //param render
                if (null == bizProperty.getHasColumns()) {
                    paramsRender(queryParams, valueMap, allParams.get(bizProperty.getColumn()), bizProperty.getValues());
                }

                isNeedAnd = true;
            }
        }

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


        if (noWhereSql.contains(NpViewDefinition.WHERE_ANNOTATION)) {
            noWhereSql = noWhereSql.replace(NpViewDefinition.WHERE_ANNOTATION, whereCauseSql.toString());
        } else {
            noWhereSql = noWhereSql + whereCauseSql.toString();
        }

        if (isCount) {
            noWhereSql = PagerUtils.count(noWhereSql, JdbcConstants.POSTGRESQL);
        }
        StringBuilder renderSql = new StringBuilder();
        renderSql.append(noWhereSql);

        //order support
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

        //limit support
        if (!isCount && null != bizParam.getOffset() && null != bizParam.getSize()) {
            //LIMIT 100 OFFSET 2
            renderSql.append(" LIMIT ").append(bizParam.getSize())
                    .append(" OFFSET ").append(bizParam.getOffset())
                    .append(BLACK_SPACE);
        }


        return new BoundSql(renderSql.toString(), queryParams, valueMap);
    }

    private void hasColumnsDo(StringBuilder whereCauseSql, BizProperty bizProperty,String operator,
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

    @Override
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

    private void renderUpdateOrDeleteWhereCause(StringBuilder renderSql, List<BizProperty> whereProperties, Map<String, ColumnMapping> parameterMappings,
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

    @Override
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

    @Override
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

    private List<BizProperty> validate(Map<String, ColumnMapping> parameterMappings, BizProperty[] properties, boolean whereCheck) {
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


    private String appendPrefix(ColumnMapping parameterMapping) {
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

    private void appendHolder(StringBuilder renderSql, BizProperty property) {
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

    private void checkValues(int length, BizProperty property, boolean eq) {
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

    private void paramsRender(List<ColumnMapping> queryParams, Map<String, Object> valueMap, ColumnMapping parameterMapping, Object[] values) {

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

    private void likeValueRender(BizProperty property) {
        Object[] values = property.getValues();

        property.setValues(new Object[]{interLikeValueRender(values[0])});
    }

    private String interLikeValueRender(Object value) {
        return new StringBuilder("%").append(value.toString()).append("%").toString();
    }

}
