package com.np.design.ui;

import java.util.Map;

public interface GridModelExtend {

    void checkRow(Map<String, Object> rowData, String column, Object oraginalValue, Object[] rowArray, String[] header);

    Boolean isColumnCellEditable(Object[] rowData, String columnName);

    void save(String[] header, Object[][] data);
}
