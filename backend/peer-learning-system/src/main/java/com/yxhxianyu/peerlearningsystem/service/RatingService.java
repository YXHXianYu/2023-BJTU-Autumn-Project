package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yxhxianyu.peerlearningsystem.dao.RatingDao;
import com.yxhxianyu.peerlearningsystem.pojo.HomeworkPojo;
import com.yxhxianyu.peerlearningsystem.pojo.RatingPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * 互评记录的服务
 * @author YXH_XianYu
 * @date 2023/10/8 23:15
 **/
@Service
public class RatingService {

    @Autowired
    RatingDao ratingDao;

    /**
     * 增加一条互评记录
     */
    public String insertRating(String homeworkUUID, String userUUID, Float score) {
        String uuid = UUID.randomUUID().toString();
        try {
            ratingDao.insert(new RatingPojo(uuid, homeworkUUID, userUUID, score));
            return uuid;
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long)");
        }
        return "";
    }

    /**
     * 删除一条互评记录
     */
    public void deleteRatingByUUID(String uuid) {
        ratingDao.deleteById(uuid);
    }

    /**
     * 更新一条互评记录的分数
     */
    public void updateScore(String uuid, Float score) {
        RatingPojo pojo = ratingDao.selectById(uuid);
        if(pojo == null) return;
        pojo.setScore(score);
        ratingDao.updateById(pojo);
    }

    /**
     * 根据作业UUID与用户UUID查询一条互评记录的UUID
     * 若找不到，则返回空字符串
     */
    public String getUUIDByTwoUUID(String homeworkUUID, String userUUID) {
        RatingPojo pojo = ratingDao.selectOne(new QueryWrapper<RatingPojo>().eq("homeworkUUID", homeworkUUID).eq("userUUID", userUUID));
        return (pojo == null ? "" : pojo.getUuid());
    }

    /**
     * 根据作业UUID与用户UUID查询一条互评记录的分数
     * 若找不到，则返回-1.0f
     */
    public Float getScoreByTwoUUID(String homeworkUUID, String userUUID) {
        RatingPojo pojo = ratingDao.selectOne(new QueryWrapper<RatingPojo>().eq("homeworkUUID", homeworkUUID).eq("userUUID", userUUID));
        return (pojo == null ? -1.0f : pojo.getScore());
    }


    /**
     * 获取所有互评记录
     */
    public List<RatingPojo> getAllRatings() {
        return ratingDao.selectList(new QueryWrapper<>());
    }

    /**
     * 根据UserUUID，获取所有需要完成的互评任务
     */
    public List<RatingPojo> getAllRatingsByUserUuid(String userUUID) {
        return ratingDao.selectList(new QueryWrapper<RatingPojo>().eq("userUUID", userUUID).eq("score", -1.0f));
    }

    /**
     * 根据RatingUUID，获取一条互评记录
     */
    public RatingPojo getRatingByUUID(String ratingUUID) {
        return ratingDao.selectById(ratingUUID);
    }

    /**
     * 根据HomeworkUUID，获取多条互评记录
     */
    public List<RatingPojo> getAllRatingsByHomeworkUUID(String homeworkUUID) {
        return ratingDao.selectList(new QueryWrapper<RatingPojo>().eq("homeworkUUID", homeworkUUID));
    }

    /**
     * 根据HomeworkUUID，查询所有分数不等于-1.0f的作业，占所有作业的比例
     */
    public float getRatingRatioByHomeworkUUID(String homeworkUUID) {
        List<RatingPojo> ratings = getAllRatingsByHomeworkUUID(homeworkUUID);
        int count = 0;
        for (RatingPojo rating : ratings) {
            if (rating.getScore() != -1.0f) {
                count += 1;
            }
        }
        return (ratings.size() == 0 ? 0.0f : (float) count / ratings.size());
    }
}
