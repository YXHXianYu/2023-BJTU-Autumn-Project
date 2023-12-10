package com.yxhxianyu.peerlearningsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yxhxianyu.peerlearningsystem.dao.*;
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
import com.yxhxianyu.peerlearningsystem.utils.algo.TaskAllocationAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.min;

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

    @Autowired
    HomeworkDao homeworkDao;

    @Autowired
    GroupHomeworkDao groupHomeworkDao;

    @Autowired
    ProblemDao problemDao;

    @Autowired
    RatingDao ratingDao;

    @Autowired
    UserDao userDao;

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

        System.out.println("uuid:" + uuid);

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

    /**
     * 获取所有用户信息
     */
    @RequestMapping(value = "/api/user/get_all", method = RequestMethod.POST)
    public String getAllUsers(@RequestParam("token") String token) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_STUDENT);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        return Util.getOkResponse("获取成功", userService.getAllUsers());
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
     * 添加一个小组作业，并将指定成员添加进该小组作业
     */
    @RequestMapping(value = "/api/create_group_homework", method = RequestMethod.POST)
    public String createGroupHomework(@RequestParam("token") String token,
                                      @RequestParam("groupHomeworkName") String groupHomeworkName,
                                      @RequestParam("problemName") String problemName,
                                      @RequestParam("submitDeadline") String submitDeadline,
                                      @RequestParam("ratingDeadline") String ratingDeadline,
                                      @RequestParam("selectType") boolean selectType,
                                      @RequestParam("students") String studentsString,
                                      @RequestParam("selectedHomework") String selectedHomework) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        // System.out.println(submitDeadline);
        Date submitDeadlineDate;
        try {
            submitDeadlineDate = Date.valueOf(submitDeadline.substring(0, 10));
        } catch (IllegalArgumentException e) {
            return Util.getResponse(422, "提交截止日期格式错误");
        }
        Date ratingDeadlineDate;
        try {
            ratingDeadlineDate = Date.valueOf(ratingDeadline.substring(0, 10));
        } catch (IllegalArgumentException e) {
            return Util.getResponse(422, "评分截止日期格式错误");
        }

        ProblemPojo problem = problemService.getProblemByName(problemName);
        if(problem == null) {
            return Util.getResponse(422, "题目不存在");
        }
        String problemUUID = problem.getUuid();

        String[] students;
        if(selectType) {
            System.out.println("添加小组作业，选择类型为 \" 快速选择 \"，被选择作业为: " + selectedHomework);
            List<String> studentsInList = homeworkService.getAllStudentsUUIDByGroupHomeworkUUID(selectedHomework);
            students = new String[studentsInList.size()];
            for(int i = 0; i < studentsInList.size(); i++) {
                students[i] = studentsInList.get(i);
            }
        } else {
            System.out.println("添加小组作业，选择类型为 \" 手动选择 \"");
            ObjectMapper mapper = new ObjectMapper();
            try {
                students = mapper.readValue(studentsString, String[].class);
            } catch (Exception e) {
                return Util.getResponse(422, "学生名单格式错误");
            }
        }

        if(students.length == 0) {
            return Util.getResponse(422, "学生名单为空");
        }

        String uuid = groupHomeworkService.insertGroupHomework(groupHomeworkName, problemUUID, submitDeadlineDate, ratingDeadlineDate);
        if(uuid.isEmpty()) {
            return Util.getResponse(422, "添加失败");
        } else {
            System.out.print("添加小组作业，学生人数: " + students.length + "; 为: ");
            for(String student : students) {
                homeworkService.insertHomework(uuid, student);
                System.out.print(student + " ");
            }
            System.out.println();
            return Util.getOkResponse("添加成功");
        }
    }

    /**
     * 获取所有小组作业
     */
    @RequestMapping(value = "/api/group_homework/get_all", method = RequestMethod.POST)
    public String getAllGroupHomeworks(@RequestParam("token") String token) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        return Util.getOkResponse("获取成功", groupHomeworkService.getAllGroupHomeworks());
    }

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
     * 查询本用户需要完成的所有任务
     */
    @RequestMapping(value = "/api/homework/get_all_user", method = RequestMethod.POST)
    public String getThisUserAllHomeworks(@RequestParam("token") String token) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }
        return Util.getOkResponse("获取成功", homeworkService.getAllHomeworksByUserName(user.getUsername()));
    }

    /**
     * 查询本用户需要完成的所有任务和题目内容
     */
    @RequestMapping(value = "/api/homework/get_all_user_with_problem", method = RequestMethod.POST)
    public String getThisUserAllHomeworksAndProblems(@RequestParam("token") String token) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }
        List<HomeworkPojo> homeworks = homeworkService.getAllHomeworksByUserName(user.getUsername());

        List<HashMap<String, String>> result = new java.util.ArrayList<>();
        for (HomeworkPojo homework : homeworks) {
            GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByUUID(homework.getGroupHomeworkUUID());
            if(groupHomework == null) { return Util.getResponse(430, "奇妙♂的错误"); }

            ProblemPojo problem = problemService.getProblemByUUID(groupHomework.getProblemUUID());
            if (problem == null) { return Util.getResponse(430, "奇妙♂的错误"); }

            String homeworkUUID = homework.getUuid();
            result.add(new HashMap<String, String>() {{
                put("homework", homeworkUUID);
                put("problem", problem.getUuid());
                put("problemName", problem.getName());
                put("problemContent", problem.getContent());
                put("problemStandardAnswer", problem.getStandardAnswer());
                put("groupHomeworkName", groupHomework.getName());
                put("submitDeadline", groupHomework.getSubmitDeadline().toString());
                put("ratingDeadline", groupHomework.getRatingDeadline().toString());
                put("answer", homework.getAnswer());
                put("score", homework.getCheckedScore().toString());
            }});
        }
        return Util.getOkResponse("获取成功", result);
    }

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
     * @param answer 答案
     * @return 结果
     */
    @RequestMapping(value = "/api/homework/set_answer", method = RequestMethod.POST)
    public String setHomeworkAnswer(@RequestParam("token") String token,
                                    @RequestParam("groupHomeworkName") String groupHomeworkName,
                                    @RequestParam("answer") String answer) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_STUDENT);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        HomeworkPojo homeworkPojo = homeworkService.getHomeworkByTwoName(groupHomeworkName, user.getUsername());

        if(homeworkPojo == null) {
            return Util.getResponse(422, "作业不存在");
        } else {
            homeworkService.updateAnswer(homeworkPojo.getUuid(), answer);
            return Util.getOkResponse("修改成功");
        }
    }

    /**
     * 设置一条作业记录是否优秀
     * @param token 提出请求的用户token
     * @param groupHomeworkName 小组作业名
     * @param studentUUID 学生UUID
     * @return 结果
     */
    @RequestMapping(value = "/api/excellent/negation", method = RequestMethod.POST)
    public String setHomeworkIsExcellent(@RequestParam("token") String token,
                                         @RequestParam("groupHomeworkName") String groupHomeworkName,
                                         @RequestParam("studentUUID") String studentUUID) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomework == null) {
            return Util.getResponse(422, "小组作业不存在");
        }
        HomeworkPojo homework = homeworkService.getHomeworkByTwoUUID(groupHomework.getUuid(), studentUUID);
        if(homework == null) {
            return Util.getResponse(422, "作业不存在");
        }

        homeworkService.setIsExcellent(homework.getUuid(), !homework.getIsExcellentHomework()); // 取反
        return Util.getOkResponse("设置成功");
    }


    /**
     * 获取该GroupHomework的所有优秀作业（只包括答案），并且以列表的方式返回
     */
    @RequestMapping(value = "/api/excellent/get", method = RequestMethod.POST)
    public String getExcellentHomeworks(@RequestParam("token") String token,
                                        @RequestParam("groupHomeworkName") String groupHomeworkName) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }

        GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomework == null) {
            return Util.getResponse(422, "小组作业不存在");
        }

        List<HomeworkPojo> homeworks = homeworkService.getAllExcellentHomeworksByGroupHomeworkUUID(groupHomework.getUuid());
        List<HashMap<String, String>> resultHomeworks = new java.util.ArrayList<>();
        for(HomeworkPojo homework : homeworks) {
            resultHomeworks.add(new HashMap<String, String>() {{
                put("answer", homework.getAnswer());
            }});
        }
        return Util.getOkResponse("获取成功", resultHomeworks);
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

    /**
     * 申请复查
     * @param token 提出请求的用户token
     * @param homeworkUUID 需要修改的UUID
     */
    @RequestMapping(value = "/api/homework/request_recheck", method = RequestMethod.POST)
    public String requestRecheck(@RequestParam("token") String token,
                                 @RequestParam("homeworkUUID") String homeworkUUID,
                                 @RequestParam("recheckReason") String recheckReason,
                                 @RequestParam("recheckDetailedReason") String recheckDetailedReason) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_STUDENT);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        HomeworkPojo homework = homeworkService.getHomeworkByUUID(homeworkUUID);
        if (homework == null) {
            return Util.getResponse(422, "作业不存在");
        }
        if (!(homework.getUserUUID().equals(user.getUuid()))) {
            return Util.getResponse(422, "不能申请复查别人的作业");
        }
        if (homework.getCheckedScore() < 0) {
            return Util.getResponse(422, "作业未被互评");
        }
        homeworkService.setIsNeedToCheck(homeworkUUID, true, recheckReason, recheckDetailedReason);
        return Util.getOkResponse("申请成功");
    }

    /* ----- ----- Rating ----- ----- */

    /**
     * 修改一条评分记录
     */
    @RequestMapping(value = "/api/rating/add", method = RequestMethod.POST)
    public String addRating(@RequestParam("token") String token,
                            @RequestParam("ratingUUID") String ratingUUID,
                            @RequestParam("score") float score) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_STUDENT);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        RatingPojo ratingPojo = ratingService.getRatingByUUID(ratingUUID);
        if(ratingPojo == null) {
            return Util.getResponse(422, "评分不存在");
        }
        if(ratingPojo.getScore() != -1.0f) {
            return Util.getResponse(422, "评分已经提交");
        }

        ratingService.updateScore(ratingUUID, score);
        return Util.getOkResponse("修改成功");
    }

    /**
     * 给一个GroupHomework创建互评任务
     */
    @RequestMapping(value = "/api/rating/create", method = RequestMethod.POST)
    public String createRatingTask(@RequestParam("token") String token,
                                   @RequestParam("groupHomeworkName") String groupHomeworkName,
                                   @RequestParam("ratingNumber") int ratingNumber) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        GroupHomeworkPojo groupHomeworkPojo = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomeworkPojo == null) {
            return Util.getResponse(422, "小组作业不存在");
        }

        if(groupHomeworkPojo.getState() == 1) {
            return Util.getResponse(422, "小组作业已经发布");
        }
        if(groupHomeworkPojo.getState() == 2) {
            return Util.getResponse(422, "小组作业已经截至");
        }

        List<HomeworkPojo> allHomeworks = homeworkService.getAllHomeworksByGroupHomeworkUUID(groupHomeworkPojo.getUuid());
        int n = allHomeworks.size();
        int m = min(n - 1, ratingNumber);

        List<List<Integer>> ratingTable = TaskAllocationAlgorithm.allocateTasksRandom(n, m);

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < m; j++) {
                int ratingUsernameIndex = ratingTable.get(i).get(j);
                String ratingUserUUID = allHomeworks.get(ratingUsernameIndex - 1).getUserUUID();
                ratingService.insertRating(allHomeworks.get(i).getUuid(), ratingUserUUID, -1.0f);
            }
        }

        groupHomeworkPojo.setState(1);
        groupHomeworkService.updateGroupHomework(groupHomeworkPojo);

        return Util.getOkResponse("创建成功");
    }


    /**
     * 获取一个用户需要完成的所有互评任务和对应互评任务的题目内容
     */
    @RequestMapping(value = "/api/rating/get_all_rating_and_problem", method = RequestMethod.POST)
    public String getThisUserAllRatingsAndProblems(@RequestParam("token") String token) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }
        List<RatingPojo> ratings = ratingService.getAllRatingsByUserUuid(user.getUuid());

        List<HashMap<String, String>> result = new java.util.ArrayList<>();
        for (RatingPojo rating : ratings) {
            HomeworkPojo homework = homeworkService.getHomeworkByUUID(rating.getHomeworkUUID());
            if(homework == null) { return Util.getResponse(430, "奇妙♂的错误"); }

            GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByUUID(homework.getGroupHomeworkUUID());
            if(groupHomework == null) { return Util.getResponse(430, "奇妙♂的错误"); }

            ProblemPojo problem = problemService.getProblemByUUID(groupHomework.getProblemUUID());
            if (problem == null) { return Util.getResponse(430, "奇妙♂的错误"); }

            String homeworkUUID = homework.getUuid();
            result.add(new HashMap<String, String>() {{
                put("uuid", rating.getUuid());
                put("groupHomeworkName", groupHomework.getName());
                put("problemName", problem.getName());
                put("problemContent", problem.getContent());
                put("problemStandardAnswer", problem.getStandardAnswer());
                put("answer", homework.getAnswer());
                put("score", String.valueOf(rating.getScore()));
            }});
        }
        return Util.getOkResponse("获取成功", result);
    }


    /**
     * 查看一个GroupHomework所有的学生和每个学生被互评任务的得分平均值
     */
    @RequestMapping(value = "/api/rating/get_all_students_and_score", method = RequestMethod.POST)
    public String getAllRatings(@RequestParam("token") String token,
                                @RequestParam("groupHomeworkName") String groupHomeworkName) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result_auth = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result_auth instanceof String) { return (String) result_auth; }

        GroupHomeworkPojo groupHomeworkPojo = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomeworkPojo == null) {
            return Util.getResponse(422, "小组作业不存在");
        }

        List<HomeworkPojo> allHomeworks = homeworkService.getAllHomeworksByGroupHomeworkUUID(groupHomeworkPojo.getUuid());
        List<HashMap<String, String>> result = new java.util.ArrayList<>();
        for (HomeworkPojo homework : allHomeworks) {
            String userUUID = homework.getUserUUID();
            String username = userService.getUserByUUID(userUUID).getUsername();
            float averageScore = homeworkService.getAverageScoreByHomeworkUuidAndUserUuid(homework.getUuid(), userUUID);
            float checkedScore = homeworkService.getCheckedScoreByUUID(homework.getUuid());
            float haveRatingRatio = ratingService.getRatingRatioByHomeworkUUID(homework.getUuid());
            result.add(new HashMap<String, String>() {{
                put("username", username);
                put("userUUID", userUUID);
                put("groupHomeworkName", groupHomeworkName);
                put("averageScore", String.format("%.2f", averageScore));
                put("checkedScore", String.format("%.2f", checkedScore));
                put("haveRatingRatio", String.format("%.2f", haveRatingRatio));
                put("isNeedToCheck", (homework.getIsNeedToCheck() ? "是" : "否"));
                put("isExcellent", (homework.getIsExcellentHomework() ? "是" : "否"));
            }});
        }
        return Util.getOkResponse("获取成功", result);
    }

    /**
     * 根据GroupHomeworkUUID，截至一个互评任务
     */
    @RequestMapping(value = "/api/rating/finish", method = RequestMethod.POST)
    public String finishRating(@RequestParam("token") String token,
                               @RequestParam("groupHomeworkName") String groupHomeworkName) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result_auth = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result_auth instanceof String) { return (String) result_auth; }

        GroupHomeworkPojo groupHomeworkPojo = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomeworkPojo == null) {
            return Util.getResponse(422, "小组作业不存在");
        }

        if(groupHomeworkPojo.getState() == 0) {
            return Util.getResponse(422, "小组作业尚未发布");
        }
        if(groupHomeworkPojo.getState() == 2) {
            return Util.getResponse(422, "小组作业已经截至");
        }

        List<HomeworkPojo> allHomeworks = homeworkService.getAllHomeworksByGroupHomeworkUUID(groupHomeworkPojo.getUuid());
        for (HomeworkPojo homework : allHomeworks) {
            String userUUID = homework.getUserUUID();
            float averageScore = homeworkService.getAverageScoreByHomeworkUuidAndUserUuid(homework.getUuid(), userUUID);
            homeworkService.updateCheckedScore(homework.getUuid(), averageScore);
        }

        groupHomeworkService.updateState(groupHomeworkPojo.getUuid(), 2);

        return Util.getOkResponse("截至成功");
    }

    /**
     * 复查信息获取
     */
    @RequestMapping(value = "/api/recheck/get", method = RequestMethod.POST)
    public String recheckGet(@RequestParam("token") String token,
                             @RequestParam("groupHomeworkName") String groupHomeworkName,
                             @RequestParam("studentUserUUID") String studentUserUUID) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result_auth = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result_auth instanceof String) { return (String) result_auth; }
        
        UserPojo student = userService.getUserByUUID(studentUserUUID);
        if(student == null) {
            return Util.getResponse(422, "学生不存在");
        }
        HomeworkPojo homework = homeworkService.getHomeworkByTwoName(groupHomeworkName, student.getUsername());
        if(homework == null) {
            return Util.getResponse(422, "作业不存在");
        }
        GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByUUID(homework.getGroupHomeworkUUID());
        if(groupHomework == null) {
            return Util.getResponse(422, "小组作业不存在");
        }
        ProblemPojo problem = problemService.getProblemByUUID(groupHomework.getProblemUUID());
        if(problem == null) {
            return Util.getResponse(422, "题目不存在");
        }

        HashMap<String, String> result = new HashMap<String, String>() {{
            put("homeworkName", groupHomeworkName);
            put("problemName", problem.getName());
            put("problemContent", problem.getContent());
            put("standardAnswer", problem.getStandardAnswer());
            put("answer", homework.getAnswer());
            put("recheckReason", homework.getRecheckReason());
            put("recheckDetailedReason", homework.getRecheckDetailedReason());
        }};
        return Util.getOkResponse("获取成功", result);
    }

    /**
     * 复查信息设置（设置分数）
     */
    @RequestMapping(value = "/api/recheck/set", method = RequestMethod.POST)
    public String recheckSet(@RequestParam("token") String token,
                             @RequestParam("groupHomeworkName") String groupHomeworkName,
                             @RequestParam("studentUserUUID") String studentUserUUID,
                             @RequestParam("score") float score) {
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        Object result_auth = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result_auth instanceof String) { return (String) result_auth; }

        UserPojo student = userService.getUserByUUID(studentUserUUID);
        if(student == null) {
            return Util.getResponse(422, "学生不存在");
        }
        HomeworkPojo homework = homeworkService.getHomeworkByTwoName(groupHomeworkName, student.getUsername());
        if(homework == null) {
            return Util.getResponse(422, "作业不存在");
        }
        GroupHomeworkPojo groupHomework = groupHomeworkService.getGroupHomeworkByUUID(homework.getGroupHomeworkUUID());
        if(groupHomework == null) {
            return Util.getResponse(422, "小组作业不存在");
        }
        ProblemPojo problem = problemService.getProblemByUUID(groupHomework.getProblemUUID());
        if(problem == null) {
            return Util.getResponse(422, "题目不存在");
        }

        homeworkService.updateCheckedScore(homework.getUuid(), score);
        return Util.getOkResponse("修改成功");
    }

    /* ----- ----- Algorithm ----- ----- */

    /**
     * 互评任务分配算法（随机性构造性算法）
     * @return 映射关系
     */
    @RequestMapping(value = "/api/algo/random", method = RequestMethod.POST)
    public String random(@RequestParam("token") String token,
                         @RequestParam("groupHomeworkName") String groupHomeworkName,
                         @RequestParam("ratingNumber") int ratingNumber) {
        // ===== 权限验证基本步骤 开始 (输入 token; 输出 user实体 & 错误信息) =====
        // 通过token得到user实体
        UserPojo user = userService.getUserByToken(token);
        if(user == null) { return Util.getResponse(401, "用户未登录"); }

        // 通过user实体来检验权限
        Object result = Util.checkAuthority(user.getAuthority(), Util.AUTHORITY_TEACHER);
        if(result instanceof String) { return (String) result; }
        // ===== 权限验证基本步骤 结束 =====

        GroupHomeworkPojo groupHomeworkPojo = groupHomeworkService.getGroupHomeworkByName(groupHomeworkName);
        if(groupHomeworkPojo == null) {
            return Util.getResponse(422, "小组作业不存在");
        }

        int n = homeworkService.getAllHomeworksByGroupHomeworkUUID(groupHomeworkPojo.getUuid()).size();
        int m = min(n - 1, ratingNumber);

        return Util.getOkResponse("获取成功", TaskAllocationAlgorithm.allocateTasksRandom(n, m));
    }

}
