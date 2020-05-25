package com.np.design;

import com.np.design.ui.frame.Init;
import com.np.design.ui.frame.MainFrame;
import io.netty.util.concurrent.UnorderedThreadPoolEventExecutor;
import lombok.extern.slf4j.Slf4j;


//https://github.com/JetBrains/intellij-community
@Slf4j
public class NoRepeatApp {

    public static final MainFrame mainFrame = new MainFrame();
    public static final UnorderedThreadPoolEventExecutor executor = new UnorderedThreadPoolEventExecutor(4);

    public static void main(String[] args) {

        //初始化基础样式
        Init.initTheme();
        Init.initGlobalFont();


        //容器初始化
        mainFrame.init();


    }

}

