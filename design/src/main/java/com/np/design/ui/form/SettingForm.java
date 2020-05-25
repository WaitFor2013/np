package com.np.design.ui.form;

import com.alibaba.fastjson.JSON;
import com.np.database.ColumnDefinition;
import com.np.database.NpDataType;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.biz.param.Direction;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.orm.session.SqlSession;
import com.np.design.NoRepeatApp;
import com.np.design.domain.NpDataBaseCache;
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
import com.np.design.domain.misc.GridField;
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

    private static SettingForm settingForm;

    private void initEnv(NpDatasource datasource) {
        DefaultSqlSession session = null;
        try {
            session = (DefaultSqlSession) datasource.getSession(false);

            //check table
            BizParam tableQuery = BizParam.NEW()
                    .equals(ColumnDefinition.name("schemaname"), "public")
                    .in(ColumnDefinition.name("tablename"), new Object[]{"schema_table", "schema_column"});
            List<PgTable> dbTables = session.queryAll(tableQuery, PgTable.class);

            if (null != dbTables) {
                //如果表记录为空，删除表
                for (PgTable pgTable : dbTables) {
                    int count = 0;
                    if ("schema_table".equals(pgTable.getTablename())) {
                        count = session.count(BizParam.NEW(), SchemaTable.class);
                    }
                    if ("schema_column".equals(pgTable.getTablename())) {
                        count = session.count(BizParam.NEW(), SchemaColumn.class);
                    }
                    if (count > 0) {
                        throw new NpException("表：" + pgTable.getTablename() + "记录不为空，先自行备份表数据！！！");
                    }
                    session.executeDDL("drop table " + pgTable.getTablename());

                    //删除表索引
                    BizParam indexQuery = BizParam.NEW()
                            .equals(ColumnDefinition.name("schemaname"), "public")
                            .equals(ColumnDefinition.name("tablename"), pgTable.getTablename());
                    List<PgIndex> pgIndices = session.queryAll(indexQuery, PgIndex.class);
                    log.info("索引信息{}", JSON.toJSON(pgIndices));
                    if (null != pgIndices) {
                        for (PgIndex pgIndex : pgIndices) {
                            session.executeDDL(" DROP INDEX " + pgIndex.getIndexname());
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

            session.commit();

        } catch (Exception ex) {
            ExceptionHandler.handle(ex);
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


        envInitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NpDatasource datasource = NpDataBaseCache.getDatasource();
                initEnv(datasource);
                JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, datasource.getName() + "环境初始化成功.", "提示",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });

        loadFromOtherBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                NpDatasource datasource = NpDataBaseCache.getDatasource();
                initEnv(datasource);
                //选择环境
                //从本地环境同步
                NpDbConfig dbConfig = NpDbConfig.builder()
                        .dbTypeEnum(DbTypeEnum.POSTGRESQL)
                        .ip("127.0.0.1")
                        .port(8432)
                        .dbName("postgres")
                        .user("postgres")
                        .password("postgres")
                        .build();

                NpDatasource localDatasource = new NpDatasource(dbConfig);
                SqlSession localSession = localDatasource.getSession(true);

                List<SchemaTable> schemaTables = localSession.queryAll(BizParam.NEW(), SchemaTable.class);
                List<SchemaColumn> schemaColumns = localSession.queryAll(BizParam.NEW()
                        .orderBy(ColumnDefinition.name("id"), Direction.ASC), SchemaColumn.class);

                SqlSession otherSession = datasource.getSession(false);

                for (int i = 0; i < schemaTables.size(); i++) {
                    otherSession.create(schemaTables.get(i));
                }

                for (int i = 0; i < schemaColumns.size(); i++) {
                    otherSession.create(schemaColumns.get(i));
                }

                try {
                    localSession.close();
                    otherSession.commit();
                    otherSession.close();
                } catch (Exception exs) {
                    log.error("同步错误", exs);
                }

                JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, datasource.getName() + "从本地同步到" + datasource.getName() + "成功", "提示",
                        JOptionPane.INFORMATION_MESSAGE);

            }
        });


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
