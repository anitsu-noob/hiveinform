package com.example.hiveinform;

import com.example.hiveinform.entity.JwtToken;
import com.example.hiveinform.entity.User;
import com.example.hiveinform.repository.JwtTokenRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRedis {
    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void test(){
//        JwtToken jwtToken = new JwtToken();
//        jwtToken.setToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MSIsImlhdCI6MTY4NDY3MTU1MSwiZXhwIjoxNjg0NjcyOTkxfQ.AKOa8AjZtVlfwST4PhLi80gDkqpZI8ByX7O1uttT57A");
//        jwtToken.setUsername("test7");
//        jwtToken.setExpiration(new Date((new Date().getTime()+86400000)));
//        System.out.println(jwtTokenRepository.save(jwtToken));;
        System.out.println(redisTemplate);;
    }

    @Test
    public void delete(){
        System.out.println(jwtTokenRepository.findAll());;

    }


    @Test
    public void testTemplate()
    {
    }
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Test
//    public void test() throws Exception {
//        stringRedisTemplate.opsForValue().set("aaa", "111");
//        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));
//    }

//    @Test
//    public void testObj() throws Exception {
//        User user=new User("aa@126.com", "aa", "aa123456", "aa","123");
//        ValueOperations<String, User> operations=redisTemplate.opsForValue();
//        operations.set("com.neox", user);
//        operations.set("com.neo.f", user,1, TimeUnit.SECONDS);
//        Thread.sleep(1000);
//        //redisTemplate.delete("com.neo.f");
//        boolean exists=redisTemplate.hasKey("com.neo.f");
//        if(exists){
//            System.out.println("exists is true");
//        }else{
//            System.out.println("exists is false");
//        }
//        // Assert.assertEquals("aa", operations.get("com.neo.f").getUserName());
//    }
}