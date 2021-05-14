package com.sdk.server.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sdk.server.entities.Token;
import com.sdk.server.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import sun.misc.BASE64Encoder;

import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class JwtUtil {
    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME = 2 * 60 * 60 * 1000;
    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "916b10f2d14d2a6aeb4d6b8428a4f6eae11f36ea0d842a1c82ca0897999046e13dfeade805c8c045790eb838928ad7b9";

    public  TokenService tokenService;
   Logger log = LoggerFactory.getLogger(getClass());


    /**
     * 生成签名，两小时后过期
     * @param appid 软件id
     * @param appkey  appkey
     * @param grant_type 获取 access_token 时值为 client_credential
     * @return 加密的token
     */
    public static String createToken(String appid, String appkey, String grant_type) {
        Date nowDate=new Date();
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        //返回errmsg
        JsonResult jsonResult = null;
        if(appid == null) {
            jsonResult = new JsonResult<>("", "40015", "appid错误");
            return JSON.toJSONString(jsonResult);
        } else if(appkey == null){
            jsonResult = new JsonResult<>("", "40017", "appkey错误");
            return JSON.toJSONString(jsonResult);
        }else if(grant_type == null){
            jsonResult = new JsonResult<>("", "40020", "grant_type 错误");
            return JSON.toJSONString(jsonResult);
        }
        else {
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            String token = JWT.create()
                    .withHeader(header)
                    .withClaim("appid", appid)
                    .withClaim("appkey", appkey)
                    .withClaim("grant_type", grant_type)
                    .withIssuedAt(nowDate)
                    .withExpiresAt(date)
                    .sign(algorithm);
            String jsonStr = "{\"tokenJson\": [{" +
                    "  \"access_token\":  \""+token+"\" ,\n"+
                    "  \"expires_in\": \""+EXPIRE_TIME+"\" ,\n"+
                    "}]}";
            return jsonStr;
        }
    }

    /**
     * 校验token是否正确
     * @param token 密钥
     * @return  是否正确
     */
    public static boolean verify(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验token是否过期
     */
    public boolean isTokenExpired(Date expiration){
            return expiration.before(new Date());
    }
    /**
     * 通过login接口获取到登录凭证后
     * @param appid
     * @param appkey
     * @param code
     * @return
     */
    public static String code2Session(String appid, String appkey,String code) throws NoSuchAlgorithmException {
        long  timeNew =  System.currentTimeMillis()/ 1000;
        //language=JSON
        String codeStr="{\n" +
                "  \"time\":" + timeNew + ",\n" +
                "  \"appid\": " + appid + ",\n" +
                "  \"appkey\": " + appkey + ",\n" +
                "  \"code\": " + code + "\n" +
                "}";
        Algorithm algorithm = Algorithm.HMAC256(codeStr);

        byte[] inputData = code.getBytes();
       String openId= new BASE64Encoder().encodeBuffer(inputData);
        //返回errmsg
        JsonResult jsonResult = null;
        if(appid == null) {
            jsonResult = new JsonResult<>("", "40015", "appid错误");
            return JSON.toJSONString(jsonResult);
        } else if(appkey == null){
            jsonResult = new JsonResult<>("", "40017", "appkey错误");
            return JSON.toJSONString(jsonResult);
        }else if(code == null){
            jsonResult = new JsonResult<>("", "40020", "code错误");
            return JSON.toJSONString(jsonResult);
        }
        else {
            Map<String, Object> header = new HashMap<>();
            header.put("typ", "JWT");
            header.put("alg", "HS256");
            String jsonCode = JWT.create()
                    .withHeader(header)
                    .withClaim("codeStr", codeStr)
                    .sign(algorithm);

            return jsonCode;
        }
    }
}
