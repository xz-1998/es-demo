package com.jk.xz.service;

import com.jk.xz.pojo.UserBean;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    void add(UserBean user);

    void del(int i);

    void update(UserBean user);

    List<UserBean> find(int start, int rows);

    HashMap<String, Object> queryUser(UserBean user);

    void addOrUp(UserBean user);

    UserBean byId(String id);
}
