package com.yxhxianyu.peerlearningsystem.controller;

import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import com.yxhxianyu.peerlearningsystem.pojo.ProblemPojo;
import com.yxhxianyu.peerlearningsystem.pojo.HomeworkPojo;
import com.yxhxianyu.peerlearningsystem.pojo.GroupHomeworkPojo;
import com.yxhxianyu.peerlearningsystem.pojo.RatingPojo;
import com.yxhxianyu.peerlearningsystem.service.GroupHomeworkService;
import com.yxhxianyu.peerlearningsystem.service.HomeworkService;
import com.yxhxianyu.peerlearningsystem.service.ProblemService;
import com.yxhxianyu.peerlearningsystem.service.UserService;
import com.yxhxianyu.peerlearningsystem.service.RatingService;
import com.yxhxianyu.peerlearningsystem.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author YXH_XianYu
 **/
@RestController
public class Controller {

    /* ----- ----- Service ----- ----- */

    @Autowired
    UserService userService;

    @Autowired
    ProblemService problemService;

    @Autowired
    GroupHomeworkService groupHomeworkService;

    @Autowired
    HomeworkService homeworkService;

    @Autowired
    RatingService ratingService;

    /* ----- ----- User ----- ----- */

    /**
     * 注册学生用户
     */
    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email) {
        String encodedPassword = Util.passwordEncoder(password);

        String uuid = userService.insertUser(username, encodedPassword, email, Util.AUTHORITY_STUDENT);

        if(uuid.startsWith("ERROR")) {
            return Util.getResponse(422, uuid);
        } else {
            System.out.println("注册：" + username + " 成功");
            return Util.getOkResponse("注册成功");
        }
    }

    /**
     * 登录用户
     */
    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        String encodedPassword = Util.passwordEncoder(password);

        UserPojo user = userService.getUserByName(username);

        if(user == null) {
            System.out.println("登录：" + username + " 用户不存在");
            return Util.getResponse(404, "用户不存在");
        } else if(!user.getPassword().equals(encodedPassword)) {
            System.out.println("登录：" + username + " 密码错误");
            System.out.println("!" + user.getPassword());
            System.out.println("?" + encodedPassword);
            return Util.getResponse(401, "密码错误");
        } else {
            String token = Util.tokenEncoder(username, password);
            System.out.println("登录：" + username + " 登录成功");
            return Util.getOkResponse(
                    "登陆成功，请使用data.token中的身份验证",
                    new HashMap<String, String>() {{
                        put("token", token);
                    }}
            );
        }
    }

    /**
     * 注册一个老师权限的用户
     * @param token 管理员权限的token
     */
    @RequestMapping(value = "/api/register_teacher", method = RequestMethod.POST)
    public String registerTeacher(@RequestParam("token") String token,
                                   @RequestParam("username") String username,
                                   @RequestParam("password") String password,
                                   @RequestParam("email") String email) {
        return registerProtectedUser(token, username, password, email, Util.AUTHORITY_TEACHER, Util.NEEDED_AUTHORITY_REGISTER_TEACHER);
    }

    /**
     * 注册一个管理员权限的用户
     * @param token 管理员权限的token
     */
    @RequestMapping(value = "/api/register_administrator", method = RequestMethod.POST)
    public String registerAdministrator(@RequestParam("token") String token,
                                        @RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        @RequestParam("email") String email) {
        return registerProtectedUser(token, username, password, email, Util.AUTHORITY_ADMINISTRATOR, Util.NEEDED_AUTHORITY_REGISTER_ADMINISTRATOR);
    }

    /**
     * 注册一个受保护权限的用户
     * @param token 提出请求的用户token
     * @param username 新用户的用户名
     * @param password 新用户的密码
     * @param email 新用户的邮箱
     * @param authority 新用户的权限
     * @param neededAuthority 提出请求用户所需的权限
     * @return 结果
     */
    private String registerProtectedUser(String token,
                                         String username,
                                         String password,
                                         String email,
                                         int authority,
                                         int neededAuthority) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), neededAuthority);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        // 注册
        String encodedPassword = Util.passwordEncoder(password);
        String uuid = userService.insertUser(username, encodedPassword, email, authority);

        if(uuid.startsWith("ERROR")) {
            System.out.println("注册：" + username + " 失败");
            return Util.getResponse(422, uuid);
        } else {
            System.out.println("注册：" + username + " 成功");
            return Util.getOkResponse("注册成功");
        }
    }

    /* ----- ----- Problem ----- ----- */

    /**
     * 添加一个题目
     * @param token 提出请求的用户token
     * @param name 题目名
     * @param content 题目内容
     * @param standardAnswer 题目标准答案
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/add", method = RequestMethod.POST)
    public String addProblem(@RequestParam("token") String token,
                             @RequestParam("name") String name,
                             @RequestParam("content") String content,
                             @RequestParam("standardAnswer") String standardAnswer) {

        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        String uuid = problemService.insertProblem(name, content, standardAnswer);
        if(uuid.isEmpty()) {
            return Util.getResponse(422, "添加失败");
        } else {
            return Util.getOkResponse("添加成功");
        }
    }

    /**
     * 删除一个题目
     * @param token 提出请求的用户token
     * @param name 题目名
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/get_content", method = RequestMethod.POST)
    public String getProblemContent(@RequestParam("token") String token,
                                    @RequestParam("name") String name) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        if(problemService.deleteProblemByName(name)) {
            return Util.getOkResponse("删除成功");
        } else {
            return Util.getResponse(422, "题目不存在");
        }
    }

    /**
     * 获取一个题目的标准答案
     * @param token 提出请求的用户token
     * @param name 题目名
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/get_standard_answer", method = RequestMethod.POST)
    public String getProblemStandardAnswer(@RequestParam("token") String token,
                                           @RequestParam("name") String name) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        ProblemPojo problemPojo = problemService.getProblemByName(name);

        if(problemPojo == null) {
            return Util.getResponse(422, "题目不存在");
        } else {
            return Util.getOkResponse("获取成功", problemPojo.getStandardAnswer());
        }
    }

    /**
     * 设置一个题目的内容
     * @param token 提出请求的用户token
     * @param name 题目名
     * @param content 题目内容
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/set_content", method = RequestMethod.POST)
    public String setProblemContent(@RequestParam("token") String token,
                                    @RequestParam("name") String name,
                                    @RequestParam("content") String content) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        problemService.updateProblemContent(name, content);

        return Util.getOkResponse("修改成功");
    }

    /**
     * 设置一个题目的标准答案
     * @param token 提出请求的用户token
     * @param name 题目名
     * @param standardAnswer 题目标准答案
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/set_standard_answer", method = RequestMethod.POST)
    public String setProblemStandardAnswer(@RequestParam("token") String token,
                                           @RequestParam("name") String name,
                                           @RequestParam("standardAnswer") String standardAnswer) {

        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        problemService.updateProblemStandardAnswer(name, standardAnswer);

        return Util.getOkResponse("修改成功");
    }

    /**
     * 获取所有题目
     * @param token 提出请求的用户token
     * @return 结果
     */
    @RequestMapping(value = "/api/problem/get_all", method = RequestMethod.POST)
    public String getAllProblems(@RequestParam("token") String token) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        return Util.getOkResponse("获取成功", problemService.getAllProblems());
    }

    /**
     * 删除一个题目
     */
    @RequestMapping(value = "/api/problem/delete", method = RequestMethod.POST)
    public String deleteProblem(@RequestParam("token") String token,
                                @RequestParam("name") String name) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        if(problemService.deleteProblemByName(name)) {
            return Util.getOkResponse("删除成功");
        } else {
            return Util.getResponse(422, "题目不存在");
        }
    }


    /* ----- ----- Group Homework ----- ----- */

    /**
     * 添加一个小组作业
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param problemName 题目名
     * @param submitDeadline 提交截止日期
     * @param ratingDeadline 评分截止日期
     * @return 结果
     */
    @RequestMapping(value = "/api/group_homework/add", method = RequestMethod.POST)
    public String addGroupHomework(@RequestParam("token") String token,
                                   @RequestParam("groupHomeworkName") String groupHomeworkName,
                                   @RequestParam("problemName") String problemName,
                                   @RequestParam("submitDeadline") String submitDeadline,
                                   @RequestParam("ratingDeadline") String ratingDeadline) {
        return "TODO";
    }

    /**
     * 获取一个小组作业
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @return 结果
     */
    @RequestMapping(value = "/api/group_homework/get", method = RequestMethod.POST)
    public String getGroupHomework(@RequestParam("token") String token,
                                   @RequestParam("groupHomeworkName") String groupHomeworkName) {
        return "TODO";
    }

    /**
     * 设置一个小组作业的提交截止日期
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param submitDeadline 提交截止日期
     * @return 结果
     */
    @RequestMapping(value = "/api/group_homework/set_submit_deadline", method = RequestMethod.POST)
    public String setGroupHomeworkSubmitDeadline(@RequestParam("token") String token,
                                                 @RequestParam("groupHomeworkName") String groupHomeworkName,
                                                 @RequestParam("submitDeadline") String submitDeadline) {
        return "TODO";
    }

    /**
     * 设置一个小组作业的评分截止日期
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param ratingDeadline 评分截止日期
     * @return 结果
     */
    @RequestMapping(value = "/api/group_homework/set_rating_deadline", method = RequestMethod.POST)
    public String setGroupHomeworkRatingDeadline(@RequestParam("token") String token,
                                                 @RequestParam("groupHomeworkName") String groupHomeworkName,
                                                 @RequestParam("ratingDeadline") String ratingDeadline) {
        return "TODO";
    }

    /* ----- ----- Homework ----- ----- */

    /**
     * 添加一条作业记录
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param username 用户名
     * @return 结果
     */
    @RequestMapping(value = "/api/homework/add", method = RequestMethod.POST)
    public String addHomework(@RequestParam("token") String token,
                              @RequestParam("groupHomeworkName") String groupHomeworkName,
                              @RequestParam("username") String username) {
        return "TODO";
    }

    /**
     * 设置一条作业记录的答案
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param username 用户名
     * @param answer 答案
     * @return 结果
     */
    @RequestMapping(value = "/api/homework/set_answer", method = RequestMethod.POST)
    public String setHomeworkAnswer(@RequestParam("token") String token,
                                    @RequestParam("groupHomeworkName") String groupHomeworkName,
                                    @RequestParam("username") String username,
                                    @RequestParam("answer") String answer) {
        return "TODO";
    }

    /**
     * 设置一条作业记录是否优秀
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param username 用户名
     * @param isExcellent 是否优秀
     * @return 结果
     */
    @RequestMapping(value = "/api/homework/set_is_excellent", method = RequestMethod.POST)
    public String setHomeworkIsExcellent(@RequestParam("token") String token,
                                         @RequestParam("groupHomeworkName") String groupHomeworkName,
                                         @RequestParam("username") String username,
                                         @RequestParam("isExcellent") boolean isExcellent) {
        return "TODO";
    }

    /**
     * 获取一条作业记录的分数
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param username 用户名
     * @return 结果
     */
    @RequestMapping(value = "/api/homework/get_score", method = RequestMethod.POST)
    public String getHomeworkScore(@RequestParam("token") String token,
                                   @RequestParam("groupHomeworkName") String groupHomeworkName,
                                   @RequestParam("username") String username) {
        return "TODO";
    }

    /* ----- ----- Rating ----- ----- */

    /**
     * 添加一条评分记录
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param username 用户名
     * @param ratingUsername 评分用户名
     * @param score 分数
     * @return 结果
     */
    @RequestMapping(value = "/api/rating/add", method = RequestMethod.POST)
    public String addRating(@RequestParam("token") String token,
                            @RequestParam("groupHomeworkName") String groupHomeworkName,
                            @RequestParam("username") String username,
                            @RequestParam("ratingUsername") String ratingUsername,
                            @RequestParam("score") float score) {
        return "TODO";
    }

}
