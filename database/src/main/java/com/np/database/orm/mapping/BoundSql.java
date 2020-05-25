package com.np.database.orm.mapping;


import com.np.database.reflection.property.PropertyTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An actual SQL String got from an {link SqlSource} after having processed any dynamic content.
 * The SQL may have SQL placeholders "?" and an list (ordered) of an parameter mappings
 * with the additional information for each parameter (at least the param name of the input object to read
 * the value from).
 * <p>
 * Can also have additional parameters that are created by the dynamic language (for loops, bind...).
 *
 * @author Clinton Begin
 */
public class BoundSql {

    private final String sql;
    private final List<ColumnMapping> parameterMappings;
    private final Map<String, Object> additionalParameters;

    private final Object parameterObject;

    public BoundSql(String sql, List<ColumnMapping> parameterMappings, Object parameterObject) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap<>();
    }

    public BoundSql(String sql, List<ColumnMapping> parameterMappings, Map<String, Object> valueMap) {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.additionalParameters = valueMap;

        parameterObject = null;
    }

    public String getSql() {
        return sql;
    }

    public List<ColumnMapping> getParameterMappings() {
        return parameterMappings;
    }

    public Object getParameterObject() {
        return parameterObject;
    }

    public boolean hasAdditionalParameter(String name) {
        String paramName = new PropertyTokenizer(name).getName();
        return additionalParameters.containsKey(paramName);
    }

    public Map<String, Object> getAdditionalParameters() {
        return additionalParameters;
    }

    public void setAdditionalParameter(String name, Object value) {
        additionalParameters.put(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return additionalParameters.get(name);
    }
}
