package com.kaishengit.service;

import com.kaishengit.entity.Account;
import java.util.List;
import java.util.Map;

/**
 * 系统账号的业务类
 */
public interface AccountService {

    /**
     * 查询所有账号及对应的角色
     * @return 账号集合
     */
    List<Account> findAllAccountWithRoles();

    /**
     * 新增账户
     * @param account 要新增的账户对象
     * @param rolesIds 对应的角色ID数组
     */
    void addAccount(Account account, Integer[] rolesIds);

    /**
     * 根据URL传过来的的参数查询所有账号及对应的角色
     * @param requestParam
     * @return
     */
    List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam);

    /**
     * 根据电话号码查找Account对象
     * @param userMobile
     * @return
     */
    Account findByMobile(String userMobile);


}
