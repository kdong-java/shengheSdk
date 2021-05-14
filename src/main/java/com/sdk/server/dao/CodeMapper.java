package com.sdk.server.dao;

import com.sdk.server.entities.Code;
import com.sdk.server.entities.UserLogin;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CodeMapper {
    /**
     * 将code2Session  时间戳+appid+appkey+code加密存入数据库
     * @param code1
     * @return
     */
    int insertCode(Code code1);


    /**
     * 通过idCardNumber和accountNumber查询是否有用户
     * @return
     * @param userLogin
     */
    UserLogin selectByIdAndAccount(UserLogin userLogin);


    /**
     * 将身份证，账号，年龄，登陆时间，退出时间和五个放一起的字符串加密存入数据库中
     */
    int insertBackCode(UserLogin userLogin);

    /**
     * 查询不到用户就插入
     * @param userLogin
     * @return
     */
    int insertUser(UserLogin userLogin);

    /**
     * 修改用户登入时间
     * @param userLogin
     * @return
     */
    int updateByLoginTime(UserLogin userLogin);

    /**
     * 修改用户登出时间
     * @param userLogin
     * @return
     */
    int updateByLogoutTime(UserLogin userLogin);
}
