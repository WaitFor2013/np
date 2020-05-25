package com.np.database.orm.mapping;


import java.util.Collections;
import java.util.List;

/**
 * @author Clinton Begin
 */
public class ParameterMap {

  private String id;
  private Class<?> type;
  private List<ColumnMapping> parameterMappings;

  private ParameterMap() {
  }

  public static class Builder {
    private ParameterMap parameterMap = new ParameterMap();

    public Builder(String id, Class<?> type, List<ColumnMapping> parameterMappings) {
      parameterMap.id = id;
      parameterMap.type = type;
      parameterMap.parameterMappings = parameterMappings;
    }

    public Class<?> type() {
      return parameterMap.type;
    }

    public ParameterMap build() {
      //lock down collections
      parameterMap.parameterMappings = Collections.unmodifiableList(parameterMap.parameterMappings);
      return parameterMap;
    }
  }

  public String getId() {
    return id;
  }

  public Class<?> getType() {
    return type;
  }

  public List<ColumnMapping> getParameterMappings() {
    return parameterMappings;
  }

}
