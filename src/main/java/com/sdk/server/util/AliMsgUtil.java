package com.sdk.server.util;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

import java.util.Random;

public class AliMsgUtil {

    StringBuilder code = new StringBuilder();

    public String sendMsg(String phoneNumber) throws ClientException {
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
               System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
               final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
         final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
        //替换成你的AK
                final String accessKeyId = "your accessKeyId";//你的accessKeyId
        final String accessKeySecret = "your accessKeySecret";//你的accessKeySecret
         //初始化ascClient,暂时不支持多region（请勿修改）
              IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
         IAcsClient acsClient = new DefaultAcsClient(profile);
        //组装请求对象
              SendSmsRequest request = new SendSmsRequest();
         //使用post提交
              request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
              request.setPhoneNumbers(phoneNumber);
        //必填:短信签名-可在短信控制台中找到
             request.setSignName("your signName");
         //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
              request.setTemplateCode("your templateCode");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
                //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
               //request.setTemplateParam("{\"code\":\"988756\"}");
             String msgCode = getMsgCode();
        request.setTemplateParam("{\"code\":\"" + msgCode + "\"}");
         //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            //请求成功
            System.out.println("=====success====");
        } else {
            System.out.println("=====fail=======");
        }
         return msgCode;
     }



          /**
 53      * 生成随机的6位数，短信验证码
 54      * @return
 55      */
         private static String getMsgCode() {
               int n = 6;
               StringBuilder code = new StringBuilder();
              Random ran = new Random();
             for (int i = 0; i < n; i++) {
                code.append(Integer.valueOf(ran.nextInt(10)).toString());
            }
               return code.toString();
          }

}
