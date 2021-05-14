package com.jk.xz.dao;

import com.jk.xz.pojo.UserBean;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserDao extends ElasticsearchRepository<UserBean, Integer> {
}
