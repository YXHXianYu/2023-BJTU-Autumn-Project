package com.yxhxianyu.peerlearningsystem.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yxhxianyu.peerlearningsystem.dao.ProblemDao;
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
     * 并返回该问题的UUID
     * 若insert失败，则返回空字符串
     */
    public String insertProblem(String name, String content, String standardAnswer) {
        String uuid = UUID.randomUUID().toString();
        try {
            problemDao.insert(new ProblemPojo(uuid, name, content, standardAnswer));
            return uuid;
        } catch (DuplicateKeyException e) {
            System.out.println("Insert failed: duplicate username, name is \"" + name + "\"");
        } catch (DataIntegrityViolationException e) {
            System.out.println("Insert failed: data integrity violation (maybe data is too long or foreign key not exists), name is \"" + name + "\"");
        }
        return "";
    }

    /**
     * 根据UUID删除一条问题
     * 在得到UUID时，请注意你的ProblemPojo对象非空，否则会在函数外部产生NullPointerException
     */
    public void deleteProblemByUUID(String uuid) {
        problemDao.deleteById(uuid);
    }

    /**
     * 根据名称删除一条问题
     */
    public void deleteProblemByName(String username) {
        ProblemPojo pojo = getProblemByName(username);
        if(pojo != null)
            problemDao.deleteById(pojo.getUuid());
    }

    /**
     * 根据UUID修改problem的名字
     */
    public void updateProblemName(String uuid, String name) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        if(problemPojo == null) return;
        problemPojo.setName(name);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID修改problem的内容
     */
    public void updateProblemContent(String uuid, String content) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        if(problemPojo == null) return;
        problemPojo.setContent(content);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID修改problem的标准答案
     */
    public void updateProblemStandardAnswer(String uuid, String standardAnswer) {
        ProblemPojo problemPojo = problemDao.selectById(uuid);
        if(problemPojo == null) return;
        problemPojo.setStandardAnswer(standardAnswer);
        problemDao.updateById(problemPojo);
    }

    /**
     * 根据UUID查询problem
     */
    @Nullable
    public ProblemPojo getProblemByUUID(String uuid) {
        return problemDao.selectById(uuid);
    }

    /**
     * 根据名字查询problem
     */
    @Nullable
    public ProblemPojo getProblemByName(String name) {
        return problemDao.selectOne(new QueryWrapper<ProblemPojo>().eq("name", name));
    }

    /**
     * 根据名字获取UUID
     */
    public String getUUIDByName(String name) {
        ProblemPojo pojo = problemDao.selectOne(new QueryWrapper<ProblemPojo>().eq("name", name));
        return (pojo == null ? "" : pojo.getUuid());
    }

    /**
     * 查询所有problems
     */
    public List<ProblemPojo> getAllProblems() {
        return problemDao.selectList(new QueryWrapper<>());
    }

}
