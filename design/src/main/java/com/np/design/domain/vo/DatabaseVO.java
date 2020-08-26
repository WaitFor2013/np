package com.np.design.domain.vo;

import com.np.design.exception.NpException;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;


@Getter
@Setter
public class DatabaseVO {

    //连接名
    private String connectName;

    //IP
    private String dbHost;

    //端口
    private String dbPort;

    //数据库
    private String dbName;

    //用户信息
    private String dbUser;

    //密码
    private String dbPassword;

    //连接测试结果
    private String dbType;

    private DatabaseVO() {

    }

    public static DatabaseVO getInstance(JComboBox dbTypeText,
                                         JTextField connectField,
                                         JTextField dbhostField,
                                         JTextField dbportField,
                                         JTextField dbnameField,
                                         JTextField dbuserField,
                                         JPasswordField dbpasswordField) {

        Object selectItem = dbTypeText.getSelectedItem();
        if(null == selectItem){
            throw new NpException("请选择数据库类型");
        }

        DatabaseVO databaseVO = new DatabaseVO();
        databaseVO.connectName = connectField.getText();
        databaseVO.dbHost = dbhostField.getText();
        databaseVO.dbPort = dbportField.getText();
        databaseVO.dbName = dbnameField.getText();
        databaseVO.dbUser = dbuserField.getText();
        databaseVO.dbPassword = String.valueOf(dbpasswordField.getPassword());
        databaseVO.dbType = selectItem.toString();

        check(databaseVO);

        return databaseVO;
    }

    private static void check(DatabaseVO databaseVO) {

        if (StringUtils.isEmpty(databaseVO.connectName)) {
            throw new NpException("【连接名】信息填写错误");
        }

        if (StringUtils.isEmpty(databaseVO.dbHost)) {
            throw new NpException("【主机】信息填写错误");
        }

        if (StringUtils.isEmpty(databaseVO.dbPort)) {
            throw new NpException("【端口】信息填写错误");
        }

        if (StringUtils.isEmpty(databaseVO.dbName)) {
            throw new NpException("【数据库名】信息填写错误");
        }

        if (StringUtils.isEmpty(databaseVO.dbUser)) {
            throw new NpException("【用户】信息填写错误");
        }

        if (StringUtils.isEmpty(databaseVO.dbPassword)) {
            throw new NpException("【密码】信息填写错误");
        }

    }

    @Override
    public String toString() {
        return String.format("[%s][%s:%s](%s)", this.getConnectName(), this.getDbHost(), this.getDbPort(), this.getDbName());
    }
}
