package com.cc.safe.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cc.safe.serialization.User;

public class FastJsonTest {
    public static void main(String[] args) {
        User user = new User("qq", 18, "这是一个秘密信息");
        System.out.println(JSONObject.toJSONString(user));
        System.out.println(JSON.toJSONString(user, SerializerFeature.WriteClassName));
    }
}
