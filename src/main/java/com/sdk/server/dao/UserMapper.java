package com.sdk.server.dao;

import com.sdk.server.entities.User;
import com.sdk.server.entities.UserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    /**
     * 查找用户
     * @param userTel
     * @param password
     * @return
     */
    User selectByTelAndPwd(@Param("userTel")String userTel,@Param("password")String password);


    /**
     * 插入用户
     * @param userTel
     * @param password
     * @return
     */
    int insertNewUser(@Param("userTel") String userTel, @Param("password") String password);

    /**
     * 删除之前用户
     */
    int deleteOldUser(@Param("userTel") String userTel);

    /**
     * 查找是否有相同用户
     * @param tel
     * @return
     */
    int selectByTel(@Param("userTel") String tel);
}
