package com.np.design.ui.frame;

import com.apple.eawt.Application;
import com.np.design.NoRepeatApp;
import com.np.design.NpConstants;
import com.np.design.ui.form.LoadingForm;
import com.np.design.ui.form.MainWindow;
import com.np.design.NoRepeatApp;
import com.np.design.NpConstants;
import com.np.design.ui.form.LoadingForm;
import com.np.design.ui.form.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class MainFrame extends JFrame {

    public void init() {

        this.setName(NpConstants.APP_NAME);
        this.setTitle(NpConstants.APP_TITLE);
        this.setIconImages(Arrays.asList(NpConstants.logoImages));
        // Mac系统Dock图标
        if (NpConstants.isMacOs()) {
            Application application = Application.getApplication();
            application.setDockIconImage(NpConstants.IMAGE_LOGO_1024);
            //application.setEnabledAboutMenu(false);
            //application.setEnabledPreferencesMenu(false);
        }

        // 设置窗口大小
        setPreferSizeAndLocateToCenter(this, 1, 1);

        //容器初始化
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (screenSize.getWidth() <= 1366) {
            // 低分辨率下自动最大化窗口
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
        this.pack();
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel loadingPanel = LoadingForm.getInstance().getLoadPanel();
        this.add(loadingPanel);

        //设置主窗口
        MainWindow.getInstance().init();
        this.setContentPane(MainWindow.getInstance().getMainPanel());


        this.remove(loadingPanel);

    }


    /**
     * 设置组件preferSize并定位于屏幕中央(基于屏幕宽高的百分百)
     */
    public static void setPreferSizeAndLocateToCenter(Component component, double preferWidthPercent, double preferHeightPercent) {

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(NoRepeatApp.mainFrame.getGraphicsConfiguration());
        int screenWidth = screenSize.width - screenInsets.left - screenInsets.right;
        int screenHeight = screenSize.height - screenInsets.top - screenInsets.bottom;


        int preferWidth = (int) (screenWidth * preferWidthPercent);
        int preferHeight = (int) (screenHeight * preferHeightPercent);


        component.setBounds((screenWidth - preferWidth) / 2, (screenHeight - preferHeight) / 2,
                preferWidth, preferHeight);
        Dimension preferSize = new Dimension(preferWidth, preferHeight);
        component.setPreferredSize(preferSize);

    }
}
