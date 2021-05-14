package com.sdk.server.controller;

import com.alibaba.fastjson.JSON;
import com.sdk.server.entities.User;
import com.sdk.server.service.CodeService;
import com.sdk.server.service.TokenService;
import com.sdk.server.service.UserService;
import com.sdk.server.util.JsonResult;
import com.sdk.server.util.JwtUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private CodeService codeService;

    private static Logger log = LogManager.getLogger(LoginController.class.getName());

    private final static String SUCCESS_MSG="1";
    private final static String Fail_MSG="0";

    /**
     * 接口调用凭证
     * @param appid
     * @param
     * @param grant_type
     * @return
     */
    @RequestMapping("/token")
    public String getAccessToken(String appid, String appkey,String grant_type,HttpSession session){
        grant_type=(grant_type==null)? "0" : grant_type;
        String token=tokenService.setToken(appid,appkey,grant_type);
        session.setAttribute("token",token);
        return token;
    }

    /**
     * 登录服务调用
     * @param user
     * @return
     */
    @RequestMapping("/loginac")
    public String checkLogin(@RequestBody User user,String appid, String secret,String grant_type,HttpSession session){
        String userTel = user.userTel;
        String passWord = user.passwd;
        //根据用户名和密码查找用户
        User loginUser = userService.searchUser(userTel, passWord);
        JsonResult jsonResult = null;
        if(user == null)
            jsonResult = new JsonResult<>("", "0", Fail_MSG);
        else {
            Map<String, Object> map = new HashMap<>();
            String token = JwtUtil.createToken(appid, secret,grant_type);
            map.put("userData", loginUser);
            map.put("token", token);
            jsonResult = new JsonResult<>(map, SUCCESS_MSG);
        }
        session.setAttribute("loginUser",loginUser);

        return JSON.toJSONString(jsonResult);
    }


    /**
     * 忘记密码
     */
    @RequestMapping("/ResetPassword")
    public String fogetPwd(String userTel,String pwd,String codeMsg,String userCode) {
            String result = userService.resetPwd(userTel, pwd);
          return result;
        }


    /**
     * 点击完成注册
     */
    @RequestMapping("/RegistByPhoneNumber")
    public String registerByPhoneNumber(String userTel,String passWord,String codeMsg,String userCode){
        //验证验证码
      int backMsg=  userService.checkCodeMsg(codeMsg,userCode);
      if(backMsg==0){
          return "验证码输入错误！";
      }
      //存入新用户
      else{
          return userService.registByPhoneNumber(userTel,passWord);
      }

    }

    /**
     * 获取手机验证码
     */
    @RequestMapping("/GetVerificationCode")
    public String  GetVerificationCode(String phoneNumber) {
        return userService.GetVerificationCode(phoneNumber);
    }
    /**
     * 验证验证码
     */
    @RequestMapping("/CheckVerificationCode")
    public String CheckVerificationCode(String codeMsg,String userCode) {
        int backMsg = userService.checkCodeMsg(codeMsg, userCode);
        if (backMsg == 0) {
            return "验证失败！";
        } else {
            return "验证成功";
        }
    }

    /**
     * 登陆通过code进行加密
     * @param appid
     * @param appkey
     * @param code
     * @return
     * @throws NoSuchAlgorithmException
     */
    @RequestMapping("/user/get_code")
    public String code2Session(String appid,String appkey,String code) throws NoSuchAlgorithmException {
        return codeService.setCode(appid,appkey,code);
    }

    /**
     *登出
     * */
    @RequestMapping("/logout")
    public String logout(HttpSession session){
        System.out.println("退出");
        session.invalidate();//session失效
        return "default/login";
    }

}
