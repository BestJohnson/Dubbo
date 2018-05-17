package com.kaishengit.service;

import com.kaishengit.entity.Permission;
import com.kaishengit.entity.Roles;

import java.util.List;

/**
 * 角色权限的业务类
 */
public interface RolePermissionService {
    /**
     * 查找所有权限
     * @return 权限对象集合
     */
    List<Permission> findAllPermission();

    /**
     * 根据权限类型查找权限列表
     * @param permissionType
     * @return
     */
    List<Permission> findPermissionByType(String permissionType);

    /**
     * 新增权限
     * @param permission
     */
    void addPermission(Permission permission);


    /**
     * 新增角色
     * @param roles 角色对象
     * @param permissionId 角色对应的权限ID
     */
    void addRoles(Roles roles, Integer[] permissionId);

    /**
     * 查找所有角色及其所拥有的权限列表
     * @return
     */
    List<Roles> findAllRolesWithPermission();

    /**
     * 查找所有角色
     * @return
     */
    List<Roles> findAllRoles();

    /**
     * 删除权限
     * @param id 权限ID
     */
    void delPermissionById(Integer id);

    /**
     * 根据账号ID查找对应的角色
     * @param id
     * @return
     */
    List<Roles> findAllRolesByAccountId(Integer id);

    /**
     * 根据角色ID查找对应的权限
     * @param id
     * @return
     */
    List<Permission> findAllPermissionByRolesId(Integer id);

    /**
     * 根据ID查找对应的权限
     * @param id
     * @return
     */
    Permission findPermissionById(Integer id);

    /**
     * 修改权限
     * @param permission
     */
    void updatePermission(Permission permission);
}
