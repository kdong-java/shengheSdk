package com.sdk.server.entities;

import lombok.Data;

/**
 * 用户参数
 */
@Data
public class User {
    public String name;//用户名
    public String userTel;//用户手机号*登陆账号*
    public String passwd;//密码
    public String code;//验证码
}
