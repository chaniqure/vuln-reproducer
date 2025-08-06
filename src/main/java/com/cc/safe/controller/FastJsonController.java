package com.cc.safe.controller;

import com.alibaba.fastjson.JSONObject;
import com.cc.safe.serialization.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fastjson")
public class FastJsonController {
    @PostMapping
    public String post(@RequestBody String body) {
        // 这里可以添加日志记录或其他处理逻辑
        User vuln = JSONObject.parseObject(body, User.class);
        return "success";
    }
}
