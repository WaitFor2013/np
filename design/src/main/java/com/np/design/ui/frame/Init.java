package com.np.design.ui.frame;

import com.np.design.domain.misc.NpConfig;
import com.np.design.NpConstants;
import com.np.design.NpConstants;
import lombok.extern.slf4j.Slf4j;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;


@Slf4j
public class Init {

    /**
     * 设置全局字体
     */
    public static void initGlobalFont() {
        if (null == NpConfig.config.getFontSize()) {
            // 根据DPI调整字号
            // 得到屏幕的分辨率dpi
            // dell 1920*1080/24寸=96
            // 小米air 1920*1080/13.3寸=144
            // 小米air 1366*768/13.3寸=96
            int fontSize = 15;

            // Mac等高分辨率屏幕字号初始化
            if (NpConstants.isMacOs()) {
                fontSize = 15;
            } else {
                fontSize = (int) (getScreenScale() * fontSize);
            }
            NpConfig.config.setFontSize(fontSize);
            log.info("设置字体：{}", fontSize);
        }

        Font font = new Font(NpConfig.config.getFont(), Font.PLAIN, NpConfig.config.getFontSize());
        FontUIResource fontRes = new FontUIResource(font);
        for (Enumeration<Object> keys = UIManager.getDefaults().keys(); keys.hasMoreElements(); ) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }


    }

    /**
     * 获取屏幕规格
     * author by darcula@com.bulenkov
     * see https://github.com/bulenkov/Darcula
     *
     * @return
     */
    public static float getScreenScale() {
        int dpi = 96;

        try {
            dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        } catch (HeadlessException var2) {
        }

        float scale = 1.0F;
        if (dpi < 120) {
            scale = 1.0F;
        } else if (dpi < 144) {
            scale = 1.25F;
        } else if (dpi < 168) {
            scale = 1.5F;
        } else if (dpi < 192) {
            scale = 1.75F;
        } else {
            scale = 2.0F;
        }

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        log.info("screen dpi:{},width:{},height:{}", dpi, screenSize.getWidth(), screenSize.getHeight());

        return scale;
    }


    /**
     * 初始化look and feel
     */
    public static void initTheme() {

        try {
            switch (NpConfig.config.getTheme()) {
                case BEAUTYEYE:
                    BeautyEyeLNFHelper.launchBeautyEyeLNF();
                    UIManager.put("RootPane.setupButtonVisible", false);
                    break;
                case SYSTEM:
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    break;
                case DARCULA:
                default:
                    UIManager.setLookAndFeel("com.bulenkov.darcula.DarculaLaf");
            }
        } catch (Exception e) {
            log.error("初始化错误", e);
        }
    }


}
