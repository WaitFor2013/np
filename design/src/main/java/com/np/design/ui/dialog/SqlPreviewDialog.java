package com.np.design.ui.dialog;

import com.np.database.orm.mapping.ColumnMapping;
import com.np.design.ui.frame.MainFrame;
import com.np.design.ui.frame.MainFrame;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.event.*;
import java.util.List;
import java.util.Map;

public class SqlPreviewDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTable resultTable;

    private static SqlPreviewDialog sqlPreviewDialog;

    public static SqlPreviewDialog getInstance() {
        if (sqlPreviewDialog == null) {
            sqlPreviewDialog = new SqlPreviewDialog();
        }
        return sqlPreviewDialog;
    }

    public void setData(List<ColumnMapping> columns, List<Map> result) {
        ShowModel showModel = new ShowModel(columns, result);
        resultTable.setModel(showModel);

        resultTable.getColumnModel().getColumn(0).setMaxWidth(50);
    }

    public SqlPreviewDialog() {
        this.setTitle("SQL结果集预览");
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        MainFrame.setPreferSizeAndLocateToCenter(this, 0.8, 0.7);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static class ShowModel extends AbstractTableModel {

        private final List<ColumnMapping> columns;
        private final List<Map> result;

        public ShowModel(List<ColumnMapping> columns, List<Map> result) {
            this.columns = columns;
            this.result = result;
        }

        @Override
        public int getRowCount() {
            return result.size();
        }

        @Override
        public int getColumnCount() {
            return columns.size() + 1;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            if (columnIndex == 0) {
                return rowIndex + 1;
            }

            String columnName = getColumnName(columnIndex);
            return result.get(rowIndex).get(columnName);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if (columnIndex == 0)
                return false;

            return true;
        }

        public String getColumnName(int column) {
            if (column == 0)
                return "";

            return columns.get(column - 1).getColumn();
        }
    }
}
