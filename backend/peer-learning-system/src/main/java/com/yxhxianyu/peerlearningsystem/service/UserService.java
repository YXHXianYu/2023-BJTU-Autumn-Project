package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.peerlearningsystem.dao.UserDao;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * User表的服务
 * @author YXH_XianYu
 * @date 2023/10/7 23:28
 **/
@Service
public class UserService {

    @Autowired
    UserDao userDao;

    /**
     * 插入一条新的用户
     */
    public void insertUser(String username, String password, String email, int authority) {
        String uuid = UUID.randomUUID().toString();
        userDao.insert(new UserPojo(uuid, username, password, email, authority));
    }

    /**
     * 根据UUID删除一条用户
     * 在得到UUID时，请注意你的UserPojo对象非空，否则会在函数外部产生NullPointerException
     */
    public void deleteUser(String uuid) {
        userDao.deleteById(uuid);
    }

    /**
     * 根据名称删除一条用户
     */
    public void deleteUserByName(String username) {
        UserPojo user = getUserByName(username);
        if(user != null)
            userDao.deleteById(user.getUuid());
    }

    // 不支持修改

    /**
     * 根据UUID查询一条用户信息
     */
    public UserPojo getUserByUUID(String uuid) {
        return userDao.selectById(uuid);
    }

    /**
     * 根据名字查询一条用户信息
     */
    public UserPojo getUserByName(String username) {
        return userDao.selectOne(new QueryWrapper<UserPojo>().eq("username", username));
    }

    /**
     * 查询所有用户
     */
    public List<UserPojo> getAllUsers() {
        return userDao.selectList(new QueryWrapper<>());
    }

}
