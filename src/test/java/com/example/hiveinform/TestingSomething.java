package com.example.hiveinform;

import com.example.hiveinform.entity.User;
import org.apache.poi.sl.usermodel.ObjectMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
@RunWith(SpringJUnit4ClassRunner.class)
public class TestingSomething {




        @Autowired
        private JavaMailSender javaMailSender;


        @Test
        public void sendSimpleMail() throws Exception {

            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("2938520740@qq.com");
            message.setTo("2938520740@qq.com");
            message.setSubject("主题：简单邮件");
            message.setText("测试邮件内容");

            javaMailSender.send(message);
        }

//    public static void main(String[] args) {
//        // 创建一个复杂对象
//        ComplexObject obj = new ComplexObject();
//        obj.setId(1);
//        obj.setName("John Doe");
//
//        List<String> hobbies = new ArrayList<>();
//        hobbies.add("Reading");
//        hobbies.add("Traveling");
//        obj.setHobbies(hobbies);
//
//        // 将对象转换为字节数组
//        byte[] byteArray = objectToByteArray(obj);
//
//        // 打印字节数组的长度和内容
//        System.out.println("Byte array length: " + byteArray.length);
//        System.out.println("Byte array content: " + new String(byteArray));
//
//
//    }
//
//    public static byte[] objectToByteArray(Serializable obj) {
//        try {
//            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
//            ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
//            objectStream.writeObject(obj);
//            objectStream.close();
//            return byteStream.toByteArray();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public static Object byteArrayToString(byte[] byteArray) {
//        try {
//            ByteArrayInputStream byteStream = new ByteArrayInputStream(byteArray);
//            ObjectInputStream objectStream = new ObjectInputStream(byteStream);
//            Object obj = objectStream.readObject();
//            objectStream.close();
//            return obj;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    // 复杂对象示例
//    static class ComplexObject implements Serializable {
//        private int id;
//        private String name;
//        private List<String> hobbies;
//
//        // 省略构造函数和 getter/setter 方法
//
//        public int getId() {
//            return id;
//        }
//
//        public void setId(int id) {
//            this.id = id;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//        public void setName(String name) {
//            this.name = name;
//        }
//
//        public List<String> getHobbies() {
//            return hobbies;
//        }
//
//        public void setHobbies(List<String> hobbies) {
//            this.hobbies = hobbies;
//        }
//    }
}
