package com.jk;

import com.jk.xz.pojo.UserBean;
import com.jk.xz.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
class EsDemoApplicationTests {
@Autowired
private UserService userService;
    @Test
    void contextLoads() {
        UserBean user = new UserBean();
        user.setAge(22);
        user.setId("1");
        user.setName("程程");
        user.setDetail("小仙女");
        userService.add(user);
    }

    @Test
    void del(){
         userService.del(1);
    }

    @Test
    void update(){
        UserBean user = new UserBean();
        user.setAge(19);
        user.setId("1");
        user.setName("张三123");
        user.setDetail("文艺青年");
        userService.add(user);
        userService.update(user);
    }

    @Test
    public void find(){
        int page=1;
        int rows=2;
        int start=(page-1)*rows;
        List<UserBean> list=userService.find(start,rows);
        System.out.println(list);
    }

}
