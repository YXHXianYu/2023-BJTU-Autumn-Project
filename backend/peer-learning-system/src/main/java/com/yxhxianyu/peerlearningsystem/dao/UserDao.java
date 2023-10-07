package com.yxhxianyu.peerlearningsystem.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import org.apache.ibatis.annotations.Mapper;

/**
 * DAO类，作为一个中间层，提供一个Java代码到数据库的映射
 * @author YXH_XianYu
 * @date 2023/10/7 23:17
 **/
@Mapper
public interface UserDao extends BaseMapper<UserPojo> {
}
