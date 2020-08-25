package com.np.database.orm.biz.param;

import com.np.database.ColumnDefinition;
import com.np.database.orm.biz.BizOperatorToken;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BizParam {

    private BizProperty[] properties;

    private BizFullText fullTextProperty;

    private List<BizOrderBy> bizOrderByList;

    private Integer offset;

    private Integer size;

    private BizParam() {

    }

    public static BizParam NEW() {
        return new BizParam();
    }

    public BizParam index(ColumnDefinition column, Object object) {
        if (null != object && !object.toString().isEmpty()) {
            internalAdd(BizProperty.builder()
                    .column(column.getColumnName())
                    .bizOperatorToken(BizOperatorToken.EQ)
                    .values(new Object[]{object})
                    .isIndex(true)
                    .build());
        }
        return this;
    }

    /**
     * 值等于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam equals(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.EQ, object);
    }


    public BizParam hasEquals(Object object, ColumnDefinition... columnDefinitions) {
        if (null == object || object.toString().isEmpty()) {
            return this;
        }

        if (null == columnDefinitions || columnDefinitions.length == 0) {
            return this;
        }

        if (columnDefinitions.length == 1) {
            return equals(columnDefinitions[1], object);
        }

        String[] columns = new String[columnDefinitions.length];
        for (int i = 0; i < columnDefinitions.length; i++) {
            columns[i] = columnDefinitions[i].getColumnName();
        }

        BizProperty bizProperty = BizProperty.builder()
                .column(columns[0])
                .bizOperatorToken(BizOperatorToken.EQ)
                .values(new Object[]{object})
                .hasColumns(columns)
                .build();

        internalAdd(bizProperty);
        return this;

    }

    /**
     * 值不等于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam notEquals(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.NEQ, object);
    }


    /**
     * 值小于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam lessThan(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.LT, object);
    }

    /**
     * 值小于等于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam lessEquals(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.LTEQ, object);
    }

    /**
     * 值大于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam greater(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.GT, object);
    }

    /**
     * 值大于等于
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam greaterEquals(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.GTEQ, object);
    }

    /**
     * 值包含
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam like(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.LIKE, object);
    }

    public BizParam hasLike(Object object, ColumnDefinition... columnDefinitions) {
        if (null == object || object.toString().isEmpty()) {
            return this;
        }

        if (null == columnDefinitions || columnDefinitions.length == 0) {
            return this;
        }

        if (columnDefinitions.length == 1) {
            return like(columnDefinitions[1], object);
        }

        String[] columns = new String[columnDefinitions.length];
        for (int i = 0; i < columnDefinitions.length; i++) {
            columns[i] = columnDefinitions[i].getColumnName();
        }

        BizProperty bizProperty = BizProperty.builder()
                .column(columns[0])
                .bizOperatorToken(BizOperatorToken.LIKE)
                .values(new Object[]{object})
                .hasColumns(columns)
                .build();

        internalAdd(bizProperty);
        return this;
    }

    /**
     * 值不包含
     *
     * @param column
     * @param object
     * @return
     */
    public BizParam notLike(ColumnDefinition column, Object object) {
        return property(column, BizOperatorToken.NOT_LIKE, object);
    }

    /**
     * 介于两者之间
     *
     * @param column
     * @param from
     * @param to
     * @return
     */
    public BizParam between(ColumnDefinition column, Object from, Object to) {

        if (null != from && null != to) {
            Object[] array = new Object[2];
            array[0] = from;
            array[1] = to;

            internalAdd(BizProperty.builder()
                    .column(column.getColumnName())
                    .bizOperatorToken(BizOperatorToken.IS_BETWEEN)
                    .values(array)
                    .build());
        }
        return this;
    }

    public BizParam hasBetween(Object from, Object to, ColumnDefinition... columnDefinitions) {

        if (null != from && null != to) {
            if (null == columnDefinitions || columnDefinitions.length == 0) {
                return this;
            }

            if (columnDefinitions.length == 1) {
                return between(columnDefinitions[1], from, to);
            }

            String[] columns = new String[columnDefinitions.length];
            for (int i = 0; i < columnDefinitions.length; i++) {
                columns[i] = columnDefinitions[i].getColumnName();
            }

            Object[] array = new Object[2];
            array[0] = from;
            array[1] = to;

            internalAdd(BizProperty.builder()
                    .column(columns[0])
                    .bizOperatorToken(BizOperatorToken.IS_BETWEEN)
                    .values(array)
                    .hasColumns(columns)
                    .build());


        }

        return this;
    }

    /**
     * 不介于两者之间
     *
     * @param column
     * @param from
     * @param to
     * @return
     */
    public BizParam notBetween(ColumnDefinition column, Object from, Object to) {
        if (null != from && null != to) {
            Object[] array = new Object[2];
            array[0] = from;
            array[1] = to;

            internalAdd(BizProperty.builder()
                    .column(column.getColumnName())
                    .bizOperatorToken(BizOperatorToken.IS_NOT_BETWEEN)
                    .values(array)
                    .build());
        }
        return this;
    }

    /**
     * 在...之中
     *
     * @param column
     * @param objects
     * @return
     */
    public BizParam in(ColumnDefinition column, Object[] objects) {
        if (null != objects && objects.length > 0) {
            internalAdd(BizProperty.builder()
                    .column(column.getColumnName())
                    .bizOperatorToken(BizOperatorToken.IS_IN)
                    .values(objects)
                    .build());
        }
        return this;
    }

    /**
     * 不在...之中
     *
     * @param column
     * @param objects
     * @return
     */
    public BizParam notIn(ColumnDefinition column, Object[] objects) {
        if (null != objects && objects.length > 0) {
            internalAdd(BizProperty.builder()
                    .column(column.getColumnName())
                    .bizOperatorToken(BizOperatorToken.IS_NOT_IN)
                    .values(objects)
                    .build());
        }
        return this;
    }

    public BizParam arrayContains(ColumnDefinition column, List<?> object) {
        if (null != object && !object.isEmpty()) {
            return property(column, BizOperatorToken.ARRAY_CONTAINS, object);
        }
        return this;
    }

    public BizParam arrayOverlap(ColumnDefinition column, List<?> object) {
        if (null != object && !object.isEmpty()) {
            return property(column, BizOperatorToken.ARRAY_OVERLAP, object);
        }
        return this;
    }

    private BizParam property(ColumnDefinition column, BizOperatorToken operator, Object value) {
        if (null == value || value.toString().isEmpty()) {
            return this;
        }
        internalAdd(BizProperty.builder()
                .column(column.getColumnName())
                .bizOperatorToken(operator)
                .values(new Object[]{value})
                .build());
        return this;
    }


    /**
     * 全文搜索
     *
     * @param value             搜索值
     * @param columnDefinitions 搜索列
     * @return
     */
    public BizParam fullText(String value, ColumnDefinition... columnDefinitions) {
        if (null != value && !value.isEmpty() && null != columnDefinitions && columnDefinitions.length != 0) {
            String[] columns = new String[columnDefinitions.length];
            for (int i = 0; i < columnDefinitions.length; i++) {
                columns[i] = columnDefinitions[i].getColumnName();
            }
            this.fullTextProperty = new BizFullText(columns, value);
        }
        return this;
    }

    /**
     * 排序字段
     *
     * @param columnDefinition 字段
     * @param direction        正序、倒序
     * @return
     */
    public BizParam orderBy(ColumnDefinition columnDefinition, Direction direction) {

        if (null == bizOrderByList) {
            bizOrderByList = new ArrayList<>();
        }

        bizOrderByList.add(new BizOrderBy(columnDefinition.getColumnName(), direction));

        return this;
    }

    /**
     * 分页
     *
     * @param offset
     * @param size
     * @return
     */
    public BizParam page(Integer offset, Integer size) {
        this.offset = offset;
        this.size = size;

        return this;
    }

    private void internalAdd(BizProperty bizProperty) {
        if (null == properties) {
            properties = new BizProperty[1];
        } else {
            BizProperty[] newProperties = new BizProperty[properties.length + 1];
            for (int i = 0; i < properties.length; i++) {
                newProperties[i] = properties[i];
            }
            properties = newProperties;
        }
        properties[properties.length - 1] = bizProperty;
    }
}
