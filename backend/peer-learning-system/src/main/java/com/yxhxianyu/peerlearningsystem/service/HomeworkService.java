package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.peerlearningsystem.dao.HomeworkDao;
import com.yxhxianyu.peerlearningsystem.pojo.HomeworkPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 作业的服务
 * @author YXH_XianYu
 * @date 2023/10/8 22:58
 **/
@Service
public class HomeworkService {

    @Autowired
    HomeworkDao homeworkDao;

    /**
     * 增加一条作业
     * 返回这条作业的UUID
     * 若insert失败，则返回空字符串
     */
    public String insertHomework(String groupHomeworkUUID, String userUUID) {
        String uuid = UUID.randomUUID().toString();
        try {
            homeworkDao.insert(new HomeworkPojo(uuid, groupHomeworkUUID, userUUID, "", false, false, 0.0f));
            return uuid;
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long)");
        }
        return "";
    }

    /**
     * 根据UUID删除一个作业
     */
    public void deleteHomeworkByUUID(String uuid) {
        homeworkDao.deleteById(uuid);
    }

    /**
     * 更新上传作业的答案
     */
    public void updateAnswer(String uuid, String answer) {
        HomeworkPojo pojo = homeworkDao.selectById(uuid);
        if(pojo == null) return;
        pojo.setAnswer(answer);
        homeworkDao.updateById(pojo);
    }

    /**
     * 根据UUID查询作业
     */
    @Nullable
    public HomeworkPojo getHomeworkByUUID(String uuid) {
        return homeworkDao.selectById(uuid);
    }

    /**
     * 根据GroupHomeworkUUID和UserUUID查询作业
     */
    @Nullable
    public HomeworkPojo getHomeworkByTwoUUID(String groupHomeworkUUID, String userUUID) {
        return homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().eq("groupHomeworkUUID", groupHomeworkUUID).eq("userUUID", userUUID));
    }

    /**
     * 根据GroupHomeworkUUID和UserUUID查询UUID
     */
    public String getUUIDByTwoUUID(String groupHomeworkUUID, String userUUID) {
        HomeworkPojo pojo = homeworkDao.selectOne(new QueryWrapper<HomeworkPojo>().eq("groupHomeworkUUID", groupHomeworkUUID).eq("userUUID", userUUID));
        return (pojo == null ? "" : pojo.getUuid());
    }

    /**
     * 查询所有作业
     */
    public List<HomeworkPojo> getAllHomeworks() {
        return homeworkDao.selectList(new QueryWrapper<>());
    }

}
