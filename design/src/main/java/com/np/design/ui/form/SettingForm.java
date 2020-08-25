package com.np.design.ui.form;

import com.alibaba.fastjson.JSON;import com.np.database.ColumnDefinition;
import com.np.database.NpDataType;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.design.NoRepeatApp;
import com.np.design.domain.NpDataBaseCache;
import com.np.design.domain.SqlFile;
import com.np.design.domain.code.generate.PoCodeGenerate;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaParam;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.db.SchemaView;
import com.np.design.domain.ddl.PgIndex;
import com.np.design.domain.ddl.PgTable;
import com.np.design.domain.ddl.PostgresDDLHelper;
import com.np.design.domain.misc.GridField;
import com.np.design.domain.vo.DatabaseVO;
import com.np.design.exception.ExceptionHandler;
import com.np.design.exception.NpException;
import com.np.design.ui.dialog.CommonTipsDialog;
import com.np.design.ui.dialog.SqlExportDialog;
import com.np.design.ui.listener.NpComboBoxListener;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Slf4j
public class SettingForm {

    private JTable dbtable;
    private JTextField connectName;
    private JTextField dbhost;
    private JTextField dbport;
    private JButton saveButton;
    private JTextField dbname;
    private JTextField dbuser;
    private JPasswordField dbpassword;
    private JButton deleteButton;
    private JPanel panel;
    private JPanel settingPanel;
    private JButton connectButton;
    private JTextField dbType;
    private JLabel dbTypeLabel;
    private JButton envInitBtn;
    private JButton schemaPoBtn;
    private JButton loadFromOtherBtn;
    private JButton exportBtn;

    private static SettingForm settingForm;

    private void initEnv(NpDatasource datasource) {

        DefaultSqlSession session = (DefaultSqlSession) datasource.getSession(false);

        try {

            Statement statement = session.getConnection().createStatement();

            //check table
            BizParam tableQuery = BizParam.NEW()
                    .equals(ColumnDefinition.name("schemaname"), "public")
                    .in(ColumnDefinition.name("tablename"), new Object[]{"schema_table", "schema_column", "schema_view", "schema_param"});

            List<PgTable> dbTables = session.queryAll(tableQuery, PgTable.class);

            if (null != dbTables) {
                //如果表记录为空，删除表
                for (PgTable pgTable : dbTables) {

                    ResultSet resultSet = statement.executeQuery("select count(1) from " + pgTable.getTablename());

                    if (null != resultSet && resultSet.next()) {
                        int count = resultSet.getInt(1);
                        if (count > 0) {
                            throw new NpException("表：" + pgTable.getTablename() + "记录不为空，先自行备份表数据！！！");
                        }
                    }

                    statement.execute("drop table " + pgTable.getTablename());

                    //删除表索引
                    BizParam indexQuery = BizParam.NEW()
                            .equals(ColumnDefinition.name("schemaname"), "public")
                            .equals(ColumnDefinition.name("tablename"), pgTable.getTablename());
                    List<PgIndex> pgIndices = session.queryAll(indexQuery, PgIndex.class);
                    log.info("索引信息{}", JSON.toJSON(pgIndices));
                    if (null != pgIndices) {
                        for (PgIndex pgIndex : pgIndices) {
                            statement.execute(" DROP INDEX " + pgIndex.getIndexname());
                        }
                    }

                }
                //删除后，先提交一次
                session.commit();
            }

            //创建表,表名唯一 AND 表缩写唯一
            session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaTable.class));
            String uqTableNameIdx = String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                    , "schema_table", "table_name", "schema_table", "table_name");
            session.executeDDL(uqTableNameIdx);
            String uqTableAbbrIdx = String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                    , "schema_table", "table_abbr", "schema_table", "table_abbr");
            session.executeDDL(uqTableAbbrIdx);

            //创建表
            session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaColumn.class));
            String uqTableAndColumnIdx = String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                    , "schema_column", "table_name_column_name", "schema_column", "table_name,column_name");
            session.executeDDL(uqTableAndColumnIdx);


            session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaView.class));
            session.executeDDL(String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                    , "schema_view", "schema_view_name", "schema_view", "view_name"));


            session.executeDDL(PostgresDDLHelper.generateCreateSQL(SchemaParam.class));
            session.executeDDL(String.format(" create unique index %s_idx_unq_%s on %s  using btree (%s)"
                    , "schema_param", "schema_param_name", "schema_param", "view_name,table_name,column_name"));

            session.commit();

        } catch (SQLException e) {
            throw new NpException("执行失败", e);
        } finally {

            if (null != session) {
                try {
                    session.close();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    private SettingForm() {

        Object[] columnNames = {"连接名", "主机", "端口", "数据库", "用户名", "数据库类型"};


        DefaultTableModel model = new DefaultTableModel(NpDataBaseCache.getStoreDatabaseModel(), columnNames);
        dbtable.setModel(model);
        DefaultTableCellRenderer hr = (DefaultTableCellRenderer) dbtable.getTableHeader().getDefaultRenderer();
        hr.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    DatabaseVO instance = DatabaseVO.getInstance(connectName,
                            dbhost,
                            dbport,
                            dbname,
                            dbuser,
                            dbpassword);

                    DefaultTableModel model = new DefaultTableModel(NpDataBaseCache.addDatabase(instance), columnNames);
                    dbtable.setModel(model);

                } catch (NpException ex) {
                    ExceptionHandler.handle(ex);
                }
            }
        });


        envInitBtn.addActionListener(new NpComboBoxListener(() -> {

            NpDatasource datasource = NpDataBaseCache.getDatasource();
            initEnv(datasource);
            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, datasource.getName() + "环境初始化成功.", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

        }));

        loadFromOtherBtn.addActionListener(new NpComboBoxListener(() -> {
            NpDatasource datasource = NpDataBaseCache.getDatasource();

            SqlExportDialog sqlExportDialog = SqlExportDialog.getInstance();
            sqlExportDialog.setTitle("表定义导入");
            sqlExportDialog.setVisible(true);

            String filePath = sqlExportDialog.getFilePath();
            if (null == filePath || filePath.isEmpty()) {
                throw new NpException("输入目录不允许为空");
            }

            int result = JOptionPane.showConfirmDialog(NoRepeatApp.mainFrame,
                    "数据库定义将被重置：【    " + datasource.getName() +
                            "   】\n\n 核对数据库！！！\n 核对数据库！！！\n 核对数据库！！！\n 核对数据库！！！ \n 核对数据库！！！ \n 核对数据库！！！ \n 核对数据库！！！ \n 核对数据库！！！ \n\n" +
                            "【  " + NpDataBaseCache.connectName + "    】定义将被重置。",
                    "请确认",
                    JOptionPane.YES_NO_OPTION);

            if (result != 0) {
                throw new NpException("动作已取消");
            }

            try (
                    Connection connection = datasource.getDataSource().getConnection();
                    Statement statement = connection.createStatement();
            ) {

                connection.setAutoCommit(false);

                initData(statement, filePath, "schema_table");
                initData(statement, filePath, "schema_column");
                initData(statement, filePath, "schema_view");
                initData(statement, filePath, "schema_param");

                connection.commit();

            } catch (Exception ex) {
                throw new NpException("同步失败:" + ex.getMessage(), ex);
            }

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, datasource.getName() + "：表定义同步成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);

        }));

        exportBtn.addActionListener(new NpComboBoxListener(() -> {
            NpDatasource datasource = NpDataBaseCache.getDatasource();

            SqlExportDialog sqlExportDialog = SqlExportDialog.getInstance();
            sqlExportDialog.setTitle("表定义导出");
            sqlExportDialog.setVisible(true);


            String filePath = sqlExportDialog.getFilePath();
            if (null == filePath || filePath.isEmpty()) {
                throw new NpException("输出目录不允许为空");
            }

            try (
                    Connection connection = datasource.getDataSource().getConnection();
                    Statement statement = connection.createStatement();
            ) {

                connection.setAutoCommit(true);

                writeFile(statement, filePath, "schema_table");
                writeFile(statement, filePath, "schema_column");
                writeFile(statement, filePath, "schema_view");
                writeFile(statement, filePath, "schema_param");

            } catch (Exception ex) {

                throw new NpException("导出失败", ex);
            }


            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, datasource.getName() + "：表定义导出成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }));

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int[] selectedRows = dbtable.getSelectedRows();
                    if (null == selectedRows || selectedRows.length == 0) {
                        throw new NpException("未选中连接信息。");
                    }

                    DefaultTableModel model = new DefaultTableModel(NpDataBaseCache.removeDatabases(settingPanel, selectedRows), columnNames);
                    dbtable.setModel(model);

                } catch (NpException ex) {
                    ExceptionHandler.handle(ex);
                }
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int[] selectedRows = dbtable.getSelectedRows();
                    if (null == selectedRows || selectedRows.length != 1) {
                        throw new NpException("请选择单条记录");
                    }


                    NpDataBaseCache.setWorkDatabase(selectedRows[0]);

                } catch (NpException ex) {
                    ExceptionHandler.handle(ex);
                }
            }
        });

        schemaPoBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                CommonTipsDialog commonTipsDialog = CommonTipsDialog.getInstance();
                commonTipsDialog.setVisible(true);
                if (commonTipsDialog.isOk()) {
                    List<SchemaTable> tables = new ArrayList<>();
                    Map<String, List<SchemaColumn>> columns = new HashMap<>();
                    codeInit(tables, columns);

                    for (SchemaTable schemaTable : tables) {

                        String fileContent = PoCodeGenerate.generate(commonTipsDialog.getPackage(), schemaTable, columns.get(schemaTable.getTableName()));
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
            }
        });
    }

    public void initData(Statement statement, String filePath, String table) {

        try {
            String fileContent = FileUtils.readFileToString(new File(filePath + File.separator + table + ".sql"));
            String[] split = fileContent.split(";");
            if (null == split || split.length == 0) {
                throw new NpException("文件格式不正确：" + table);
            }

            statement.execute("delete from " + table);
            for (String temp : split) {
                statement.execute(temp);
            }

        } catch (IOException e) {
            throw new NpException("未找到文件:" + table + ".sql", e);
        } catch (SQLException e) {
            throw new NpException("SQL执行错误:" + table, e);
        }

    }

    public void writeFile(Statement statement, String filePath, String table) throws SQLException, IOException {
        ResultSet resultSet = statement.executeQuery("select * from " + table);

        StringBuilder fileContent = new StringBuilder();
        while (resultSet.next()) {
            String sql = SqlFile.mapRow(resultSet);
            fileContent.append(sql).append("\n");
        }
        FileUtils.write(new File(filePath + File.separator + table + ".sql"), fileContent.toString());
        resultSet.close();
    }


    private void codeInit(List<SchemaTable> tables, Map<String, List<SchemaColumn>> columns) {
        setClassDefer(SchemaTable.class, "表定义", tables, columns);
        setClassDefer(SchemaColumn.class, "列定义", tables, columns);
        setClassDefer(SchemaView.class, "视图定义", tables, columns);
        setClassDefer(SchemaParam.class, "视图参数定义", tables, columns);
    }

    private void setClassDefer(Class<?> clazz, String comment, List<SchemaTable> tables, Map<String, List<SchemaColumn>> columns) {
        SchemaTable codeTable = new SchemaTable();
        Table tableAnnotation = clazz.getAnnotation(Table.class);
        codeTable.setTableName(tableAnnotation.name());
        codeTable.setTableAbbr("");
        codeTable.setTableComment(comment);
        tables.add(codeTable);

        List<SchemaColumn> tableColumns = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            Column columnAnnotation = field.getAnnotation(Column.class);
            GridField gridFieldAnnotation = field.getAnnotation(GridField.class);
            if (null != columnAnnotation) {
                SchemaColumn schemaColumn = new SchemaColumn();
                schemaColumn.setColumnName(columnAnnotation.name());
                schemaColumn.setColumnComment("");
                if (null != gridFieldAnnotation) {
                    schemaColumn.setColumnComment(gridFieldAnnotation.name());
                }
                if (field.getType().isAssignableFrom(Integer.class)) {
                    schemaColumn.setColumnType(NpDataType.INT.toString());
                }
                if (field.getType().isAssignableFrom(String.class)) {
                    schemaColumn.setColumnType(NpDataType.STR256.toString());
                }
                if (field.getType().isAssignableFrom(Boolean.class)) {
                    schemaColumn.setColumnType(NpDataType.BOOL.toString());
                }
                tableColumns.add(schemaColumn);
            }
        }
        columns.put(tableAnnotation.name(), tableColumns);

    }

    public static SettingForm getInstance() {
        if (settingForm == null) {
            settingForm = new SettingForm();
        }
        return settingForm;
    }


}
