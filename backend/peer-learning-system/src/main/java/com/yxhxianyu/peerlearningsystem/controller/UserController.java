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

    @RequestMapping(value = "/api/register", method = RequestMethod.POST)
    public String register(@RequestParam("username") String username,
                           @RequestParam("password") String password,
                           @RequestParam("email") String email,
                           @RequestParam("isTeacher") boolean isTeacher) {
        String encodedPassword = Util.passwordEncoder(password);

        String uuid;
        if(isTeacher) {
            uuid = userService.insertUser(username, encodedPassword, email, 1);
        } else {
            uuid = userService.insertUser(username, encodedPassword, email, 0);
        }

        if(uuid.startsWith("ERROR")) {
            return Util.getResponse(422, uuid);
        } else {
            return Util.getOkResponse("注册成功");
        }
    }

    @RequestMapping(value = "/api/login", method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {
        String encodedPassword = Util.passwordEncoder(password);

        UserPojo user = userService.getUserByName(username);

        if(user == null) {
            return Util.getResponse(404, "用户不存在");
        } else if(!user.getPassword().equals(encodedPassword)) {
            return Util.getResponse(401, "密码错误");
        } else {
            String token = Util.tokenEncoder(username, password);
            return Util.getOkResponse(
                    "登陆成功，请使用data.token中的身份验证",
                    new HashMap<String, String>() {{
                        put("token", token);
                    }}
            );
        }
    }
}
