package com.jk.xz.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.jk.xz.dao.UserDao;
import com.jk.xz.pojo.UserBean;
import com.jk.xz.service.UserService;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private ElasticsearchTemplate esTemplate;
    @Override
    public void add(UserBean user) {
        userDao.save(user);
    }

    @Override
    public void del(int i) {
        userDao.deleteById(i);
    }

    @Override
    public void update(UserBean user) {
        userDao.save(user);
    }

    @Override
    public List<UserBean> find(int start, int rows) {
        List<UserBean> list=new ArrayList<>();
        //获取客户端对象
        Client client = esTemplate.getClient();
        //创建查询请求对象：设置查询的索引、查询的类型
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setTypes("user");
        //条查
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "程");
        searchRequestBuilder.setQuery(matchQueryBuilder);
        //设置分页
        searchRequestBuilder.setFrom(start);
        searchRequestBuilder.setSize(rows);
        //执行当前查询对象 拿到返回结果集
        SearchResponse searchResponse = searchRequestBuilder.get();
        //获取到所有搜索出的结果 也叫所有命中对象
        SearchHits hits = searchResponse.getHits();
        //使用迭代器 循环迭代查询内容
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();//获取值
            String sourceAsString = next.getSourceAsString();
            UserBean userBean = JSONObject.parseObject(sourceAsString, UserBean.class);
            list.add(userBean);
        }
        return list;
    }
    /**
    * @Author: xz
    * @Description: 分页查询
    * @Date: 2021/5/11 11:45
    * @Param:[user]
    * @Return: java.util.HashMap<java.lang.String,java.lang.Object>
    **/
    @Override
    public HashMap<String, Object> queryUser(UserBean user) {
        List<UserBean> list=new ArrayList<>();
        //获取到当前elasticsearch的客户端对象

        Client client = esTemplate.getClient();

        //创建查询请求对象：设置查询的索引、查询的类型

        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setTypes("user");

        //条查
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(user.getName())){
            MatchQueryBuilder name = QueryBuilders.matchQuery("name", user.getName());
            boolQueryBuilder.must(name);
        }
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("birth");
        RangeQueryBuilder rangeQueryBuilder2 = QueryBuilders.rangeQuery("price");
        if (!StringUtils.isEmpty(user.getSbirth())){
            rangeQueryBuilder.gte(user.getBirth());
        }
        if (!StringUtils.isEmpty(user.getEbirth())){
            rangeQueryBuilder.lte(user.getEbirth());
        }
        if (!StringUtils.isEmpty(user.getEbirth()) || !StringUtils.isEmpty(user.getSbirth())){
            boolQueryBuilder.must(rangeQueryBuilder);
        }

        if (user.getSprice()!=null&&user.getEprice()!=null){
            rangeQueryBuilder2.gte(user.getSprice()).lte(user.getEprice());
            boolQueryBuilder.must(rangeQueryBuilder2);
        }
        else{
            if (user.getSprice()!=null){
                rangeQueryBuilder2.gte(user.getSprice());
                boolQueryBuilder.must(rangeQueryBuilder2);
            }

            if (user.getEprice()!=null){
                rangeQueryBuilder2.lte(user.getEprice());
                boolQueryBuilder.must(rangeQueryBuilder2);
            }
        }
        searchRequestBuilder.setQuery(boolQueryBuilder);
        //开始位置
        int start= (user.getPage()-1)*user.getRows();
        //排序
        searchRequestBuilder.addSort("age", SortOrder.ASC);
        //开始位置
        searchRequestBuilder.setFrom(start);
        //结束位置
        searchRequestBuilder.setSize(user.getRows());

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name");
        highlightBuilder.preTags("<font color='red'>");
        highlightBuilder.postTags("</font>");
        searchRequestBuilder.highlighter(highlightBuilder);
        //执行当前查询对象 拿到返回结果集

        SearchResponse searchResponse = searchRequestBuilder.get();

        //获取到所有搜索出的结果 也叫所有命中对象

        SearchHits hits = searchResponse.getHits();

        //获取总条数
        long totalHits = hits.getTotalHits();
        //迭代器
        Iterator<SearchHit> iterator = hits.iterator();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            String sourceAsString = next.getSourceAsString();
            UserBean userBean = JSONObject.parseObject(sourceAsString, UserBean.class);
            //获取到所有高亮字段结果集
            Map<String, HighlightField> highlightFields = next.getHighlightFields();
            //获取姓名的高亮结果
            HighlightField name = highlightFields.get("name");
            if (name!=null){
                userBean.setName(name.getFragments()[0].toString());
            }
            list.add(userBean);
        }

        HashMap<String, Object> map=new HashMap<>();
        map.put("rows",list);
        map.put("total",totalHits);
        return map;
    }
    /*
    * @Author: xz
    * @Description: 新增+修改
    * @Date: 2021/5/11 16:40
    * @Param:[user]
    * @Return: void
    **/
    @Override
    public void addOrUp(UserBean user) {
        if (user.getId()==null){
            UUID uuid = UUID.randomUUID();
            user.setId(uuid.toString());
            userDao.save(user);
        }
        else{
            userDao.save(user);
                }
    }
    /*
    * @Author: xz
    * @Description: 回显
    * @Date: 2021/5/11 17:43
    * @Param:[id]
    * @Return: com.jk.xz.pojo.UserBean
    **/
    @Override
    public UserBean byId(String id) {
        Client client = esTemplate.getClient();
        SearchRequestBuilder searchRequestBuilder = client.prepareSearch("user").setTypes("user");
        MatchQueryBuilder query = QueryBuilders.matchQuery("id", id);
        SearchRequestBuilder searchRequestBuilder1 = searchRequestBuilder.setQuery(query);
        //执行当前查询对象 拿到返回结果集
        SearchResponse response = searchRequestBuilder.get();
        //获取到所有搜索出的结果 也叫所有命中对象
        SearchHits hits = response.getHits();
        Iterator<SearchHit> iterator = hits.iterator();

        UserBean userBean = new UserBean();
        while (iterator.hasNext()){
            SearchHit next = iterator.next();
            String string = next.getSourceAsString();
            //把json字符串转换成java对象
            UserBean user= JSONObject.parseObject(string, UserBean.class);
            userBean=user;
        }
        return userBean;
    }

}
