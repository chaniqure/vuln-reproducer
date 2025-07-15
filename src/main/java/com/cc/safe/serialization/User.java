package com.cc.safe.serialization;


import lombok.*;

import java.io.*;

@AllArgsConstructor
@ToString
public class User implements Serializable {
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private int age;
    @Getter
    @Setter
    private String msg;



    public static void main(String[] args) {
        serializeObject();
        deserializeObject();
    }
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        System.out.println(1111);
        Runtime.getRuntime().exec("calc");
        return;
    }
    private static String file = "E:/code/safe/vuln-reproducer/vuln-reproducer/src/main/java/com/cc/safe/serialization/user.txt";
    /**
     * 将 Student 对象序列化到文件
     */
    private static void serializeObject() {
        User student = new User("张三", 20, "这是一个秘密信息");

        try (FileOutputStream fileOut = new FileOutputStream(file);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {

            out.writeObject(student);
            System.out.println("原始对象: " + student);

        } catch (Exception i) {
            i.printStackTrace();
        }
    }

    /**
     * 从文件反序列化 Student 对象
     */
    private static void deserializeObject() {
        User student = null;
        try (FileInputStream fileIn = new FileInputStream(file);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {

            student = (User) in.readObject();
            System.out.println("反序列化后的对象: " + student);
        } catch (IOException i) {
            i.printStackTrace();
            return;
        } catch (ClassNotFoundException c) {
            System.out.println("Student 类未找到");
            c.printStackTrace();
            return;
        }
    }
}
