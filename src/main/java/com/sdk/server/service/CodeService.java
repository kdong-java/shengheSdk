package com.sdk.server.service;

import com.sdk.server.dao.CodeMapper;
import com.sdk.server.dao.TokenMapper;
import com.sdk.server.entities.Code;
import com.sdk.server.entities.Token;
import com.sdk.server.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
@Transactional
@Service
public class CodeService {
    @Autowired(required = false)
    private CodeMapper codeMapper;
    public String setCode(String appid, String appkey, String code) throws NoSuchAlgorithmException {
        String jiamiCode= JwtUtil.code2Session(appid,appkey,code);
        Date nowDate=new Date();
        //将token存入数据库中
      Code code1=new Code();
        code1.setAppid(appid);
        code1.setAppkey(appkey);
        code1.setDate(nowDate.toString());
        code1.setCode(code);
        code1.setJiamiCode(jiamiCode);
        int a=codeMapper.insertCode(code1);
        return jiamiCode;
    }
}
