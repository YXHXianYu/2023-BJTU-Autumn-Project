package com.yxhxianyu.peerlearningsystem.controller;

import com.yxhxianyu.peerlearningsystem.pojo.UserPojo;
import com.yxhxianyu.peerlearningsystem.service.UserService;
import com.yxhxianyu.peerlearningsystem.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @author YXH_XianYu
 * @date 2023/10/8 23:36
 **/
@RestController
public class UserController {

    @Autowired
    UserService userService;

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
}
