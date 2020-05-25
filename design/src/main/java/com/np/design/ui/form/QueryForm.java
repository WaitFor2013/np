package com.np.design.ui.form;

import com.np.database.NpDataType;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.mapping.ColumnMapping;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.orm.type.JdbcType;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.parser.PGSQLStatementParser;
import com.np.database.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.np.design.NoRepeatApp;
import com.np.design.domain.NpDataBaseCache;
import com.np.design.domain.NpModule;
import com.np.design.domain.ViewDesign;
import com.np.design.domain.code.generate.PoCodeGenerate;
import com.np.design.domain.code.generate.ViewCodeGenerate;
import com.np.design.domain.db.SchemaParam;
import com.np.design.domain.db.SchemaTable;
import com.np.design.domain.db.SchemaView;
import com.np.design.domain.misc.GridHelper;
import com.np.design.exception.NpException;
import com.np.design.ui.GridModelExtend;
import com.np.design.ui.NpComboBoxModel;
import com.np.design.ui.NpGridModel;
import com.np.design.ui.dialog.CommonTipsDialog;
import com.np.design.ui.dialog.SqlPreviewDialog;
import com.np.design.ui.editor.DecorateKeyWords;
import com.np.design.ui.listener.NpComboBoxListener;
import com.np.design.ui.listener.NpTableActionListener;
import com.np.design.domain.code.generate.ViewCodeGenerate;
import com.np.design.domain.misc.GridHelper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableRowSorter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@Getter
public class QueryForm {
    private JPanel queryPanel;
    private JButton dbLoadBtn;
    private JButton addQueryBtn;
    private JButton deleteQueryBtn;
    private JButton deployBtn;
    private JButton codeBtn;
    private JSplitPane sqlSplitPane;
    private JPanel sqlJPanel;
    private JTextPane sqlTextPane;
    private JButton sqlParseBtn;
    private JPanel queryListJPanel;
    private JLabel moduleLabel;
    private JComboBox moduleComboBox;

    private JTable queryTable;
    private JTable resultTable;
    private JLabel viewLabel;
    private JLabel currentView;
    private JButton previewBtn;
    private NpGridModel queryTableModel;
    private NpGridModel paramTableModel;

    private DecorateKeyWords decorateKeyWords;

    private static QueryForm queryForm;

    private QueryForm() {

        decorateKeyWords = new DecorateKeyWords();

        //查询表
        queryTableModel = new NpGridModel(SchemaView.class, new GridModelExtend() {
            @Override
            public void checkRow(Map<String, Object> rowData, String column, Object originalValue, Object[] rowArray, String[] header) {

                SchemaView schemaView = SchemaView.newInstance(header, rowArray);
                if (!"模块".equals(column) && schemaView.getViewModule() == null) {
                    throw new NpException("请先选择模块。");
                }

                if ("名称".equals(column)) {
                    Object viewName = rowData.get("名称");

                    String modulePrefix = NpModule.getModulePrefix(schemaView.getViewModule());
                    String startWith = "view_";
                    if (null != viewName && !viewName.toString().startsWith(startWith)) {
                        throw new NpException("视图名必须以" + startWith + "开头。");
                    }


                    SchemaTable.nameCheck("名称", viewName.toString());

                    setCurrent(viewName.toString());

                    List<SchemaView> cacheViews = ViewDesign.cacheViews;

                    for (SchemaView temp : cacheViews) {
                        if (null != temp.getViewName() && temp.getViewName().equals(viewName.toString())) {
                            throw new NpException("视图名全局不允许重复:" + viewName.toString());
                        }
                    }


                    if (null != originalValue) {

                        if (null != ViewDesign.getDbView(originalValue.toString())) {
                            throw new NpException("已发布视图不允许修改名称，只可删除重新建立，小心操作！！！");
                        }

                        String sql = ViewDesign.cacheSql.get(originalValue.toString());
                        ViewDesign.cacheSql.remove(originalValue.toString());
                        ViewDesign.cacheSql.put(viewName.toString(), sql);

                        List<SchemaParam> params = ViewDesign.cacheParams.get(originalValue.toString());
                        ViewDesign.cacheParams.remove(originalValue.toString());
                        ViewDesign.cacheParams.put(viewName.toString(), params);

                    }


                }

            }

            @Override
            public Boolean isColumnCellEditable(Object[] rowData, String columnName) {

                if ("一致".equals(columnName)) {
                    return false;
                }

                return true;
            }

            @Override
            public void save(String[] header, Object[][] data) {
                ViewDesign.refreshViewCache(header, data);
            }
        });
        queryTable.setModel(queryTableModel);
        GridHelper.initTable(queryTable, queryTableModel, SchemaView.class);

        queryTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {

                int selectedRow = queryTable.getSelectedRow();

                if (selectedRow < 0)
                    return;

                if (!queryTableModel.showCounter.isEmpty()) {
                    selectedRow = queryTableModel.showCounter.get(selectedRow);
                }


                SchemaView schemaView = ViewDesign.cacheViews.get(selectedRow);
                if (null == schemaView.getViewName() || schemaView.getViewName().trim().isEmpty()) {
                    setCurrent("");
                    detailEmpty();
                } else {
                    setCurrent(schemaView.getViewName());
                    detailInit(schemaView);
                }
            }
        });


        //结果集表
        paramTableModel = new NpGridModel(SchemaParam.class, new GridModelExtend() {
            @Override
            public void checkRow(Map<String, Object> rowData, String column, Object oraginalValue, Object[] rowArray, String[] header) {
                viewChange();
            }

            @Override
            public Boolean isColumnCellEditable(Object[] rowData, String columnName) {

                SchemaParam schemaParam = SchemaParam.newInstance(paramTableModel.getHeader(), rowData);

                if (null == schemaParam.getTableName() || schemaParam.getTableName().isEmpty()) {
                    if ("列类型".equals(columnName) || "列中文名".equals(columnName)) {
                        return true;
                    }
                }

                return false;
            }

            @Override
            public void save(String[] header, Object[][] data) {
                ViewDesign.refreshParamCache(header, data);
            }
        });
        resultTable.setModel(paramTableModel);
        GridHelper.initTable(resultTable, paramTableModel, SchemaParam.class);

        viewListInit();

        initSql();

        //模块选择，筛选表记录
        TableRowSorter<NpGridModel> sorter = new TableRowSorter<NpGridModel>(queryTableModel);

        for (int i = 0; i < queryTableModel.getHeader().length; i++) {
            sorter.setSortable(i, false);
        }

        queryTable.setRowSorter(sorter);

        moduleComboBox.setModel(new NpComboBoxModel(NpModule.getListModules()));
        moduleComboBox.addActionListener(new NpComboBoxListener(() -> {

            Object selectedItem = moduleComboBox.getSelectedItem();

            if (selectedItem.toString().equals(NpModule.ALL)) {
                queryTableModel.showCounter.clear();
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(new RowFilter<NpGridModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends NpGridModel, ? extends Integer> entry) {
                        NpGridModel npGridModel = entry.getModel();
                        Integer i = entry.getIdentifier();
                        if (i == 0) {
                            queryTableModel.showCounter.clear();
                        }
                        SchemaView schemaView = SchemaView.newInstance(npGridModel.getHeader(), npGridModel.getRowArray(i));

                        if (null == schemaView.getViewModule() || schemaView.getViewModule().isEmpty() || selectedItem.toString().equals(schemaView.getViewModule())) {
                            //log.info("Filter{}", i);
                            npGridModel.showCounter.add(i);
                            return true;
                        }
                        return false;
                    }
                });

                detailEmpty();
                queryTable.clearSelection();
            }

        }));
    }

    private List<SchemaParam> sqlParse() {
        String sqlText = sqlTextPane.getText();
        if (null == sqlText || sqlText.trim().isEmpty()) {
            throw new NpException("SQL不允许为空。");
        }
        NpSqlHelper.FormatOption formatOption = new NpSqlHelper.FormatOption();
        formatOption.setUppCase(false);
        String formatPGSql = NpSqlHelper.formatPGSql(sqlText, formatOption);

        sqlTextPane.setText(formatPGSql);
        decorateKeyWords.decorateKeyWords(sqlTextPane);

        PGSQLStatementParser parser = new PGSQLStatementParser(formatPGSql);
        SQLStatement statement = parser.parseStatement();
        PGSchemaStatVisitor pgSchemaStatVisitor = new PGSchemaStatVisitor();
        statement.accept(pgSchemaStatVisitor);

        return ViewDesign.sqlParse(statement, pgSchemaStatVisitor);
    }

    private void initSql() {

        sqlParseBtn.addActionListener(new NpTableActionListener(resultTable, false, () -> {

            List<SchemaParam> schemaParams = sqlParse();

            Object[][] newData = new Object[schemaParams.size()][];
            for (int i = 0; i < schemaParams.size(); i++) {
                newData[i] = schemaParams.get(i).getRow();
            }
            paramTableModel.setData(newData);
            paramTableModel.initChange();
        }));

        sqlTextPane.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                String viewName = ViewDesign.currentView.get();
                if (null == viewName || viewName.isEmpty()) {
                    JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "请选选择视图，并为视图配置名称！", "错误",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    ViewDesign.cacheSql.put(viewName, sqlTextPane.getText());
                }
                decorateKeyWords.decorateKeyWords(sqlTextPane);

                viewChange();
            }
        });

        previewBtn.addActionListener(new NpTableActionListener(resultTable, false, () -> {
            sqlParse();

            String currentView = ViewDesign.currentView.get();
            if (null == currentView || currentView.isEmpty()) {
                throw new NpException("当前视图不允许为空");
            }

            List<SchemaParam> params = ViewDesign.cacheParams.get(currentView);
            if (null == params || params.isEmpty()) {
                throw new NpException("视图结果集不允许为空");
            }

            ViewDesign.checkParams(currentView, params);

            DefaultSqlSession session = null;
            try {
                NpDatasource datasource = NpDataBaseCache.getDatasource();
                session = (DefaultSqlSession) datasource.getSession(true);

                List<ColumnMapping> allColumns = new ArrayList<>();

                for (int i = 0; i < params.size(); i++) {
                    SchemaParam temp = params.get(i);

                    if (temp.getIsResult()) {
                        Class fromTypeClazz = NpDataType.getFromTypeClazz(temp.getColumnType());
                        ColumnMapping.Builder builder = new ColumnMapping.Builder(temp.getColumnName(), fromTypeClazz)
                                .pojoProperty(temp.getColumnName());
                        if (NpDataType.ARRAY_STR.toString().equals(temp.getColumnType())) {
                            builder.jdbcType(JdbcType.VARCHAR);
                        }
                        if (NpDataType.ARRAY_INT.toString().equals(temp.getColumnType())) {
                            builder.jdbcType(JdbcType.INTEGER);
                        }

                        builder.columnPrefix(temp.getTablePrefix());

                        allColumns.add(builder.build());
                    }
                }

                PGSQLStatementParser parser = new PGSQLStatementParser(sqlTextPane.getText());
                SQLStatement statement = parser.parseStatement();
                PGSelectStatement selectStatement = (PGSelectStatement) statement;
                SQLSelectQueryBlock queryBlock = selectStatement.getSelect().getFirstQueryBlock();

                String noWhereSql = sqlTextPane.getText();

                if (null != queryBlock.getWhere()) {
                    String whereCause = queryBlock.getWhere().toString();

                    noWhereSql = noWhereSql.replace(whereCause.toLowerCase(), " 1 = 1 ");
                }

                List<Map> result = session.query(noWhereSql, BizParam.NEW(), allColumns);

                SqlPreviewDialog sqlPreviewDialog = SqlPreviewDialog.getInstance();
                sqlPreviewDialog.setData(allColumns, result);

                sqlPreviewDialog.setVisible(true);

            } finally {
                if (null != session) {
                    try {
                        session.close();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }


        }));
    }

    private void setCurrent(String viewName) {
        currentView.setText(viewName);
        ViewDesign.currentView.set(viewName);
    }

    private void viewListInit() {

        dbLoadBtn.addActionListener(new NpTableActionListener(queryTable, false, () -> {

            if (!ViewDesign.cacheViews.isEmpty()) {
                int result = JOptionPane.showConfirmDialog(NoRepeatApp.mainFrame,
                        "确定重新从数据库中加载视图定义",
                        "请确认",
                        JOptionPane.YES_NO_OPTION);
                if (!(result == JOptionPane.YES_OPTION)) {
                    return;
                }
            }

            ViewDesign.dbLoad();

            List<SchemaView> cacheViews = ViewDesign.cacheViews;

            Object[][] newData = new Object[cacheViews.size()][];
            for (int i = 0; i < cacheViews.size(); i++) {
                newData[i] = cacheViews.get(i).getRow();
            }

            queryTableModel.setData(newData);

            detailEmpty();

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "数据库加载记录成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }));

        addQueryBtn.addActionListener(e -> {
            queryTableModel.addEmptyRow();
            detailEmpty();
        });

        deleteQueryBtn.addActionListener(new NpTableActionListener(queryTable, true, () -> {

            int selectedRow = queryTable.getSelectedRow();

            if (!queryTableModel.showCounter.isEmpty()) {
                selectedRow = queryTableModel.showCounter.get(selectedRow);
            }

            List<SchemaView> cacheViews = ViewDesign.cacheViews;
            SchemaView schemaView = cacheViews.get(selectedRow);

            ViewDesign.delete(schemaView);

            queryTableModel.deleteRow(selectedRow);
            detailEmpty();
        }));

        deployBtn.addActionListener(new NpTableActionListener(queryTable, true, () -> {
            //发布
            int selectedRow = queryTable.getSelectedRow();

            if (!queryTableModel.showCounter.isEmpty()) {
                selectedRow = queryTableModel.showCounter.get(selectedRow);
            }

            List<SchemaView> cacheViews = ViewDesign.cacheViews;
            SchemaView schemaView = cacheViews.get(selectedRow);

            ViewDesign.deploy(schemaView);

            schemaView.setIsRelease(true);
            queryTableModel.setRowArray(selectedRow, schemaView.getRow());

            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "发布成功", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
        }));

        codeBtn.addActionListener(new NpTableActionListener(queryTable, true, () -> {

            int[] selectedRows = queryTable.getSelectedRows();
            List<SchemaView> views = new ArrayList<>();
            for (int i : selectedRows) {
                int selectedRow = i;
                if (!queryTableModel.showCounter.isEmpty()) {
                    selectedRow = queryTableModel.showCounter.get(i);
                }
                SchemaView schemaView = SchemaView.newInstance(queryTableModel.getHeader(), queryTableModel.getRowArray(selectedRow));
                if (!schemaView.getIsRelease()) {
                    throw new NpException("视图未发布，不允许代码生成");
                }
                views.add(schemaView);
            }
            CommonTipsDialog commonTipsDialog = CommonTipsDialog.getInstance();
            commonTipsDialog.setVisible(true);
            if (commonTipsDialog.isOk()) {
                for (SchemaView schemaView : views) {
                    String fileContent = ViewCodeGenerate.generate(commonTipsDialog.getPackage(),
                            schemaView, ViewDesign.cacheSql.get(schemaView.getViewName()),
                            ViewDesign.cacheParams.get(schemaView.getViewName()));

                    String fileName = PoCodeGenerate.toCamel(schemaView.getViewName(), true) + ".java";
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

    private void detailInit(SchemaView schemaView) {
        sqlTextPane.setText(ViewDesign.cacheSql.get(schemaView.getViewName()));
        decorateKeyWords.decorateKeyWords(sqlTextPane);
        List<SchemaParam> schemaParams = ViewDesign.cacheParams.get(schemaView.getViewName());
        if (null != schemaParams && !schemaParams.isEmpty()) {
            Object[][] data = new Object[schemaParams.size()][];
            for (int i = 0; i < schemaParams.size(); i++) {
                SchemaParam temp = schemaParams.get(i);
                data[i] = temp.getRow();
            }
            paramTableModel.setData(data);
            paramTableModel.initChange();

        } else {
            paramTableModel.setData(null);
            paramTableModel.initChange();
        }
    }

    private void viewChange() {
        String current = ViewDesign.currentView.get();
        if (null == current || current.trim().isEmpty()) {
            return;
        }

        List<SchemaView> cacheViews = ViewDesign.cacheViews;
        for (int i = 0; i < cacheViews.size(); i++) {
            SchemaView schemaView = cacheViews.get(i);
            if (current.equals(schemaView.getViewName())) {
                schemaView.setIsRelease(false);
                queryTableModel.setRowArray(i, schemaView.getRow());
            }
        }
    }

    private void detailEmpty() {
        ViewDesign.currentView.set(null);
        sqlTextPane.setText("");
        paramTableModel.setData(null);
    }

    public static QueryForm getInstance() {
        if (queryForm == null) {
            queryForm = new QueryForm();
        }
        return queryForm;
    }

}
