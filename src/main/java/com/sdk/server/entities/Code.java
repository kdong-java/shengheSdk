package com.sdk.server.entities;

import lombok.Data;

/**
 * 登陆后生成的凭证
 */
@Data
public class Code {
    public String date;   //生成凭证时间
    public String code;    //访问返回的code
    public String appid;    //软件id；
    public String appkey;   //软件key
    public String jiamiCode; //生成的加密凭证
}
