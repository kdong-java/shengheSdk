package com.sdk.server.service;

import com.aliyuncs.exceptions.ClientException;
import com.sdk.server.dao.CodeMapper;
import com.sdk.server.dao.UserMapper;
import com.sdk.server.entities.User;
import com.sdk.server.entities.UserLogin;
import com.sdk.server.util.AliMsgUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.misc.BASE64Encoder;

/**
 * 用户服务接口
 */
@Transactional
@Service
public class UserService {
    @Autowired(required = false)
    private CodeMapper codeMapper;
    @Autowired
    private UserMapper userMapper;

   private AliMsgUtil aliMsgUtil;
    private static Logger log = LogManager.getLogger(UserService.class.getName());
    //验证成功返回信息
    private final int SUCCESS_MSG=1;
    private final int FAIL_MSG=0;

    public User searchUser(String userTel, String passWord) {
        //查询用户
        User user=new User();
        byte[] inputData = passWord.getBytes();
        String jiamiPwd= new BASE64Encoder().encodeBuffer(inputData);
        user=userMapper.selectByTelAndPwd(userTel,jiamiPwd);
        return null;

    }


    /**
     * 通过idCardNumber 和accountNumber查找数据
     * @param idCardNumber
     * @param accountNumber
     * @param age
     * @param loginTime
     * @param logoutTime
     * @return
     */
    public String getLoginInfo(String idCardNumber, String accountNumber, String age, String loginTime, String logoutTime) {
        UserLogin userLogin=new UserLogin();
        userLogin.setIdCardNumber(idCardNumber);
        userLogin.setAccountNumber(accountNumber);
        userLogin.setAge(age);
        userLogin.setLoginTime(loginTime);
        userLogin.setLogoutTime(logoutTime);
       UserLogin userList=null;
        userList=codeMapper.selectByIdAndAccount(userLogin);
        String backJson=null;
        log.error(userList);
        if(userList!=null){
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;
        }else{
            int a=codeMapper.insertUser(userLogin);
            userList=codeMapper.selectByIdAndAccount(userLogin);
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;
        }
    }

    /**
     * 短信验证码
     *
     * @param tel
     * @param passWord
     * @return
     */
    public String registByPhoneNumber(String tel, String passWord) {
        String backMsg="";
        //先查找数据库中是否有相同用户
        int a=userMapper.selectByTel(tel);
        if(a!=0){
            backMsg="请勿重复注册！";
            return backMsg;
        }else{
            int b=userMapper.insertNewUser(tel,passWord);
            if(b==0){
                log.error("存入新用户入库失败！");
            }
            backMsg="注册成功！";
            return backMsg;
        }

    }

    /**
     * 检验验证码
     *
     * @param phoneNumber
     * @return
     */
    public String GetVerificationCode(String phoneNumber) {
        String backMsg = "";
        //验证外部sdk发送手机验证码
        String msgCode = null;
        try {
            msgCode = aliMsgUtil.sendMsg(phoneNumber);
        } catch (ClientException e) {
            log.error("验证码未发送！", e.toString());
        }
        return msgCode;
    }



    /**
     * 重置密码
     * @param userTel
     * @param pwd
     * @return
     */
    public String resetPwd(String userTel, String pwd) {
        String backMsg="";
        //删除用户之前信息并插入新用户
        int a=userMapper.deleteOldUser(userTel);
        if(a==0){
            backMsg="删除旧用户失败！";
            log.error("删除旧用户失败！");
            return backMsg;
        }
        int b=userMapper.insertNewUser(userTel,pwd);
        if(a==0){
            backMsg="重置密码失败！";
            log.error("重置密码失败！");
            return backMsg;
        }
        backMsg="密码重置成功!";
            return backMsg;
        }

    /**
     * 验证验证码
     * @param codeMsg
     * @return int
     */
    public int checkCodeMsg(String codeMsg,String userCode) {
        if (codeMsg == userCode) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     *
     * @param idCardNumber
     * @param accountNumber
     * @param age
     * @param loginTime
     * @param logoutTime
     * @return
     */
    public String insertLoginTime(String idCardNumber, String accountNumber, String age, String loginTime, String logoutTime) {

        UserLogin userLogin=new UserLogin();
        userLogin.setIdCardNumber(idCardNumber);
        userLogin.setAccountNumber(accountNumber);
        userLogin.setAge(age);
        userLogin.setLoginTime(loginTime);
        userLogin.setLogoutTime(logoutTime);
        int b=codeMapper.updateByLoginTime(userLogin);
        UserLogin userList=null;
        userList=codeMapper.selectByIdAndAccount(userLogin);
        String backJson=null;
        log.error(userList);
        if(userList!=null){
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;

        }else{
            int a=codeMapper.insertUser(userLogin);
            userList=codeMapper.selectByIdAndAccount(userLogin);
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;
        }
    }

    /**
     * 插入登出时间
     */
    /**
     *
     * @param idCardNumber
     * @param accountNumber
     * @param age
     * @param loginTime
     * @param logoutTime
     * @return
     */
    public String insertLogoutTime(String idCardNumber, String accountNumber, String age, String loginTime, String logoutTime) {

        UserLogin userLogin=new UserLogin();
        userLogin.setIdCardNumber(idCardNumber);
        userLogin.setAccountNumber(accountNumber);
        userLogin.setAge(age);
        userLogin.setLoginTime(loginTime);
        userLogin.setLogoutTime(logoutTime);
        int b=codeMapper.updateByLogoutTime(userLogin);
        UserLogin userList=null;
        userList=codeMapper.selectByIdAndAccount(userLogin);
        String backJson=null;
        log.error(userList);
        if(userList!=null){
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;
        }else{
            int a=codeMapper.insertUser(userLogin);
            userList=codeMapper.selectByIdAndAccount(userLogin);
            idCardNumber=userList.getIdCardNumber();
            accountNumber=userList.getAccountNumber();
            age=userList.getAge();
            loginTime=userList.getLoginTime();
            logoutTime=userList.getLogoutTime();
            backJson= "{\"userLogin\": [{" +
                    "  \"idCardNumber\":  \""+idCardNumber.toString()+"\" ,\n"+
                    "  \"accountNumber\":  \""+accountNumber.toString()+"\" ,\n"+
                    "  \"age\":  \""+age.toString()+"\" ,\n"+
                    "  \"loginTime\":  \""+loginTime.toString()+"\" ,\n"+
                    "  \"logoutTime\":  \""+logoutTime.toString()+"\" ,\n"+
                    "  \"MSG\":" + "1" + "\n" +
                    "}]}";
            return backJson;
        }
    }
}

