package com.np.design.domain.misc;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class NpConfig {


    private Integer fontSize;

    private String font;

    private ThemeEnum theme;

    public final static NpConfig config = new NpConfig();

    private NpConfig() {

        this.theme = ThemeEnum.DARCULA;


        //默认读取配置文件

    }
}
