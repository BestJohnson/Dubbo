package com.kaishengit.controller;



import com.kaishengit.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 首页及登录、登出控制器
 */

@Controller
public class HomeController  {

    @Autowired
    private AccountService accountService;


    /**
     * 系统的登录页面
     * @return
     */
    @GetMapping("/")
    public String home() {
        Subject subject = SecurityUtils.getSubject();


        //判断当前账户是否是被认证的的，是的话则退出
        if(subject.isAuthenticated()) {
            subject.logout();
        }

        //判断当前是否是被记住（通过rememberMe实现），是的话则直接跳转到登录成功页面
        if(subject.isRemembered()) {
            return "redirect:/home";
        }
        return "index";
    }

    /**
     * 登录
     * @return
     */
    @PostMapping("/")
    public String login(String accountMobile,
                        String password,
                        String rememberMe,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {

        Subject subject = SecurityUtils.getSubject();
        String requestIp = request.getRemoteAddr();

        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(accountMobile, DigestUtils.md5Hex(password), rememberMe != null,requestIp);

        try {
            subject.login(usernamePasswordToken);

            //登录后向退出登录前路径的跳转
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = "/home";

            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }

            return "redirect:" + url;

        } catch (UnknownAccountException | IncorrectCredentialsException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
        } catch (LockedAccountException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号被锁定");
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            redirectAttributes.addFlashAttribute("message","账号或密码错误");
        }

        return "redirect:/";
    }


    //安全退出使用shiro内置的过滤器，在spring-shiro.xml中配置

    @GetMapping("/401")
    public String unauthorizedUrl() {
        return "error/401";
    }

    /**
     * 登录成功后的页面
     * @return
     */
    @GetMapping("/home")
    public String hello() {
        return "home";
    }


}
