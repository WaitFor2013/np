package com.np.design.ui.form;

import com.np.design.NoRepeatApp;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;


@Getter
@Slf4j
public class MainWindow {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel aboutPanel;
    private JPanel settingPanel;
    private JPanel domainPanel;
    private JPanel queryPanel;
    private JLabel connectInfo;
    private JLabel connectLabel;

    private static MainWindow mainWindow;

    private MainWindow() {

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                /*int index = tabbedPane.getSelectedIndex();
                if (index == 1 || index == 2) {
                    String text = connectInfo.getText();
                    if (null == text || text.trim().isEmpty() || "无".equals(text.trim())) {
                        JOptionPane.showMessageDialog(NoRepeatApp.mainFrame, "先配置【工作环境】", "错误",
                                JOptionPane.ERROR_MESSAGE);
                        tabbedPane.setSelectedIndex(0);
                    }
                }*/
            }
        });
    }

    public static MainWindow getInstance() {
        if (mainWindow == null) {
            mainWindow = new MainWindow();
        }
        return mainWindow;
    }

    private static GridConstraints gridConstraints = new GridConstraints(0, 0, 1, 1,
            GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW,
            GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(200, 200),
            null, 0, false);

    public void init() {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            //do nothing
        }

        mainWindow = getInstance();

        //初始化设置
        mainWindow.getSettingPanel().add(SettingForm.getInstance().getSettingPanel(), gridConstraints);
        //领域设计
        mainWindow.getDomainPanel().add(DomainForm.getInstance().getDomainPanel(), gridConstraints);
        //查询设计
        mainWindow.getQueryPanel().add(QueryForm.getInstance().getQueryPanel(), gridConstraints);
        //关于
        mainWindow.getAboutPanel().add(AboutForm.getInstance().getAboutPanel(), gridConstraints);


        mainWindow.getMainPanel().updateUI();

        initAllTab();

        addListeners();


    }

    /**
     * 初始化所有tab
     */
    public static void initAllTab() {
        NoRepeatApp.executor.execute(AboutForm::init);
    }

    /**
     * 添加事件监听
     */
    public static void addListeners() {
        //NoRepeatApp.executor.execute(AboutListener::addListeners);
    }


}
