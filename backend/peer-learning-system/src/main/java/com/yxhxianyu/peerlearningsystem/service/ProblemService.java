package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yxhxianyu.peerlearningsystem.dao.ProblemDao;
import com.yxhxianyu.peerlearningsystem.pojo.ProblemPojo;
import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Problem表的服务
 * @author YXH_XianYu
 * @date 2023/10/7 23:51
 **/
@Service
public class ProblemService {

    @Autowired
    ProblemDao problemDao;

    /**
     * 插入一个新的Problem
     */
    public void insertProblem(String name, String content, String standardAnswer) {
        String uuid = UUID.randomUUID().toString();
        problemDao.insert(new ProblemPojo(uuid, name, content, standardAnswer));
    }

    // 不支持删除

    /**
     * 根据UUID修改problem的名字
     */
    public void updateProblemName(String uuid, String name) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        problemPojo.setName(name);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID修改problem的内容
     */
    public void updateProblemContent(String uuid, String content) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        problemPojo.setContent(content);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID修改problem的标准答案
     */
    public void updateProblemStandardAnswer(String uuid, String standardAnswer) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        problemPojo.setStandardAnswer(standardAnswer);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID查询problem
     */
    public ProblemPojo getProblemByUUID(String uuid) {
        return problemDao.selectById(uuid);
    }

    /**
     * 根据名字查询problem
     */
    public ProblemPojo getProblemByName(String name) {
        return problemDao.selectOne(new QueryWrapper<ProblemPojo>().eq("name", name));
    }

    /**
     * 查询所有problems
     */
    public List<ProblemPojo> getAllProblems() {
        return problemDao.selectList(new QueryWrapper<>());
    }

}
