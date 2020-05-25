package com.np.database;

import com.np.database.exception.NpDbException;

import java.util.Date;

//标示
public interface NoRepeatPO {

    default String getSysId() {
        throw new NpDbException("UnSupported");
    }

    default void setSysId(String sysId) {
        throw new NpDbException("UnSupported");
    }

    default String getTenantId() {
        throw new NpDbException("UnSupported");
    }

    default void setTenantId(String tenantId) {
        throw new NpDbException("UnSupported");
    }

    default Date getGmtCreate() {
        throw new NpDbException("UnSupported");
    }

    default void setGmtCreate(Date gmtCreate) {
        throw new NpDbException("UnSupported");
    }

    default Date getGmtModified() {
        throw new NpDbException("UnSupported");
    }

    default void setGmtModified(Date gmtModified) {
        throw new NpDbException("UnSupported");
    }

    default String getCreateUserId() {
        throw new NpDbException("UnSupported");
    }

    default void setCreateUserId(String createUserId) {
        throw new NpDbException("UnSupported");
    }

    default String getModifyUserId() {
        throw new NpDbException("UnSupported");
    }

    default void setModifyUserId(String modifyUserId) {
        throw new NpDbException("UnSupported");
    }

    default String getExtFields() {
        throw new NpDbException("UnSupported");
    }

    default void setExtFields(String extFields) {
        throw new NpDbException("UnSupported");
    }
}
