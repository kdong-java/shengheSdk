package com.sdk.server.controller;

import com.sdk.server.entities.User;
import com.sdk.server.entities.UserLogin;
import com.sdk.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/smrz")
public class XxsController {
    @Autowired
    private UserService userService;

    /**
     * 传入idCardNumber和accountNumber,查找数据库中有没有对应的数据
     * 有则返回age和loginTime,logoutTime 和1
     * 没有则返回0并存入库中
     * @param idCardNumber
     * @param accountNumber
     * @param age
     * @param loginTime
     * @param logoutTime
     * @return
     */
    @RequestMapping("/SMRZ")
    public String getReturnCode(String idCardNumber, String accountNumber, String age, String loginTime, String logoutTime){
          String backJson="";
           if(accountNumber==null){
               backJson= "{\n" +
                       "  \"MSG\":" + "账号未输入" + "\n" +
                       "  \"MSG\":" + "0" + "\n" +
                       "}";
               return backJson;
            }
            else if(loginTime==null){
               idCardNumber=(idCardNumber==null)? "0" : idCardNumber;
                age=(age==null)? "0" : age;
               loginTime="0";
                logoutTime=(logoutTime==null)? "0" : logoutTime;
        return userService.insertLogoutTime(idCardNumber,accountNumber,age,loginTime,logoutTime);
        } else if(logoutTime==null){
               idCardNumber=(idCardNumber==null)? "0" : idCardNumber;
               age=(age==null)? "0" : age;
               logoutTime="0";
               return userService.insertLoginTime(idCardNumber,accountNumber,age,loginTime,logoutTime);
           }else{
               idCardNumber=(idCardNumber==null)? "0" : idCardNumber;
               age=(age==null)? "0" : age;
               loginTime=(loginTime==null)? "0" : loginTime;
               loginTime=(loginTime==null)? "0" : loginTime;
               return userService.getLoginInfo(idCardNumber,accountNumber,age,loginTime,logoutTime);
           }
    }

}
