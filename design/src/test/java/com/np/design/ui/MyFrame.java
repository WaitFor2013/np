package com.np.design.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;

class MyFrame extends JFrame {
    private static final long serialVersionUID = 1L;

    public MyFrame() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dimension = toolkit.getScreenSize();
        setSize((int) (dimension.width * 0.5), (int) (dimension.height * 0.5));
        // 设置窗体居中显示
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JTable table = new JTable();
        //设置表格的数据模型
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        //设置表头
        model.setColumnIdentifiers(new Object[]{"1", "2"});
        table.setModel(model);
        //自动创建RowSorter
        table.setAutoCreateRowSorter(true);
        //添加一行内容
        model.addRow(new Object[]{"A", "4"});
        model.addRow(new Object[]{"B", "9"});
        model.addRow(new Object[]{"A", "5"});
        JScrollPane pane = new JScrollPane();
        //将JTable添加到JScrollPane中
        pane.setViewportView(table);
        add(pane);
        setVisible(true);
        //这里是关键
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<DefaultTableModel>(model);
        table.setRowSorter(sorter);
        //实现过滤，search为正则表达式，该方法支持参数为null和空字符串，因此不用对输入进行校验
        String search = "5";
        sorter.setRowFilter(RowFilter.regexFilter(search));
    }

    public static void main(String[] args) {
        new MyFrame();
    }
}