package com.kaishengit.shiro;

import com.kaishengit.entity.Account;
import com.kaishengit.entity.AccountLoginLog;
import com.kaishengit.entity.Permission;
import com.kaishengit.entity.Roles;
import com.kaishengit.service.AccountService;
import com.kaishengit.service.RolePermissionService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private AccountService accountService;

    @Autowired
    private RolePermissionService rolePermissionService;

    /**
     * 判断角色和权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //获取当前登录对象
        Account account = (Account) principalCollection.getPrimaryPrincipal();
        //获取对象拥有的角色
        List<Roles> rolesList = rolePermissionService.findAllRolesByAccountId(account.getId());

        //获取角色对应的的权限
        List<Permission> permissionList = new ArrayList<>();
        for(Roles roles : rolesList) {
            permissionList = rolePermissionService.findAllPermissionByRolesId(roles.getId());
        }

        Set<String> rolesName = new HashSet<>();
        for(Roles roles : rolesList) {
            rolesName.add(roles.getRolesCode());
        }

        Set<String> permissionName = new HashSet<>();
        for(Permission permission : permissionList) {
            permissionName.add(permission.getPermissionCode());
        }

        //当前账号拥有的角色（code）
        simpleAuthorizationInfo.setRoles(rolesName);
        //当前账号拥有的权限（code）
        simpleAuthorizationInfo.setStringPermissions(permissionName);


        return simpleAuthorizationInfo;
    }


    /**
     * 判断登录
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String userMobile = usernamePasswordToken.getUsername();

        if(userMobile != null) {
            Account account = accountService.findByMobile(userMobile);
            if(account == null) {
                throw new UnknownAccountException("该账号不存在");
            } else {
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    //日志
                    logger.info("{} 登录成功：{}",account,usernamePasswordToken.getHost());

                    AccountLoginLog accountLoginLog = new AccountLoginLog();
                    accountLoginLog.setLoginTime(new Date());
                    accountLoginLog.setLoginIp(usernamePasswordToken.getHost());
                    accountLoginLog.setAccountId(account.getId());

                    return new SimpleAuthenticationInfo(account,account.getAccountPassword(),getName());
                } else {
                    throw new LockedAccountException("账号异常" + account.getAccountState());
                }
            }
        }

        return null;
    }
}
