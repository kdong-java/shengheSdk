package com.sdk.server.entities;

import lombok.Data;

@Data
public class UserLogin {
    private String idCardNumber;//身份证
    private String accountNumber;//账号
    private String age;         //年龄
    private String loginTime;   //登陆时间
    private String logoutTime;  //登出时间
}
