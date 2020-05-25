package com.np.design;

import java.awt.*;
import java.io.File;


public class NpConstants {

    /**
     * 软件名称,版本
     */
    public final static String APP_NAME = "NP";
    public final static String APP_TITLE= "牛皮";
    public final static String APP_VERSION = "v_1.0.0_200205";

    /**
     * 帮助图标
     */
    public final static Image HELP_ICON = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/helpButton.png"));

    /**
     * 帮助图标-focused
     */
    public final static Image HELP_FOCUSED_ICON = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/helpButtonFocused.png"));


    private final static String osName = System.getProperty("os.name");

    public static boolean isMacOs() {
        return osName.contains("Mac");
    }

    public static String configHome = System.getProperty("user.home") + File.separator + ".np"
            + File.separator;


    /**
     * Logo-1024*1024
     */
    public static final Image IMAGE_LOGO_1024 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-1024.png"));

    /**
     * Logo-512*512
     */
    public static final Image IMAGE_LOGO_512 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-512.png"));

    /**
     * Logo-256*256
     */
    public static final Image IMAGE_LOGO_256 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-256.png"));

    /**
     * Logo-128*128
     */
    public static final Image IMAGE_LOGO_128 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-128.png"));

    /**
     * Logo-64*64
     */
    public static final Image IMAGE_LOGO_64 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-64.png"));

    /**
     * Logo-48*48
     */
    public static final Image IMAGE_LOGO_48 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-48.png"));

    /**
     * Logo-32*32
     */
    public static final Image IMAGE_LOGO_32 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-32.png"));

    /**
     * Logo-24*24
     */
    public static final Image IMAGE_LOGO_24 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-24.png"));

    /**
     * Logo-16*16
     */
    public static final Image IMAGE_LOGO_16 = Toolkit.getDefaultToolkit()
            .getImage(NpConstants.class.getResource("/icon/logo-16.png"));

    public static final Image[] logoImages = new Image[]{
            IMAGE_LOGO_1024,
            IMAGE_LOGO_512,
            IMAGE_LOGO_256,
            IMAGE_LOGO_128,
            IMAGE_LOGO_64,
            IMAGE_LOGO_48,
            IMAGE_LOGO_32,
            IMAGE_LOGO_24,
            IMAGE_LOGO_16
    };
}
