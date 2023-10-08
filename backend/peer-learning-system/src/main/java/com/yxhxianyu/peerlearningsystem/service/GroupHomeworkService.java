package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.peerlearningsystem.dao.GroupHomeworkDao;
import com.yxhxianyu.peerlearningsystem.pojo.GroupHomeworkPojo;
import com.yxhxianyu.peerlearningsystem.pojo.ProblemPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

/**
 * GroupHomework的服务
 * @author YXH_XianYu
 * @date 2023/10/8 22:07
 **/
@Service
public class GroupHomeworkService {

    @Autowired
    GroupHomeworkDao groupHomeworkDao;

    public static int HOMEWORK_STATE_RELEASED = 0;

    public static int HOMEWORK_STATE_START_RATING = 1;

    /**
     * 插入一个新的GroupHomework。
     * 并返回这个GroupHomework的UUID，
     * 若insert失败，则返回空字符串
     */
    public String insertGroupHomework(String name, String problemUUID, Date submitDeadline, Date ratingDeadline) {
        String uuid = UUID.randomUUID().toString();
        try {
            groupHomeworkDao.insert(new GroupHomeworkPojo(uuid, name, problemUUID, HOMEWORK_STATE_RELEASED, submitDeadline, ratingDeadline));
            return uuid;
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username, name is \"" + name + "\"");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long or foreign key not exists), name is \"" + name + "\"");
        }
        return "";
    }

    /**
     * 根据UUID删除一个GroupHomework
     */
    public void deleteGroupHomeworkByUUID(String uuid) {
        groupHomeworkDao.deleteById(uuid);
    }

    /**
     * 根据名称删除一个GroupHomework
     */
    public void deleteGroupHomeworkByName(String name) {
        GroupHomeworkPojo pojo = getGroupHomeworkByName(name);
        if(pojo != null)
            groupHomeworkDao.deleteById(pojo.getUuid());
    }

    /**
     * 根据UUID修改提交截止时间
     */
    public void updateSubmitDeadline(String uuid, Date date) {
        GroupHomeworkPojo pojo = groupHomeworkDao.selectById(uuid);
        if(pojo == null) return;
        pojo.setSubmitDeadline(date);
        groupHomeworkDao.updateById(pojo);
    }

    /**
     * 根据UUID修改互评截止时间
     */
    public void updateRatingDeadline(String uuid, Date date) {
        GroupHomeworkPojo pojo = groupHomeworkDao.selectById(uuid);
        if(pojo == null) return;
        pojo.setRatingDeadline(date);
        groupHomeworkDao.updateById(pojo);
    }

    /**
     * 根据UUID更新作业状态
     */
    public void updateState(String uuid, int state) {
        GroupHomeworkPojo pojo = groupHomeworkDao.selectById(uuid);
        if(pojo == null) return;
        pojo.setState(state);
        groupHomeworkDao.updateById(pojo);
    }

    /**
     * 根据UUID查询GroupHomework
     */
    @Nullable
    public GroupHomeworkPojo getGroupHomeworkByUUID(String uuid) {
        return groupHomeworkDao.selectById(uuid);
    }

    /**
     * 根据名称查询GroupHomework
     */
    @Nullable
    public GroupHomeworkPojo getGroupHomeworkByName(String name) {
        return groupHomeworkDao.selectOne(new QueryWrapper<GroupHomeworkPojo>().eq("name", name));
    }

    /**
     * 根据名称获取UUID
     * 若找不到该记录，则返回空字符串
     */
    public String getUUIDByName(String name) {
        GroupHomeworkPojo pojo = groupHomeworkDao.selectOne(new QueryWrapper<GroupHomeworkPojo>().eq("name", name));
        return (pojo == null ? "" : pojo.getUuid());
    }

    /**
     * 查询所有GroupHomework
     */
    public List<GroupHomeworkPojo> getAllGroupHomeworks() {
        return groupHomeworkDao.selectList(new QueryWrapper<>());
    }

}
