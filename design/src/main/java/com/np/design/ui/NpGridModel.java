package com.np.design.ui;

import com.np.design.domain.misc.GridHelper;
import com.np.design.exception.ExceptionHandler;
import com.np.design.exception.NpException;
import com.np.design.domain.misc.GridHelper;
import lombok.extern.slf4j.Slf4j;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class NpGridModel extends AbstractTableModel {

    private Class<?> clazz;
    private String[] header;
    private Class[] columnType;
    private Object[][] data;
    private GridModelExtend gridModelExtend;
    private List<Integer> changedRow = new ArrayList<>();
    public final List<Integer> showCounter = new ArrayList<>();

    public NpGridModel(Class<?> clazz, GridModelExtend gridModelExtend) {
        this.clazz = clazz;
        this.header = GridHelper.getColumns(clazz);
        this.columnType = GridHelper.getColumnTypes(clazz);
        this.gridModelExtend = gridModelExtend;
    }

    public void setData(Object[][] newData) {
        data = newData;
        fireTableDataChanged();
    }

    public void initChange(){
        gridModelExtend.save(header, data);
    }

    public void addEmptyRow() {
        Object[] emptyRow;
        if (null != clazz) {
            emptyRow = GridHelper.getInitValues(clazz);
        } else {
            emptyRow = new Object[header.length];
        }
        if (null == data) {
            data = new Object[1][];
            data[0] = emptyRow;
        } else {
            Object[][] newData = new Object[data.length + 1][];
            newData[0] = emptyRow;
            System.arraycopy(data, 0, newData, 1, data.length);
            data = newData;
        }

        gridModelExtend.save(header, data);
        fireTableDataChanged();
    }

    public void deleteRow(int row) {
        Object[][] newData = new Object[data.length - 1][];
        for (int i = 0, j = 0; i < data.length; i++) {
            if (row != i) {
                newData[j] = data[i];
                j++;
            }
        }
        data = newData;

        gridModelExtend.save(header, data);
        fireTableDataChanged();
    }

    public Map<String, Object> getRow(int row) {
        Map<String, Object> rowMap = new HashMap<>();
        Object[] rowArray = data[row];
        for (int i = 0; i < rowArray.length; i++) {
            rowMap.put(getColumnName(i), rowArray[i]);
        }
        return rowMap;
    }

    public Object[] getRowArray(int row) {
        return data[row];
    }

    public void setRowArray(int row, Object[] rowArray) {
        data[row] = rowArray;
        fireTableRowsUpdated(row, row);
    }

    public void upOrDown(int row, boolean isUp) {
        if (isUp && 0 == row) {
            throw new NpException("第一行不能上移");
        }
        if (!isUp && row == data.length - 1) {
            throw new NpException("最后一行不能下移");
        }
        Object[] temp = data[row];
        if (isUp) {
            data[row] = data[row - 1];
            data[row - 1] = temp;
            fireTableRowsUpdated(row - 1, row);
        } else {
            data[row] = data[row + 1];
            data[row + 1] = temp;
            fireTableRowsUpdated(row, row + 1);
        }

        gridModelExtend.save(header, data);
    }

    public Class getColumnClass(int c) {
        return columnType[c];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        Object[] rowData = data[rowIndex];
        return gridModelExtend.isColumnCellEditable(rowData, getColumnName(columnIndex));
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (null != data) {
            return data[rowIndex][columnIndex];
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            Object originalValue = data[rowIndex][columnIndex];
            if (null == aValue && null == originalValue) {
                //never change
                return;
            }
            if (null != aValue && null != originalValue) {
                if (aValue.toString().equals(originalValue.toString())) {
                    //never change
                    return;
                }
            }

            Map<String, Object> rowMap = new HashMap<>();
            Object[] rowArray = data[rowIndex];
            for (int i = 0; i < rowArray.length; i++) {
                if (i == columnIndex) {
                    rowMap.put(getColumnName(i), aValue);
                } else {
                    rowMap.put(getColumnName(i), rowArray[i]);
                }
            }


            gridModelExtend.checkRow(rowMap, getColumnName(columnIndex), originalValue, rowArray, header);

            data[rowIndex][columnIndex] = aValue;

            gridModelExtend.save(header, data);
            fireTableRowsUpdated(rowIndex, rowIndex);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler.handle(e);
        }
    }

    public String getColumnName(int column) {
        return header[column];
    }

    @Override
    public int getRowCount() {
        return null == data ? 0 : data.length;
    }

    @Override
    public int getColumnCount() {
        return header.length;
    }

    public String[] getHeader() {
        return header;
    }

    public Class[] getColumnType() {
        return columnType;
    }
}
