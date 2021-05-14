package com.jk.xz.controller;

import com.jk.xz.pojo.UserBean;
import com.jk.xz.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    /**
    * @Author: xz
    * @Description: 分页查询
    * @Date: 2021/5/11 16:38
    * @Param:[user]
    * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
    **/
    @GetMapping
    public HashMap<String,Object> queryUser(UserBean user){

        return userService.queryUser(user);
    }
   /*
   * @Author: xz
   * @Description: 新增+修改
   * @Date: 2021/5/11 16:38
   * @Param:
   * @Return:
   **/
   @PostMapping("/addOrUp")
   public void addOrUp(UserBean user){
       userService.addOrUp(user);
   }
   /*
   * @Author: xz
   * @Description: 回显
   * @Date: 2021/5/11 17:42
   * @Param:
   * @Return:
   **/
   @GetMapping("/byId")
   public UserBean byId(String id){
       return userService.byId(id);
   }
}
