package com.np.design.domain;

import com.np.database.ColumnDefinition;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.biz.param.BizParam;
import com.np.database.orm.session.DefaultSqlSession;
import com.np.database.recommend.SysColumn;
import com.np.database.sql.NpSqlHelper;
import com.np.database.sql.TableStat;
import com.np.database.sql.ast.SQLExpr;
import com.np.database.sql.ast.SQLStatement;
import com.np.database.sql.ast.expr.SQLAllColumnExpr;
import com.np.database.sql.ast.expr.SQLIdentifierExpr;
import com.np.database.sql.ast.expr.SQLPropertyExpr;
import com.np.database.sql.ast.statement.SQLSelectItem;
import com.np.database.sql.ast.statement.SQLSelectQueryBlock;
import com.np.database.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.np.database.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.np.design.NoRepeatApp;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaParam;
import com.np.design.domain.db.SchemaView;
import com.np.design.exception.NpException;
import com.np.design.NoRepeatApp;
import com.np.design.domain.db.SchemaColumn;
import com.np.design.domain.db.SchemaParam;
import com.np.design.domain.db.SchemaView;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
public class ViewDesign {

    private static final AtomicBoolean dbLoaded = new AtomicBoolean(false);

    private static final List<SchemaView> _dbViews = new ArrayList<>();
    private static final Map<String, List<SchemaParam>> _dbParams = new HashMap<>();

    public static final List<SchemaView> cacheViews = new ArrayList<>();
    public static final Map<String, List<SchemaParam>> cacheParams = new HashMap<>();
    public static final Map<String, String> cacheSql = new HashMap<>();

    //当前视图
    public static final AtomicReference<String> currentView = new AtomicReference<>();

    public static SchemaView getDbView(String name) {
        for (SchemaView temp : _dbViews) {
            if (temp.getViewName().equals(name))
                return temp;
        }
        return null;
    }

    private static void removeDbView(String name) {
        Iterator<SchemaView> iterator = _dbViews.iterator();
        while (iterator.hasNext()) {
            SchemaView next = iterator.next();
            if (next.getViewName().equals(name))
                iterator.remove();
        }

        _dbParams.remove(name);

    }

    private static void removeDbCache(String name) {
        Iterator<SchemaView> iterator = cacheViews.iterator();
        while (iterator.hasNext()) {
            SchemaView next = iterator.next();
            if (next.getViewName().equals(name))
                iterator.remove();
        }

        cacheParams.remove(name);
        cacheSql.remove(name);
    }

    public static void delete(SchemaView schemaView) {
        boolean hasDB = false;
        if (null == schemaView.getViewName() || schemaView.getViewName().trim().isEmpty()) {
            return;
        } else {
            if (null != getDbView(schemaView.getViewName())) {
                hasDB = true;
            }
        }

        if (hasDB) {
            int result = JOptionPane.showConfirmDialog(NoRepeatApp.mainFrame,
                    "已发布视图删除！！！" +
                            "\n  三思而后行！！！\n  三思而后行！！！\n  三思而后行！！！" +
                            "\n\n  返回再想想？？？ ",
                    "请确认",
                    JOptionPane.YES_NO_OPTION);
            if (!(result == JOptionPane.NO_OPTION)) {
                return;
            }

            DefaultSqlSession session = null;
            try {
                NpDatasource datasource = NpDataBaseCache.getDatasource();
                session = (DefaultSqlSession) datasource.getSession(false);
                SchemaParam deleteParam = new SchemaParam();
                deleteParam.setViewName(schemaView.getViewName());

                //删除
                session.deleteByColumns(schemaView, ColumnDefinition.name("view_name"));
                session.deleteByColumns(deleteParam, ColumnDefinition.name("view_name"));

                session.commit();

                removeDbView(schemaView.getViewName());
            } finally {
                if (null != session) {
                    try {
                        session.close();
                    } catch (Exception ex) {
                        //do nothing
                    }
                }
            }

        }

        removeDbCache(schemaView.getViewName());
    }

    public static void checkParams(String viewName, List<SchemaParam> params) {

        boolean hasTenantId = false;

        for (int i = 0; i < params.size(); i++) {
            SchemaParam temp = params.get(i);
            temp.setViewName(viewName);

            if (temp.getIsParam() && temp.getColumnName().equals(SysColumn.tenant_id.toString())) {
                hasTenantId = true;
            }

            if (null == temp.getColumnType()) {
                throw new NpException("列类型不允许为空:" + temp.getColumnName());
            }
            if (null == temp.getColumnComment() || temp.getColumnComment().trim().isEmpty()) {
                throw new NpException("列备注不允许为空:" + temp.getColumnName());
            }

            if (null == temp.getTablePrefix()) {
                temp.setTablePrefix("");
            }

            if (null == temp.getTableName()) {
                temp.setTableName("");
            }
        }

        if (!hasTenantId) {
            throw new NpException("视图设计必须有租户:tenant_id查询条件。");
        }
    }

    public static void deploy(SchemaView schemaView) {
        schemaView.check();

        String sql = cacheSql.get(schemaView.getViewName());
        if (null == sql || sql.isEmpty()) {
            throw new NpException("SQL不允许为空");
        }
        NpSqlHelper.FormatOption formatOption = new NpSqlHelper.FormatOption();
        formatOption.setUppCase(false);
        try {
            String formatPGSql = NpSqlHelper.formatPGSql(sql, formatOption);
            schemaView.setViewSql(formatPGSql);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NpException("SQL格式错误:" + e.getMessage());
        }

        List<SchemaParam> params = cacheParams.get(schemaView.getViewName());
        if (null == params || params.isEmpty()) {
            throw new NpException("结果集解析不允许为空");
        }

        checkParams(schemaView.getViewName(), params);


        DefaultSqlSession session = null;
        try {
            NpDatasource datasource = NpDataBaseCache.getDatasource();
            session = (DefaultSqlSession) datasource.getSession(false);
            SchemaParam deleteParam = new SchemaParam();
            deleteParam.setViewName(schemaView.getViewName());

            //删除
            session.deleteByColumns(schemaView, ColumnDefinition.name("view_name"));
            session.deleteByColumns(deleteParam, ColumnDefinition.name("view_name"));

            //重新插入
            session.create(schemaView);
            for (int i = 0; i < params.size(); i++) {
                SchemaParam schemaParam = params.get(i);
                session.create(schemaParam);
            }

            session.commit();

            Iterator<SchemaView> iterator = cacheViews.iterator();
            while (iterator.hasNext()) {
                SchemaView next = iterator.next();
                if (next.equals(schemaView.getViewName())) {
                    next.setIsRelease(true);
                }
            }

        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }

    }

    public static void refreshParamCache(String[] header, Object[][] data) {

        String viewName = currentView.get();

        if (null == viewName || viewName.isEmpty()) {
            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "请选选择视图，并为视图配置名称！", "错误",
                    JOptionPane.ERROR_MESSAGE);
        }

        cacheParams.remove(viewName);
        //保存数据
        List<SchemaParam> params = new ArrayList<>();
        if (null != data && data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                SchemaParam schemaParam = SchemaParam.newInstance(header, data[i]);
                params.add(schemaParam);
            }
        }

        cacheParams.put(viewName, params);
    }

    public static void refreshViewCache(String[] header, Object[][] data) {

        cacheViews.clear();
        //保存数据
        if (null != data && data.length > 0) {
            for (int i = 0; i < data.length; i++) {
                SchemaView schemaView = SchemaView.newInstance(header, data[i]);
                cacheViews.add(schemaView);
            }
        } else {
            cacheParams.clear();
        }

    }

    public static void dbLoad() {

        _dbViews.clear();
        _dbParams.clear();

        cacheViews.clear();
        cacheSql.clear();
        cacheParams.clear();
        dbLoaded.set(false);

        TableDesign.loadDb(false);
        DefaultSqlSession session = null;

        try {
            NpDatasource datasource = NpDataBaseCache.getDatasource();
            session = (DefaultSqlSession) datasource.getSession(false);

            List<SchemaView> schemaViews = session.queryAll(BizParam.NEW(), SchemaView.class);
            List<SchemaParam> schemaParams = session.queryAll(BizParam.NEW(), SchemaParam.class);

            for (int i = 0; i < schemaViews.size(); i++) {
                SchemaView schemaView = schemaViews.get(i);
                _dbViews.add(schemaView);

                SchemaView cacheView = SchemaView.copyFrom(schemaView);
                cacheView.setIsRelease(true);
                cacheViews.add(cacheView);
                cacheSql.put(schemaView.getViewName(), schemaView.getViewSql());
            }

            for (int j = 0; j < schemaParams.size(); j++) {

                SchemaParam schemaParam = schemaParams.get(j);
                if (!_dbParams.containsKey(schemaParam.getViewName())) {
                    _dbParams.put(schemaParam.getViewName(), new ArrayList<>());
                    cacheParams.put(schemaParam.getViewName(), new ArrayList<>());
                }


                _dbParams.get(schemaParam.getViewName()).add(schemaParam);
                cacheParams.get(schemaParam.getViewName()).add(SchemaParam.copyFrom(schemaParam));
            }

            session.commit();
            dbLoaded.set(true);

        } finally {
            if (null != session) {
                try {
                    session.close();
                } catch (Exception ex) {
                    //do nothing
                }
            }
        }
    }

    public static boolean isDbLoad() {
        return dbLoaded.get();
    }

    public static List<SchemaParam> sqlParse(SQLStatement statement, PGSchemaStatVisitor pgSchemaStatVisitor) {

        if (!dbLoaded.get()) {
            throw new NpException("请先进行【读取DB】操作。");
        }

        Map<TableStat.Name, TableStat> tables = pgSchemaStatVisitor.getTables();

        Map<String, String> tableName$prefix = pgSchemaStatVisitor.getTablePrefixMap();

        if (tableName$prefix.isEmpty()) {
            if (tables.size() != 1) {
                throw new NpException("多张表关联查询,表必须有别名。");
            }

            TableStat.Name[] names = tables.keySet().toArray(new TableStat.Name[1]);
            tableName$prefix.put(names[0].getName(), "");
        }

        Map<String, String> prefix$tableName = new HashMap<>();
        if (null != tableName$prefix && !tableName$prefix.isEmpty()) {
            tableName$prefix.entrySet().forEach(entry -> {
                if (!entry.getValue().isEmpty()) {
                    prefix$tableName.put(entry.getValue(), entry.getKey());

                }
            });
        }

        List<SchemaParam> result = new ArrayList<>();
        Set<String> paramNames = new HashSet<>();

        if (!(statement instanceof PGSelectStatement)) {
            throw new NpException("必须是查询语句，当前仅支持PG的视图设计。");
        }
        PGSelectStatement selectStatement = (PGSelectStatement) statement;
        SQLSelectQueryBlock queryBlock = selectStatement.getSelect().getFirstQueryBlock();

        List<SQLSelectItem> selectList = queryBlock.getSelectList();
        if (null == selectList || selectList.isEmpty()) {
            throw new NpException("返回参数不允许为空。");
        }

        Map<String, SchemaParam> paramCache = new HashMap<>();

        for (int i = 0; i < selectList.size(); i++) {
            SQLSelectItem sqlSelectItem = selectList.get(i);
            SQLExpr sqlExpr = sqlSelectItem.getExpr();

            SchemaParam tempParam = new SchemaParam();

            if (sqlExpr instanceof SQLPropertyExpr) {
                SQLPropertyExpr sqlPropertyExpr = (SQLPropertyExpr) sqlExpr;
                tempParam.setColumnName(sqlPropertyExpr.getName());

                if (null != sqlPropertyExpr.getOwnernName() && !sqlPropertyExpr.getOwnernName().isEmpty()) {
                    tempParam.setTablePrefix(sqlPropertyExpr.getOwnernName());
                    tempParam.setTableName(prefix$tableName.get(sqlPropertyExpr.getOwnernName()));
                } else {
                    if (tableName$prefix.size() != 1) {
                        throw new NpException("多张表关联查询必须有别名。");
                    }
                    tempParam.setTablePrefix("");
                    tempParam.setTableName(tableName$prefix.get(0));
                }

                renderTypeAndComment(tempParam);

                tempParam.setIsResult(true);
                tempParam.setIsParam(false);

                if (null != sqlSelectItem.getAlias()) {
                    tempParam.setTableName("");
                    tempParam.setColumnName(sqlSelectItem.getAlias());
                }

                paramCache.put(tempParam.getTableName() + tempParam.getColumnName(), tempParam);
            } else if (sqlExpr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr sqlIdentifierExpr = (SQLIdentifierExpr) sqlExpr;
                String columnName = sqlIdentifierExpr.getName();
                String tableName = sqlIdentifierExpr.getResolvedTableSource().toString();

                tempParam.setColumnName(columnName);
                tempParam.setTableName(tableName);
                tempParam.setTablePrefix("");
                tempParam.setIsResult(true);
                tempParam.setIsParam(false);

                renderTypeAndComment(tempParam);

                if (null != sqlSelectItem.getAlias()) {
                    tempParam.setTableName("");
                    tempParam.setColumnName(sqlSelectItem.getAlias());
                }

                paramCache.put(tempParam.getTableName() + tempParam.getColumnName(), tempParam);

            } else if (sqlExpr instanceof SQLAllColumnExpr) {
                throw new NpException("视图设计不允许使用【*】进行查询。");
            } else {
                if (null == sqlSelectItem.getAlias() || sqlSelectItem.getAlias().isEmpty()) {
                    throw new NpException("参数【" + sqlSelectItem.getExpr().toString() + "】必须as别名。");
                }
                tempParam.setColumnName(sqlSelectItem.getAlias());

                tempParam.setIsResult(true);
                tempParam.setIsParam(false);
            }

            if (paramNames.contains(tempParam.getColumnName())) {
                throw new NpException("参数名称不允许重复:" + tempParam.getColumnName());
            }
            paramNames.add(tempParam.getColumnName());

            result.add(tempParam);
        }

        //String whereCause = queryBlock.getWhere().toString();
        Collection<TableStat.Column> columns = pgSchemaStatVisitor.getColumns();
        for (TableStat.Column column : columns) {
            if (column.getName().equals("*")) {
                throw new NpException("视图设计不允许使用【*】进行查询。");
            }

            if (column.isWhere()) {

                SchemaParam tempParam = null;
                if (paramCache.containsKey(column.getTable() + column.getName())) {
                    tempParam = paramCache.get(column.getTable() + column.getName());
                    tempParam.setIsParam(true);
                } else {
                    tempParam = new SchemaParam();
                    tempParam.setTableName(column.getTable());
                    tempParam.setColumnName(column.getName());
                    if (tableName$prefix.containsKey(column.getTable())) {
                        tempParam.setTablePrefix(tableName$prefix.get(column.getTable()));
                    } else {
                        tempParam.setTablePrefix("");
                    }

                    renderTypeAndComment(tempParam);
                    tempParam.setIsParam(true);
                    tempParam.setIsResult(false);
                    result.add(tempParam);
                }

            }
        }

        return result;
    }

    private static void renderTypeAndComment(SchemaParam tempParam) {
        SchemaColumn dbColumn = TableDesign.getDbColumn(tempParam.getTableName(), tempParam.getColumnName());

        if (null == dbColumn) {
            throw new NpException("当前工作环境，不存在表[" + tempParam.getTableName() + "],列[" + tempParam.getColumnName() + "]");
        }
        tempParam.setColumnType(dbColumn.getColumnType());
        tempParam.setColumnComment(dbColumn.getColumnComment());
    }

}
