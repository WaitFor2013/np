package com.np.design.view;

import com.np.database.*;
import lombok.*;

import javax.persistence.Column;

//code generate 2020年04月13日 13时32分00秒
//设备流程数趋势分析
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ExtView(viewName = "view_eq_process_count", viewComment = "设备流程数趋势分析")
public class ViewEqProcessCount implements NoRepeatView {

    static {
        NpViewDefinition.registry("view_eq_process_count", ViewEqProcessCount.class);
        NpViewDefinition.registrySQL("view_eq_process_count", "select count(a.sys_id) as num, to_char(a.gmt_create, 'YY-MM-DD') as gmt_create	from wk_process a	@WHERE 	group by to_char(a.gmt_create, 'YY-MM-DD')");

        NpViewDefinition.registryQueryParam("view_eq_process_count", "a", "wk_process", "gmt_create", "时间:DATE");
        NpViewDefinition.registryQueryParam("view_eq_process_count", "a", "wk_process", "tenant_id", "字符串:STR(64)");
        NpViewDefinition.registryQueryParam("view_eq_process_count", "a", "wk_process", "flow_engine", "字符串:STR(64)");
        NpViewDefinition.registryQueryParam("view_eq_process_count", "a", "wk_process", "process_def_id", "字符串:STR(256)");
    }

    public static final ColumnDefinition wk_process$gmt_create = ColumnDefinition.name("wk_process$gmt_create");
    public static final ColumnDefinition wk_process$tenant_id = ColumnDefinition.name("wk_process$tenant_id");
    public static final ColumnDefinition wk_process$flow_engine = ColumnDefinition.name("wk_process$flow_engine");
    public static final ColumnDefinition wk_process$process_def_id = ColumnDefinition.name("wk_process$process_def_id");

    //数量
    @Column(name = "num")
    @ExtParam(tablePrefix = "", tableName = "", columnName = "num", columnType = "整型:INT", columnComment = "数量")
    private Integer num;

    //日期
    @Column(name = "gmt_create")
    @ExtParam(tablePrefix = "", tableName = "", columnName = "gmt_create", columnType = "字符串:STR(64)", columnComment = "日期")
    private String gmtCreate;


}