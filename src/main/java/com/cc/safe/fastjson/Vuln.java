package com.cc.safe.fastjson;

import lombok.Data;

import java.io.IOException;

@Data
public class Vuln {
    public String name;
    public String age;
    public String msg;

    public Vuln() {
        // 顶级类的默认构造函数
        System.out.println("!!! 顶级类 Vuln 构造函数被调用了 !!!");
        try {
            Runtime.getRuntime().exec("calc"); // 漏洞代码：执行命令
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
