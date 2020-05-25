package com.np.design.domain;

import java.util.ArrayList;
import java.util.List;

public enum NpModule {

    SYS("系统(sys)", "sys"),
    REAL("实时(real)", "real"),
    DY("动态程序(dy)", "dy"),
    WORK("工作台(wk)", "wk"),

    PE("人(pe)", "pe"),
    DV("机(eq)", "eq"),
    SE("料(mat)", "mat"),
    LAW("法(law)", "law"),
    CON("环(con)", "con"),
    //WMS("仓储(wms)", "wms"),
    //PMS("生产(pms)", "pms"),
    //QA("质量(qa)", "qa")
    ;

    private String cn;
    private String en;

    NpModule(String cn, String en) {
        this.cn = cn;
        this.en = en;
    }

    public static String[] getStringArray() {

        NpModule[] values = NpModule.values();

        String[] result = new String[values.length];

        for (int i = 0; i < values.length; i++) {
            NpModule npModule = values[i];
            result[i] = npModule.cn;
        }

        return result;

    }

    public static List<String> getListModules() {
        List<String> list = new ArrayList<String>();
        NpModule[] values = NpModule.values();
        list.add(ALL);

        for (int i = 0; i < values.length; i++) {
            NpModule npModule = values[i];

            list.add(npModule.cn);
        }
        return list;
    }

    public static String getModulePrefix(String module) {
        return module.substring(module.indexOf("(") + 1, module.indexOf(")"));
    }

    public String getCn() {
        return cn;
    }

    public String getEn() {
        return en;
    }

    public static final String ALL = "ALL";
}
