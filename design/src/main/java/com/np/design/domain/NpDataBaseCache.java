package com.np.design.domain;

import com.alibaba.fastjson.JSON;
import com.np.database.orm.NpDatasource;
import com.np.database.orm.NpDbConfig;
import com.np.database.orm.biz.DbTypeEnum;
import com.np.database.orm.session.SqlSession;
import com.np.design.NoRepeatApp;
import com.np.design.NpConstants;
import com.np.design.domain.vo.DatabaseVO;
import com.np.design.exception.NpException;
import com.np.design.ui.form.MainWindow;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class NpDataBaseCache {

    private final static List<DatabaseVO> databaseCache = new CopyOnWriteArrayList<>();
    private final static String DATABASES_STORE = NpConstants.configHome + "databases.store";

    private static NpDatasource workDatabase;
    public static String connectName;

    public static Object[][] getStoreDatabaseModel() {

        try {
            String dbJson = FileUtils.readFileToString(new File(DATABASES_STORE));
            List<DatabaseVO> databaseVOS = JSON.parseArray(dbJson, DatabaseVO.class);
            if (null != databaseVOS && !databaseVOS.isEmpty()) {
                databaseVOS.forEach(databaseVO -> {

                    //向前兼容处理
                    if("PostgreSQL".equals(databaseVO.getDbType())){
                        databaseVO.setDbType(DbTypeEnum.POSTGRESQL.name());
                    }

                    databaseCache.add(databaseVO);
                });
            }
        } catch (Exception e) {
            log.error("read db store failed.", e);
            //do nothing
        }

        return getDatabaseModel(false);
    }

    public static NpDatasource getDatasource() {
        if (null == workDatabase) {
            throw new NpException("工作环境未设置，请先设置工作环境");
        }
        return workDatabase;
    }

    public static synchronized void setWorkDatabase(int i) {
        DatabaseVO databaseVO = databaseCache.get(i);

        try {
            DbTypeEnum dbTypeEnum = DbTypeEnum.getFromString(databaseVO.getDbType());

            NpDbConfig dbConfig = NpDbConfig.builder()
                    .dbTypeEnum(dbTypeEnum)
                    .ip(databaseVO.getDbHost())
                    .port(Integer.parseInt(databaseVO.getDbPort()))
                    .dbName(databaseVO.getDbName())
                    .user(databaseVO.getDbUser())
                    .password(databaseVO.getDbPassword())
                    .log(false)
                    .build();

            workDatabase = new NpDatasource(dbConfig);
            connectName = databaseVO.getConnectName();

            SqlSession session = workDatabase.getSession(true);
            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "连接成功.", "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            session.close();

            MainWindow.getInstance().getConnectInfo().setText(databaseVO.toString());
        } catch (Exception e) {
            log.error("connect failed.", e);
            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, e.getMessage(), "错误",
                    JOptionPane.ERROR_MESSAGE);
        } finally {

        }

    }

    public static Object[][] addDatabase(DatabaseVO databaseVO) {

        for (DatabaseVO in : databaseCache) {
            if (in.getConnectName().equals(databaseVO.getConnectName())) {
                throw new NpException("【连接名】信息重复");
            }
        }
        databaseCache.add(databaseVO);

        return getDatabaseModel(true);
    }

    public static Object[][] removeDatabases(Component parentComponent, int[] rows) {

        for (int i : rows) {
            DatabaseVO databaseVO = databaseCache.get(i);
            int result = JOptionPane.showConfirmDialog(parentComponent,
                    "删除连接信息:" + databaseVO.getConnectName(),
                    "请确认",
                    JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION) {
                databaseCache.remove(i);
            }
        }

        return getDatabaseModel(true);
    }

    private static Object[][] getDatabaseModel(boolean save) {
        if (save) {
            save();
        }
        Object[][] result = new Object[databaseCache.size()][];
        for (int i = 0; i < databaseCache.size(); i++) {
            DatabaseVO instance = databaseCache.get(i);
            result[i] = new Object[]{instance.getConnectName(), instance.getDbHost(), instance.getDbPort(), instance.getDbName(), instance.getDbUser(), instance.getDbType()};
        }
        return result;
    }

    private static void save() {
        File file = new File(DATABASES_STORE);
        log.info("数据库信息存储于:{}", file.getAbsolutePath());
        try {
            FileUtils.write(file, JSON.toJSONString(databaseCache), false);
        } catch (IOException e) {
            log.error("save failed.", e);
            JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, e.getMessage(), "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}
