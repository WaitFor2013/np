package com.np.design.ui;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class JTabelFrame extends JFrame {

    public JTabelFrame() { // 实现构造方法
        super("RecorderOfWorkers"); // 首先调用父类JFrame的构造方法生成一个窗口
        MyTableModel myModel = new MyTableModel();// myModel存放表格的数据
        JTable table = new JTable(myModel);// 表格对象table的数据来源是myModel对象
        table.setPreferredScrollableViewportSize(new Dimension(500, 100));// 表格的显示尺寸

        JScrollPane scrollpanel = new JScrollPane(table);  // 产生一个带滚动条的面板
        // 将带滚动条的面板添加入窗口中
        getContentPane().add(scrollpanel, BorderLayout.CENTER);

        addWindowListener(new WindowAdapter() {// 注册窗口监听器
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    class MyTableModel extends AbstractTableModel {
        String[] TableHead = {"姓名", "居住地", "语文", "数学", "总分", "及格", "作弊"}; //表格标题
        Object[][] TableDate = {
                {"一心", "广州", new Integer(60), new Integer(80), new Integer(140), new Boolean(false), new Boolean(false)},
                {"不安", "河南", new Integer(70), new Integer(80), new Integer(150), new Boolean(false), new Boolean(false)},
        };//表格数据
        String update = "修改字段位置为:";

        //获得列的数目
        public int getColumnCount() {
            return TableHead.length;
        }

        //获得行的数目
        public int getRowCount() {
            return TableDate.length;
        }

        //获得标题列的数据,并将数据存放在字符串tablehead数组里
        public String getColumnName(int column) {
            return TableHead[column];
        }

        @Override
        public Object getValueAt(int row, int column) {
            return TableDate[row][column];
        }

        //判断单元格类型
        public Class getColumnClass(int c) {
            return getValueAt(0, c).getClass();
        }

        //将表格声明为可编辑状态
        public boolean isCellEditable(int row, int column) {

            if (column < 0) {
                return false;
            } else {
                return true;
            }
        }

        //改变某个数据
        public void setValueAt(Object value, int row, int column) {
            boolean debug = true;
            if (debug) {
                System.out.println("Setting value at " + row + "," + column
                        + " to " + value + " (an instance of "
                        + value.getClass() + ")");
            }
            if (TableDate[0][column] instanceof Integer && !(value instanceof Integer)) {
                try {
                    TableDate[row][column] = new Integer(value.toString());
                    fireTableCellUpdated(row, column);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(JTabelFrame.this, "The \""
                            + getColumnName(column)
                            + "\" column accepts only integer values.");
                }
            } else {
                TableDate[row][column] = value;
                fireTableCellUpdated(row, column);
            }

            if (debug) {
                System.out.println("New value of data:");
                printDebugData();
            }

        }

        private void printDebugData() {
            int numRows = getRowCount();
            int numCols = getColumnCount();

            for (int i = 0; i < numRows; i++) {
                System.out.print(" row " + i + ":");
                for (int j = 0; j < numCols; j++) {
                    System.out.print(" " + TableDate[i][j]);
                }
                System.out.println();
            }
            System.out.println("--------------------------");
        }

    }

    public static void main(String[] args) {
        JTabelFrame frame = new JTabelFrame();
        frame.pack();
        frame.setVisible(true);
    }

}