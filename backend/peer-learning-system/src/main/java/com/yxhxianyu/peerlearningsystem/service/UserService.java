package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.peerlearningsystem.dao.UserDao;
import com.yxhxianyu.peerlearningsystem.pojo.ProblemPojo;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.Nullable;
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
     * 并返回该用户的UUID
     * 若insert失败，则返回以"ERROR"开头的字符串
     */
    public String insertUser(String username, String password, String email, int authority) {
        String uuid = UUID.randomUUID().toString();
        try {
            userDao.insert(new UserPojo(uuid, username, password, email, authority));
            return uuid;
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username");
            return "ERROR: 用户名重复";
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long)");
            return "ERROR: 数据不合法 (可能因为数据过长)";
        }
    }

    /**
     * 根据UUID删除一条用户
     * 在得到UUID时，请注意你的UserPojo对象非空，否则会在函数外部产生NullPointerException
     */
    public void deleteUserByUUID(String uuid) {
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
    @Nullable
    public UserPojo getUserByUUID(String uuid) {
        return userDao.selectById(uuid);
    }

    /**
     * 根据名字查询一条用户信息
     */
    @Nullable
    public UserPojo getUserByName(String username) {
        return userDao.selectOne(new QueryWrapper<UserPojo>().eq("username", username));
    }

    /**
     * 根据名字获取UUID
     */
    public String getUUIDByName(String name) {
        UserPojo pojo = userDao.selectOne(new QueryWrapper<UserPojo>().eq("username", name));
        return (pojo == null ? "" : pojo.getUuid());
    }

    /**
     * 查询所有用户
     */
    public List<UserPojo> getAllUsers() {
        return userDao.selectList(new QueryWrapper<>());
    }

}
