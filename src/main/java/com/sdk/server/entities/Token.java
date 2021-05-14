package com.sdk.server.entities;

import lombok.Data;

@Data
public class Token {
        public String date;
        public String appid;
        public String appkey;
        public String grant_type;
        public String token;

}
