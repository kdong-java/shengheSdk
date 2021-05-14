package com.sdk.server.service;

import com.sdk.server.dao.TokenMapper;
import com.sdk.server.entities.Token;
import com.sdk.server.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional
@Service
public class TokenService {
        @Autowired(required = false)
        private TokenMapper tokenMapper;
    Logger log = LoggerFactory.getLogger(getClass());



    /**
     * 处理token凭证
     * @param appid
     * @param secret
     * @param grant_type
     * @return
     */
    public String setToken(String appid, String secret, String grant_type) {
        String token= JwtUtil.createToken(appid,secret,grant_type);
        Date nowDate=new Date();
        //将token存入数据库中
        Token token1=new Token();
        token1.setAppid(appid);
        token1.setAppkey(secret);
        token1.setDate(nowDate.toString());
        token1.setGrant_type(grant_type);
        token1.setToken(token);
        int a=tokenMapper.insertToken(token1);
        return token;
    }
}
