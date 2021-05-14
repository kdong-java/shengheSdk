/*
package com.sdk.server.filter;

import com.sdk.server.controller.LoginController;
import com.sdk.server.entities.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;

*/
/**
 * 验证用户是否登录filter
 *//*


@WebFilter(filterName = "sessionFilter",urlPatterns = {"/*"})
public class LoginCheckFilter implements Filter {
    private static Logger log = LogManager.getLogger(LoginCheckFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession();
        String[] paths = new String[]{
                "/loginac",                 //登陆页面
                "/token",                   //生成token
                "/ResetPassword",           //重置密码
                "/RegistByPhoneNumber",     //注册账号
                "/GetVerificationCode",     //获取验证码
                "/CheckVerificationCode",   //验证验证码是否正确
                "/SMRZ"};
        String sp = request.getServletPath();
        for (String path : paths) {
            // 当前路径是这几个之一
            if (path.equals(sp)) {
                filterChain.doFilter(request, response);
                log.error("当前访问的网址为:"+sp+"未被拦截");
                return;
            }
        }
        User user = (User) session.getAttribute("loginUser");
        if(user==null){
            request.getRequestDispatcher("/loginac").forward(request, response);
        }else{
            filterChain.doFilter(request, response);
        }


    }
        @Override
    public void destroy() {

     }
}
*/
