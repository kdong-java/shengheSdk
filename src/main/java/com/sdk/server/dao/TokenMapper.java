package com.sdk.server.dao;

import com.sdk.server.entities.Token;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface TokenMapper {

    int insertToken(Token token);

}
