package com.np.design.domain;

import com.np.database.*;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.List;
import java.util.Date;

//code generate 2020年03月06日 14时12分16秒
//租户 STE
@Getter
@Setter
@Table(name = "sys_tenant")
@ExtTable(comment = "租户",abbr = "STE",export = "2000")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class SysTenantPO implements NoRepeatPO {

    static {
        NpDefinition.registry("sys_tenant", SysTenantPO.class);
    }

    public static final TableDefinition NP_NAME = TableDefinition.table("sys_tenant","STE");

    //租户名称
    public static final ColumnDefinition $name = ColumnDefinition.tableAndColumn("sys_tenant","name");
    @Column(name = "name")
    @ExtColumn(comment = "租户名称",type = "字符串:STR(256)",isAllOnly = true,isNull = false)
    private String name;

    //系统状态
    public static final ColumnDefinition $sysStatus = ColumnDefinition.tableAndColumn("sys_tenant","sys_status");
    @Column(name = "sys_status")
    @ExtColumn(comment = "系统状态",type = "整型:INT",isNull = false)
    private Integer sysStatus;

    //超级账户
    public static final ColumnDefinition $superUserId = ColumnDefinition.tableAndColumn("sys_tenant","super_user_id");
    @Column(name = "super_user_id")
    @ExtColumn(comment = "超级账户",type = "字符串:STR(64)",relation = "sys_user",isTenantOnly = true,isNull = false)
    private String superUserId;

    //企业类型(化工/医药)
    public static final ColumnDefinition $bussinessType = ColumnDefinition.tableAndColumn("sys_tenant","bussiness_type");
    @Column(name = "bussiness_type")
    @ExtColumn(comment = "企业类型(化工/医药)",type = "字符串数组:ARRAY_STR")
    private List<String> bussinessType;

    //企业属性(派单/包干)
    public static final ColumnDefinition $workManner = ColumnDefinition.tableAndColumn("sys_tenant","work_manner");
    @Column(name = "work_manner")
    @ExtColumn(comment = "企业属性(派单/包干)",type = "字符串数组:ARRAY_STR")
    private List<String> workManner;

    //系统业务(设备/安环)
    public static final ColumnDefinition $systemOperation = ColumnDefinition.tableAndColumn("sys_tenant","system_operation");
    @Column(name = "system_operation")
    @ExtColumn(comment = "系统业务(设备/安环)",type = "字符串数组:ARRAY_STR")
    private List<String> systemOperation;

    //企业联系方式
    public static final ColumnDefinition $enterpriseTel = ColumnDefinition.tableAndColumn("sys_tenant","enterprise_tel");
    @Column(name = "enterprise_tel")
    @ExtColumn(comment = "企业联系方式",type = "字符串:STR(64)")
    private String enterpriseTel;

    //企业联系地址
    public static final ColumnDefinition $enterpriseAddress = ColumnDefinition.tableAndColumn("sys_tenant","enterprise_address");
    @Column(name = "enterprise_address")
    @ExtColumn(comment = "企业联系地址",type = "字符串:STR(1024)")
    private String enterpriseAddress;

    //设备数量
    public static final ColumnDefinition $equipmentAmount = ColumnDefinition.tableAndColumn("sys_tenant","equipment_amount");
    @Column(name = "equipment_amount")
    @ExtColumn(comment = "设备数量",type = "整型:INT")
    private Integer equipmentAmount;

    //有效日期
    public static final ColumnDefinition $effectiveDate = ColumnDefinition.tableAndColumn("sys_tenant","effective_date");
    @Column(name = "effective_date")
    @ExtColumn(comment = "有效日期",type = "时间:DATE")
    private Date effectiveDate;

    //[*]租户ID
    public static final ColumnDefinition $tenantId = ColumnDefinition.tableAndColumn("sys_tenant","tenant_id");
    @Column(name = "tenant_id")
    @ExtColumn(comment = "[*]租户ID",type = "字符串:STR(64)",relation = "sys_tenant",isTenantOnly = true,isNull = false,isAuth = true)
    private String tenantId;

    //[*]系统ID
    public static final ColumnDefinition $sysId = ColumnDefinition.tableAndColumn("sys_tenant","sys_id");
    @Id
    @Column(name = "sys_id")
    @ExtColumn(comment = "[*]系统ID",type = "字符串:STR(64)",isAllOnly = true,isNull = false)
    private String sysId;

    //[*]创建时间
    public static final ColumnDefinition $gmtCreate = ColumnDefinition.tableAndColumn("sys_tenant","gmt_create");
    @Column(name = "gmt_create")
    @ExtColumn(comment = "[*]创建时间",type = "时间:DATE",isNull = false)
    private Date gmtCreate;

    //[*]修改时间
    public static final ColumnDefinition $gmtModified = ColumnDefinition.tableAndColumn("sys_tenant","gmt_modified");
    @Column(name = "gmt_modified")
    @ExtColumn(comment = "[*]修改时间",type = "时间:DATE",isNull = false)
    private Date gmtModified;

    //[*]创建用户
    public static final ColumnDefinition $createUserId = ColumnDefinition.tableAndColumn("sys_tenant","create_user_id");
    @Column(name = "create_user_id")
    @ExtColumn(comment = "[*]创建用户",type = "字符串:STR(64)",relation = "sys_user",isNull = false)
    private String createUserId;

    //[*]修改用户
    public static final ColumnDefinition $modifyUserId = ColumnDefinition.tableAndColumn("sys_tenant","modify_user_id");
    @Column(name = "modify_user_id")
    @ExtColumn(comment = "[*]修改用户",type = "字符串:STR(64)",relation = "sys_user",isNull = false)
    private String modifyUserId;

    //[*]动态扩展
    public static final ColumnDefinition $extFields = ColumnDefinition.tableAndColumn("sys_tenant","ext_fields");
    @Column(name = "ext_fields")
    @ExtColumn(comment = "[*]动态扩展",type = "JSON",isNull = false)
    private String extFields;

    //code helper
    //SysTenantPO.builder().name().sysStatus().superUserId().bussinessType().workManner().systemOperation().enterpriseTel().enterpriseAddress().equipmentAmount().effectiveDate();
    //.tenantId().sysId().gmtCreate().gmtModified().createUserId().modifyUserId().extFields();
}