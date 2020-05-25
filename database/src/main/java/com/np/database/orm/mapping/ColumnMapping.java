package com.np.database.orm.mapping;

import com.np.database.orm.NpOrmContext;
import com.np.database.orm.type.JdbcType;
import com.np.database.orm.type.TypeHandler;
import com.np.database.orm.type.TypeHandlerRegistry;

/**
 * @author Clinton Begin
 * <p>
 */
public class ColumnMapping {

    // 参数前缀
    private String columnPrefix;
    private String column;

    private String pojoProperty;

    private Class<?> javaType = Object.class;
    private JdbcType jdbcType;
    private TypeHandler<?> typeHandler;


    private ColumnMapping() {
    }

    public static class Builder {
        private ColumnMapping columnMapping = new ColumnMapping();

        public Builder(String column, TypeHandler<?> typeHandler) {
            columnMapping.column = column;
            columnMapping.typeHandler = typeHandler;
        }

        public Builder(String column, Class<?> javaType) {
            columnMapping.column = column;
            columnMapping.javaType = javaType;
        }

        public Builder columnPrefix(String columnPrefix) {
            columnMapping.columnPrefix = columnPrefix;
            return this;
        }

        public Builder pojoProperty(String pojoProperty) {
            columnMapping.pojoProperty = pojoProperty;
            return this;
        }

        public Builder javaType(Class<?> javaType) {
            columnMapping.javaType = javaType;
            return this;
        }


        public Builder jdbcType(JdbcType jdbcType) {
            columnMapping.jdbcType = jdbcType;
            return this;
        }


        public ColumnMapping build() {
            resolveTypeHandler();
            validate();
            return columnMapping;
        }

        private void validate() {

            if (columnMapping.typeHandler == null) {
                throw new IllegalStateException("Type handler was null on parameter mapping for param '"
                        + columnMapping.column + "'. It was either not specified and/or could not be found for the javaType ("
                        + columnMapping.javaType.getName() + ") : jdbcType (" + columnMapping.jdbcType + ") combination.");
            }

        }

        private void resolveTypeHandler() {
            if (columnMapping.typeHandler == null && columnMapping.javaType != null) {
                TypeHandlerRegistry typeHandlerRegistry = NpOrmContext.getTypeHandlerRegistry();
                columnMapping.typeHandler = typeHandlerRegistry.getTypeHandler(columnMapping.javaType, columnMapping.jdbcType);
            }
        }

    }

    public String getColumn() {
        return column;
    }

    public String getPojoProperty() {
        if (null == pojoProperty || pojoProperty.isEmpty()) {
            return column;
        }
        return pojoProperty;
    }


    /**
     * Used for handling output of callable statements.
     *
     * @return
     */
    public Class<?> getJavaType() {
        return javaType;
    }

    /**
     * Used in the UnknownTypeHandler in case there is no handler for the param type.
     *
     * @return
     */
    public JdbcType getJdbcType() {
        return jdbcType;
    }


    /**
     * Used when setting parameters to the PreparedStatement.
     *
     * @return
     */
    public TypeHandler<?> getTypeHandler() {
        return typeHandler;
    }

    public String getColumnPrefix() {
        return columnPrefix;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ColumnMapping{");
        sb.append("param='").append(column).append('\'');
        sb.append(", javaType=").append(javaType);
        sb.append(", jdbcType=").append(jdbcType);
        sb.append('}');
        return sb.toString();
    }
}
