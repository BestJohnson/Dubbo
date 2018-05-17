package com.kaishengit.entity;

import java.io.Serializable;

/**
 * @author 
 */
public class PermissionRolesKey implements Serializable {
    private Integer permissionId;

    private Integer rolesId;

    private static final long serialVersionUID = 1L;

    public Integer getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Integer permissionId) {
        this.permissionId = permissionId;
    }

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }
}