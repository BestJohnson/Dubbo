package com.kaishengit.shiro;

import com.kaishengit.service.RolePermissionService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**
 * 动态定义权限和URL的关系
 */
public class MyFilterChainDefinition  {

    //
    /*implements FactoryBean<Ini.Section>*/

    @Autowired
    private RolePermissionService rolePermissionService;

    private AbstractShiroFilter shiroFilter;
    private String filterChainDefinitions;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public void setShiroFilter(AbstractShiroFilter shiroFilter) {
        this.shiroFilter = shiroFilter;
    }


    /**
     * spring容器启动时调用
     */
    @PostConstruct
    public synchronized void  init() {

        System.out.println("-------------初始化-----------");
        //清除原有的
        getFilterChainManager().getFilterChains().clear();
        //加载新的
        load();

        System.out.println("------------初始化结束---------");
    }

    /**
     * 删、改时刷新URL和权限的关系
     */
    public synchronized void update() {
        System.out.println("-----------刷新------------");
        //清除原先的
        getFilterChainManager().getFilterChains().clear();
        //加载最新的
        load();
        System.out.println("---------刷新结束------------");
    }


    /**
     * 加载URL和权限的关系
     */
    public synchronized void load() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);

        //从数据库中查找所有权限对象
//        List<Permission> permissionList = rolePermissionService.findAllPermission();

        Ini.Section section = ini.get(Ini.DEFAULT_SECTION_NAME);

//        for(Permission permission : permissionList) {
//            section.put(permission.getUrl(),"perms[" + permission.getPermissionCode() + "]");
//        }
        section.put("/**","user");

        //URL和权限关系设置到shiroFilter中
        DefaultFilterChainManager defaultFilterChainManager = getFilterChainManager();
        for(Map.Entry<String,String> entry : section.entrySet()) {
            defaultFilterChainManager.createChain(entry.getKey(),entry.getValue());
        }

    }

    private DefaultFilterChainManager getFilterChainManager() {
        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();

        return defaultFilterChainManager;
    }


}
