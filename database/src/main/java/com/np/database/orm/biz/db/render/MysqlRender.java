package com.np.database.orm.biz.db.render;


import com.np.database.NpViewDefinition;
import com.np.database.exception.NpDbException;
import com.np.database.orm.SqlCommandType;
import com.np.database.orm.biz.BizSqlRender;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.BizProperty;
import com.np.database.orm.mapping.BoundSql;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.sql.PagerUtils;
import com.np.database.sql.util.JdbcConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * mysql render
 */
@Slf4j
public class MysqlRender extends BaseRender implements BizSqlRender {

    private String dbVersion = "5.7";

    private SqlCommandType sqlCommandType;

    private MysqlRender() {
    }

    public static final MysqlRender INSTANCE = new MysqlRender();

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
                            hasColumnsDo(whereCauseSql, bizProperty, "=", allParams, queryParams, valueMap);
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
                            hasColumnsDo(whereCauseSql, bizProperty, "like", allParams, queryParams, valueMap);
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
                            hasColumnsDo(whereCauseSql, bizProperty, "between", allParams, queryParams, valueMap);
                        } else {
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
        fullText(isNeedAnd, bizParam, whereCauseSql, queryParams, valueMap, allParams);

        if (noWhereSql.contains(NpViewDefinition.WHERE_ANNOTATION)) {
            noWhereSql = noWhereSql.replace(NpViewDefinition.WHERE_ANNOTATION, whereCauseSql.toString());
        } else {
            noWhereSql = noWhereSql + whereCauseSql.toString();
        }

        if (isCount) {
            noWhereSql = PagerUtils.count(noWhereSql, JdbcConstants.MYSQL);
        }
        StringBuilder renderSql = new StringBuilder();
        renderSql.append(noWhereSql);

        //order support
        orderSupport(isCount,bizParam,allParams,renderSql);

        //limit support
        if (!isCount && null != bizParam.getOffset() && null != bizParam.getSize()) {
            //LIMIT 100 OFFSET 2
            renderSql.append(" LIMIT ").append(bizParam.getOffset())
                    .append(" , ").append(bizParam.getSize())
                    .append(BLACK_SPACE);
        }


        return new BoundSql(renderSql.toString(), queryParams, valueMap);
    }

}
