package com.kaishengit.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.kaishengit.entity.*;
import com.kaishengit.exception.ServiceException;
import com.kaishengit.mapper.PermissionMapper;
import com.kaishengit.mapper.PermissionRolesMapper;
import com.kaishengit.mapper.RolesMapper;
import com.kaishengit.service.RolePermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;

/**
 * 角色和权限的业务类接口的实现类
 */
@Service
public class RolePermissionServiceImpl implements RolePermissionService {

    /**
     * 增删改一定要添加日志
     */
    public static final Logger logger = LoggerFactory.getLogger(RolePermissionServiceImpl.class);

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private PermissionRolesMapper permissionRolesMapper;


    /**
     * 查找所有权限
     *
     * @return 权限对象集合
     */
    public List<Permission> findAllPermission() {
        PermissionExample permissionExample = new PermissionExample();
        return permissionMapper.selectByExample(permissionExample);
        /*List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        List<Permission> resultList = new ArrayList<>();

        treeList(permissionList,resultList,0);
        return resultList;*/

    }

    /**
     * 将查询的权限列表转换为树形集合结果
     * @param permissionList
     * @param resultList
     */
    private void treeList(List<Permission> permissionList, List<Permission> resultList, Integer parentId) {

        List<Permission> tempList = Lists.newArrayList(Collections2.filter(permissionList, new Predicate<Permission>() {
            @Override
            public boolean apply(@Nullable Permission permission) {
                return permission.getId().equals(parentId);
            }
        }));
        for(Permission permission : tempList) {
            resultList.add(permission);
            treeList(permissionList,resultList,permission.getId());
        }
    }

    /**
     * 根据权限类型查找权限列表
     *
     * @param permissionType
     * @return
     */
    public List<Permission> findPermissionByType(String permissionType) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(permissionType);
        return permissionMapper.selectByExample(permissionExample);
    }

    /**
     * 新增权限
     *
     * @param permission
     */
    public void addPermission(Permission permission) {
        permission.setCreatTime(new Date());
        permissionMapper.insertSelective(permission);
        logger.info("新增权限 {}",permission);
    }

    /**
     * 新增角色
     *
     * @param roles        角色对象
     * @param permissionId 角色对应的权限ID
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void addRoles(Roles roles, Integer[] permissionId) {
        roles.setCreatTime(new Date());
        rolesMapper.insertSelective(roles);

        if(permissionId != null) {
            //保存角色和权限的关系
            for (Integer id : permissionId) {
                PermissionRolesKey permissionRolesKey = new PermissionRolesKey();
                permissionRolesKey.setPermissionId(id);
                permissionRolesKey.setRolesId(roles.getId());

                permissionRolesMapper.insert(permissionRolesKey);
            }
        }
        logger.info("新增角色 {}",roles);

    }

    /**
     * 查找所有角色及其所拥有的权限列表
     *
     * @return
     */
    @Override
    public List<Roles> findAllRolesWithPermission() {
        return rolesMapper.findAllWithPermission();
    }

    /**
     * 查找所有角色
     *
     * @return
     */
    @Override
    public List<Roles> findAllRoles() {
        RolesExample rolesExample = new RolesExample();
        return rolesMapper.selectByExample(rolesExample);
    }

    /**
     * 根据权限ID删除权限
     *
     * @param id 权限ID
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void delPermissionById(Integer id) {
        //需要判断当前权限是否有子权限
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andParentIdEqualTo(id);

        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        if(permissionList != null && !permissionList.isEmpty()) {
            throw new ServiceException("该权限包含子权限，请先删除子权限");
        }

        //需要查询该权限是否已被角色使用
        PermissionRolesExample permissionRolesExample = new PermissionRolesExample();
        permissionRolesExample.createCriteria().andPermissionIdEqualTo(id);

        List<PermissionRolesKey> permissionRolesKeys = permissionRolesMapper.selectByExample(permissionRolesExample);
        if(permissionRolesKeys != null && !permissionRolesKeys.isEmpty()) {
            throw new ServiceException("该权限已被角色使用，无法删除");
        }

        permissionMapper.deleteByPrimaryKey(id);

        logger.info("删除权限 {}",permissionMapper.selectByPrimaryKey(id));
    }

    /**
     * 根据账号ID查找对应的角色
     *
     * @param id
     * @return
     */
    @Override
    public List<Roles> findAllRolesByAccountId(Integer id) {
        return rolesMapper.findAllByAccountId(id);
    }

    /**
     * 根据角色ID查找对应的权限
     *
     * @param id
     * @return
     */
    @Override
    public List<Permission> findAllPermissionByRolesId(Integer id) {
        return permissionMapper.findAllByRolesId(id);
    }

    /**
     * 根据ID查找对应的权限
     *
     * @param id
     * @return
     */
    @Override
    public Permission findPermissionById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改权限
     *
     * @param permission
     */
    @Override
    public void updatePermission(Permission permission) {
        permission.setUpdateTime(new Date());
        permissionMapper.updateByPrimaryKeySelective(permission);
    }


}
