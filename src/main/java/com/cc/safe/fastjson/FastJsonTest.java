package com.cc.safe.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cc.safe.serialization.User;
import lombok.Data;

import java.io.IOException;

public class FastJsonTest {
    public static void main(String[] args) {
        User user = new User("qq", 18, "这是一个秘密信息");
        System.out.println(JSONObject.toJSONString(user));
        System.out.println(JSON.toJSONString(user, SerializerFeature.WriteClassName));

        System.out.println("=== 测试内部类与顶级类的区别 ===");

        // 测试1：尝试触发顶级类（应该成功）
        String topLevelClassJson = "{\"@type\":\"com.cc.safe.fastjson.Vuln\",\"name\":\"张三\",\"age\":\"20\",\"msg\":\"这是一个秘密信息\"}";
        System.out.println("测试顶级类: " + topLevelClassJson);
        try {
            Object obj = JSON.parseObject(topLevelClassJson);
            System.out.println("顶级类反序列化成功: " + obj);
        } catch (Exception e) {
            System.err.println("顶级类反序列化失败: " + e.getMessage());
        }

        // 测试2：尝试触发内部类（应该失败或不触发构造函数）
        // String innerClassJson = "{\"@type\":\"com.cc.safe.fastjson.FastJsonTest$InnerVuln\",\"name\":\"李四\",\"age\":\"25\",\"msg\":\"内部类测试\"}";
        // System.out.println("\n测试内部类: " + innerClassJson);
        // try {
        //     Object obj = JSON.parseObject(innerClassJson);
        //     System.out.println("内部类反序列化成功: " + obj);
        // } catch (Exception e) {
        //     System.err.println("内部类反序列化失败: " + e.getMessage());
        // }

    }

    @Data
    public class InnerVuln {
        public String name;
        public String age;
        public String msg;

        public InnerVuln() {
            // 内部类的默认构造函数
            System.out.println("!!! 内部类 InnerVuln 构造函数被调用了 !!!");
            try {
                Runtime.getRuntime().exec("notepad"); // 使用notepad来区分内部类和顶级类
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
