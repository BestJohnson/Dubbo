package com.kaishengit.service.impl;

import com.kaishengit.tms.entity.Account;
import com.kaishengit.tms.entity.AccountExample;
import com.kaishengit.tms.entity.AccountLoginLog;
import com.kaishengit.tms.entity.AccountRolesKey;
import com.kaishengit.tms.exception.ServiceException;
import com.kaishengit.tms.mapper.AccountLoginLogMapper;
import com.kaishengit.tms.mapper.AccountMapper;
import com.kaishengit.tms.mapper.AccountRolesMapper;
import com.kaishengit.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统账号的业务类
 */

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountRolesMapper accountRolesMapper;

    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;

    /**
     * 系统登录
     * @param accountMobile 账户手机号
     * @param password 密码
     * @param requestIp 登录IP
     * @return accounts对象
     */
    /*public Account login(String accountMobile, String password, String requestIp) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(accountMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        Account account = null;
        if(accountList != null && !accountList.isEmpty()) {
            account = accountList.get(0);
            //再匹配密码
            if(account.getAccountPassword().equals(DigestUtils.md5Hex(password))) {
                //再判断状态
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    //再记录日志
                    AccountLoginLog accountLoginLog = new AccountLoginLog();
                    accountLoginLog.setLoginIp(requestIp);
                    accountLoginLog.setLoginTime(new Date());
                    accountLoginLog.setAccountId(account.getId());

                    accountLoginLogMapper.insertSelective(accountLoginLog);

                    logger.info("{} 登录了系统",account);
                    return account;
                } else if(Account.STATE_LOCKED.equals(account.getAccountState())) {
                    throw new ServiceException("账号被锁定，请联系管理员");
                } else {
                    throw new ServiceException("账号被禁用，请联系管理员");
                }
            } else {
                throw new ServiceException("账户或密码错误");
            }
        } else {
            throw new ServiceException("账户或密码错误");
        }
    }*/

    /**
     * 查询所有账号及对应的角色
     *
     * @return 账号集合
     */
    @Override
    public List<Account> findAllAccountWithRoles() {
        return accountMapper.findAllWithRoles();
    }

    /**
     * 新增账户
     *
     * @param account  要新增的账户对象
     * @param rolesIds 对应的角色ID数组
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void addAccount(Account account, Integer[] rolesIds) {
        account.setCreatTime(new Date());
        //默认密码为手机后6位
        String password;
        if(account.getAccountMobile().length() <= 6) {
            password = account.getAccountMobile();
        } else {
            password = account.getAccountMobile().substring(6);
        }

        password = DigestUtils.md5Hex(password);
        account.setAccountPassword(password);
        account.setAccountState(Account.STATE_NORMAL);

        accountMapper.insertSelective(account);

        //添加账号和角色的对应关系
        for(Integer roleId : rolesIds) {
            AccountRolesKey accountRolesKey = new AccountRolesKey();
            accountRolesKey.setAccountId(account.getId());
            accountRolesKey.setRolesId(roleId);

            accountRolesMapper.insert(accountRolesKey);
        }
    }

    /**
     * 根据URL传过来的的参数查询所有账号及对应的角色
     *
     * @param requestParam
     * @return
     */
    @Override
    public List<Account> findAllAccountWithRolesByQueryParam(Map<String, Object> requestParam) {
        return accountMapper.findAllWithRolesByQueryParam(requestParam);
    }

    /**
     * 根据电话号码查找Account对象
     *
     * @param userMobile
     * @return
     */
    @Override
    public Account findByMobile(String userMobile) {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(userMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        if(accountList !=null && !accountList.isEmpty()) {
            return accountList.get(0);
        }
        return null;
    }

    /**
     * 新增登录的日志记录
     *
     * @param accountLoginLog
     */
    @Override
    public void saveAccountLoginLog(AccountLoginLog accountLoginLog) {
        accountLoginLogMapper.insertSelective(accountLoginLog);
    }
}
