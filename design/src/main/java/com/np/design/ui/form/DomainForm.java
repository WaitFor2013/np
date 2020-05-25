package com.np.design.ui.form;

import com.np.database.orm.session.SqlSession;
import com.np.database.recommend.SysColumn;
import com.np.design.NoRepeatApp;
import com.np.design.domain.NpDataBaseCache;
import com.np.design.domain.NpModule;
import com.np.design.domain.TableDesign;
import com.np.design.domain.code.generate.PoCodeGenerate;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.ddl.PgConsistencyCheck;
import com.np.design.domain.misc.GridHelper;
import com.np.design.exception.NpException;
import com.np.design.ui.GridModelExtend;
import com.np.design.ui.NpComboBoxModel;
import com.np.design.ui.NpGridModel;
import com.np.design.ui.dialog.CommonTipsDialog;
import com.np.design.ui.listener.NpComboBoxListener;
import com.np.design.ui.listener.NpTableActionListener;
import com.np.design.domain.misc.GridHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Getter
@Slf4j
public class DomainForm {
    private JTable tableSchema;
    private JTable columnSchema;
    private JButton addTableBtn;
    private JButton addColumn;
    private JButton removeColumn;
    private JButton upColumnBtn;
    private JButton downColumnBtn;
    private JButton deleteTableBtn;
    private JPanel domainPanel;
    private JPanel tablePanel;
    private JPanel columnPanel;
    private JButton loadButton;
    private JButton deploy;
    private JLabel label;
    private JLabel selectTableJL;
    private JComboBox modelComboBox;
    private JLabel mJLabel;
    private JButton codeGenerateBtn;
    private JTextPane checkTextPane;
    private JScrollPane checkScroll;
    private JButton alterTableBtn;

    private NpGridModel tableSchemaModel;

    private NpGridModel columnSchemaModel;

    private static DomainForm domainForm;

    private DomainForm() {

        //table
        tableSchemaModel = new NpGridModel(SchemaTable.class, new GridModelExtend() {
            @Override
            public void checkRow(Map<String, Object> rowData, String column, Object oraginalValue, Object[] rowArray, String[] header) {

                SchemaTable.checkRow(rowData, column, oraginalValue);
                //change release
                rowArray[rowArray.length - 1] = false;
                SchemaTable schemaTable = SchemaTable.newInstance(header, rowArray);

                tableSelect(schemaTable, columnSchemaModel);
            }

            @Override
            public Boolean isColumnCellEditable(Object[] rowData, String columnName) {
                return GridHelper.checkEditable(columnName, SchemaTable.class);
            }

            @Override
            public void save(String[] header, Object[][] data) {
                TableDesign.saveTables(header, data);
            }
        });
        tableSchema.setModel(tableSchemaModel);
        GridHelper.initTable(tableSchema, tableSchemaModel, SchemaTable.class);

        addTableBtn.addActionListener(new NpTableActionListener(tableSchema, false, () -> {
            if (!TableDesign.isDbLoad()) {
                throw new NpException("必须先读取DB数据。");
            }
            tableSchemaModel.addEmptyRow();
        }));

        deploy.addActionListener(new NpTableActionListener(tableSchema, () -> {

            int selectedRow = tableSchema.getSelectedRow();
            if (!tableSchemaModel.showCounter.isEmpty()) {
                selectedRow = tableSchemaModel.showCounter.get(tableSchema.getSelectedRow());
            }

            //table valid
            SchemaTable schemaTable = SchemaTable.newInstance(tableSchemaModel.getHeader(), tableSchemaModel.getRowArray(selectedRow));
            if (null != schemaTable.getIsRelease() && schemaTable.getIsRelease()) {
                throw new NpException("未发生变更，无需发布。");
            }

            SchemaTable.validateRow(schemaTable);
            //column valid
            List<SchemaColumn> columns = TableDesign.getColumns(schemaTable.getTableName());
            if (null == columns || columns.isEmpty()) {
                throw new NpException("列不允许为空");
            }
            int custom = 0;
            int hasLabel = 0;
            int hasSelf = 0;

            for (int i = 0; i < columns.size(); i++) {
                SchemaColumn schemaColumn = columns.get(i);
                Set<String> valueSet = SysColumn.getValueSet();
                if (!valueSet.contains(schemaColumn.getColumnName())) {
                    SchemaColumn.valid(schemaColumn, true);
                    custom++;
                    //log.info("here {} {} ", custom, schemaColumn.getColumnName());
                }
                if (schemaColumn.getIsAuth()) {
                    hasLabel++;
                }

                if ("SELF".equals(schemaColumn.getRender())) {
                    hasSelf++;
                }

            }

            if (hasLabel != 1) {
                throw new NpException("表必须指定一个名称列");
            }

            if (hasSelf > 1) {
                throw new NpException("表关联表SELF必须小于1。");
            }

            if (custom == 0) {
                throw new NpException("必须有自定义列");
            }
            //deploy
            TableDesign.deploy(schemaTable, columns);

            schemaTable.setIsRelease(true);
            tableSchemaModel.setRowArray(selectedRow, schemaTable.getRow());

            checkTextPane.setText("无");

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "表[" + schemaTable.getTableName() + "]发布成功。", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

        }));

        alterTableBtn.addActionListener(new NpTableActionListener(tableSchema, () -> {

            int selectedRow = tableSchema.getSelectedRow();
            if (!tableSchemaModel.showCounter.isEmpty()) {
                selectedRow = tableSchemaModel.showCounter.get(tableSchema.getSelectedRow());
            }

            //table valid
            SchemaTable schemaTable = SchemaTable.newInstance(tableSchemaModel.getHeader(), tableSchemaModel.getRowArray(selectedRow));
            if (null != schemaTable.getIsRelease() && schemaTable.getIsRelease()) {
                throw new NpException("未发生变更，无需发布。");
            }

            SchemaTable.validateRow(schemaTable);
            //column valid
            List<SchemaColumn> columns = TableDesign.getColumns(schemaTable.getTableName());
            if (null == columns || columns.isEmpty()) {
                throw new NpException("列不允许为空");
            }
            int custom = 0;
            int hasLabel = 0;
            int hasSelf = 0;

            for (int i = 0; i < columns.size(); i++) {
                SchemaColumn schemaColumn = columns.get(i);
                Set<String> valueSet = SysColumn.getValueSet();
                if (!valueSet.contains(schemaColumn.getColumnName())) {
                    SchemaColumn.valid(schemaColumn, true);
                    custom++;
                    //log.info("here {} {} ", custom, schemaColumn.getColumnName());
                }
                if (schemaColumn.getIsAuth()) {
                    hasLabel++;
                }

                if ("SELF".equals(schemaColumn.getRender())) {
                    hasSelf++;
                }

            }

            if (hasLabel != 1) {
                throw new NpException("表必须指定一个名称列");
            }

            if (hasSelf > 1) {
                throw new NpException("表关联表SELF必须小于1。");
            }

            if (custom == 0) {
                throw new NpException("必须有自定义列");
            }
            //deploy
            TableDesign.alterDeploy(schemaTable, columns);

            schemaTable.setIsRelease(true);
            tableSchemaModel.setRowArray(selectedRow, schemaTable.getRow());

            checkTextPane.setText("无");

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "表[" + schemaTable.getTableName() + "]发布成功。", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

        }));

        //column
        columnSchemaModel = new NpGridModel(SchemaColumn.class, new GridModelExtend() {

            @Override
            public void checkRow(Map<String, Object> rowData, String column, Object oraginalValue, Object[] rowArray, String[] header) {
                SchemaColumn.checkRow(rowData, rowArray, header, column);
                //row change update table
                String tableName = selectTableJL.getText();
                List<SchemaTable> tables = TableDesign.getTables();
                for (int i = 0; i < tables.size(); i++) {
                    SchemaTable schemaTable = tables.get(i);
                    if (tableName.equals(schemaTable.getTableName())) {
                        schemaTable.setIsRelease(false);
                        tableSchemaModel.setRowArray(i, schemaTable.getRow());
                    }
                }
            }

            @Override
            public Boolean isColumnCellEditable(Object[] rowData, String columnName) {
                if (SchemaColumn.isRowCanEdit(rowData)) {
                    return GridHelper.checkEditable(columnName, SchemaTable.class);
                } else {
                    return false;
                }
            }

            @Override
            public void save(String[] header, Object[][] data) {
                TableDesign.saveColumns(selectTableJL.getText(), header, data);
            }
        });

        columnSchema.setModel(columnSchemaModel);
        GridHelper.initTable(columnSchema, columnSchemaModel, SchemaColumn.class);

        addColumn.addActionListener(new NpTableActionListener(columnSchema, false, () -> {
            String tableName = selectTableJL.getText();
            if (null == tableName || tableName.isEmpty() || "无".equals(tableName)) {
                throw new NpException("当前表为空。");
            }
            columnSchemaModel.addEmptyRow();
        }));
        removeColumn.addActionListener(new NpTableActionListener(columnSchema, () -> {
            if (SchemaColumn.isRowCanEdit(columnSchemaModel.getRowArray(columnSchema.getSelectedRow()))) {
                columnSchemaModel.deleteRow(columnSchema.getSelectedRow());

                //row change update table
                String tableName = selectTableJL.getText();
                List<SchemaTable> tables = TableDesign.getTables();
                for (int i = 0; i < tables.size(); i++) {
                    SchemaTable schemaTable = tables.get(i);
                    if (tableName.equals(schemaTable.getTableName())) {
                        schemaTable.setIsRelease(false);
                        tableSchemaModel.setRowArray(i, schemaTable.getRow());
                    }
                }
            } else {
                throw new NpException("默认字段不允许编辑。");
            }
        }));
        upColumnBtn.addActionListener(new NpTableActionListener(columnSchema, () -> {
            int selectedRow = columnSchema.getSelectedRow();
            columnSchemaModel.upOrDown(selectedRow, true);
            columnSchema.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
        }));
        downColumnBtn.addActionListener(new NpTableActionListener(columnSchema, () -> {
            int selectedRow = columnSchema.getSelectedRow();
            columnSchemaModel.upOrDown(selectedRow, false);
            columnSchema.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
        }));

        tableSchema.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                int selectedRow = tableSchema.getSelectedRow();

                if (selectedRow < 0)
                    return;

                if (!tableSchemaModel.showCounter.isEmpty()) {
                    //log.info("select {} {}", selectedRow, JSON.toJSON(tableSchemaModel.showCounter));
                    selectedRow = tableSchemaModel.showCounter.get(selectedRow);
                }

                SchemaTable schemaTable = SchemaTable.newInstance(tableSchemaModel.getHeader(), tableSchemaModel.getRowArray(selectedRow));

                //log.info("select {} {}  {}", schemaTable.getTableName(), selectedRow, JSON.toJSONString(tableSchemaModel.showCounter));

                tableSelect(schemaTable, columnSchemaModel);
            }
        });

        deleteTableBtn.addActionListener(new NpTableActionListener(tableSchema, () -> {

            int selectedRow = tableSchema.getSelectedRow();
            if (!tableSchemaModel.showCounter.isEmpty()) {
                selectedRow = tableSchemaModel.showCounter.get(tableSchema.getSelectedRow());
            }

            SchemaTable schemaTable = SchemaTable.newInstance(tableSchemaModel.getHeader(), tableSchemaModel.getRowArray(selectedRow));
            if (TableDesign.hasTableInDb(schemaTable.getTableName())) {
                int result = JOptionPane.showConfirmDialog(NoRepeatApp.mainFrame,
                        "已发布表删除，会同步删除数据库表，结构和数据都会删除！！！" +
                                "\n  三思而后行！！！\n  三思而后行！！！\n  三思而后行！！！" +
                                "\n\n  返回再想想？？？ ",
                        "请确认",
                        JOptionPane.YES_NO_OPTION);
                if (!(result == JOptionPane.NO_OPTION)) {
                    return;
                }
            }

            TableDesign.removeColumns(schemaTable.getTableName(), TableDesign.hasTableInDb(schemaTable.getTableName()));
            tableSchemaModel.deleteRow(selectedRow);
            selectTableJL.setText("无");
            checkTextPane.setText("无");
            columnSchemaModel.setData(null);
        }));

        loadButton.addActionListener(new NpTableActionListener(columnSchema, false, () -> {
            if (!TableDesign.getTables().isEmpty()) {
                int result = JOptionPane.showConfirmDialog(NoRepeatApp.mainFrame,
                        "确定重新从数据库中加载表定义",
                        "请确认",
                        JOptionPane.YES_NO_OPTION);
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
            }
            selectTableJL.setText("无");
            checkTextPane.setText("无");
            columnSchemaModel.setData(null);
            //load tables
            List<SchemaTable> schemaTables = TableDesign.loadDb(true);

            Object[][] newData = new Object[schemaTables.size()][];
            for (int i = 0; i < schemaTables.size(); i++) {
                newData[i] = schemaTables.get(i).getRow();
            }
            tableSchemaModel.setData(newData);

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "数据库加载记录成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }));

        //模块选择，筛选表记录
        TableRowSorter<NpGridModel> sorter = new TableRowSorter<NpGridModel>(tableSchemaModel);

        for (int i = 0; i < tableSchemaModel.getHeader().length; i++) {
            sorter.setSortable(i, false);
        }

        tableSchema.setRowSorter(sorter);

        modelComboBox.setModel(new NpComboBoxModel(NpModule.getListModules()));
        modelComboBox.addActionListener(new NpComboBoxListener(() -> {

            Object selectedItem = modelComboBox.getSelectedItem();
            //log.info("modelComboBox select {}", selectedItem);

            if (selectedItem.toString().equals(NpModule.ALL)) {
                tableSchemaModel.showCounter.clear();
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(new RowFilter<NpGridModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends NpGridModel, ? extends Integer> entry) {
                        NpGridModel npGridModel = entry.getModel();
                        Integer i = entry.getIdentifier();
                        if (i == 0) {
                            tableSchemaModel.showCounter.clear();
                        }
                        SchemaTable schemaTable = SchemaTable.newInstance(npGridModel.getHeader(), npGridModel.getRowArray(i));

                        if (null == schemaTable.getTableModule() || schemaTable.getTableModule().isEmpty() || selectedItem.toString().equals(schemaTable.getTableModule())) {
                            //log.info("Filter{}", i);
                            npGridModel.showCounter.add(i);
                            return true;
                        }
                        return false;
                    }
                });
            }

        }));

        codeGenerateBtn.addActionListener(new NpTableActionListener(tableSchema, () -> {

            int[] selectedRows = tableSchema.getSelectedRows();
            List<SchemaTable> tables = new ArrayList<>();
            for (int i : selectedRows) {
                int selectedRow = i;
                if (!tableSchemaModel.showCounter.isEmpty()) {
                    selectedRow = tableSchemaModel.showCounter.get(i);
                }
                SchemaTable schemaTable = SchemaTable.newInstance(tableSchemaModel.getHeader(), tableSchemaModel.getRowArray(selectedRow));
                if (!schemaTable.getIsRelease()) {
                    throw new NpException("表未发布，不允许代码生成");
                }
                tables.add(schemaTable);
            }
            CommonTipsDialog commonTipsDialog = CommonTipsDialog.getInstance();
            commonTipsDialog.setVisible(true);
            if (commonTipsDialog.isOk()) {
                for (SchemaTable schemaTable : tables) {
                    String fileContent = PoCodeGenerate.generate(commonTipsDialog.getPackage(), schemaTable, TableDesign.getColumns(schemaTable.getTableName()));
                    String fileName = PoCodeGenerate.toCamel(schemaTable.getTableName(), true) + PoCodeGenerate.PO + ".java";
                    try {
                        FileUtils.write(new File(commonTipsDialog.getFilePath() + File.separator + fileName), fileContent);
                    } catch (IOException e) {
                        log.error("文件写入失败", e);
                        throw new NpException("文件写入失败", e);
                    }
                }
                JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "代码生成成功.\n目录：" + commonTipsDialog.getFilePath(), "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }));
    }

    private void tableSelect(SchemaTable schemaTable, NpGridModel columnSchemaModel) {

        if (null != schemaTable.getTableName() && !schemaTable.getTableName().isEmpty()) {
            String tableName = schemaTable.getTableName();

            selectTableJL.setText(tableName);
            List<SchemaColumn> columns = TableDesign.getColumns(tableName);

            SqlSession session = null;
            try {
                session = NpDataBaseCache.getDatasource().getSession(true);
                String checkMsg = PgConsistencyCheck.doCheckSingle(session, schemaTable, columns);
                checkTextPane.setText(checkMsg);
            } finally {
                if (null != session) {
                    try {
                        session.close();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }

            if (null != columns) {
                Object[][] newData = new Object[columns.size()][];
                for (int i = 0; i < columns.size(); i++) {
                    newData[i] = columns.get(i).getRow();
                }
                columnSchemaModel.setData(newData);
            } else {
                //init
                columnSchemaModel.setData(null);
            }
        } else {
            selectTableJL.setText("无");
            checkTextPane.setText("无");
            columnSchemaModel.setData(null);
        }
        columnSchemaModel.fireTableStructureChanged();
        GridHelper.initTable(columnSchema, columnSchemaModel, SchemaColumn.class);

    }

    public static DomainForm getInstance() {
        if (domainForm == null) {
            domainForm = new DomainForm();
        }
        return domainForm;
    }

}
