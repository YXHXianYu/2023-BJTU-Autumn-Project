package com.yxhxianyu.peerlearningsystem.utils;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * @author YXH_XianYu
 * @date 2023/10/8 23:41
 **/
public class Util {

    /* ----- ----- Http请求 ----- ----- */

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static String getResponse(int code, String message, Object data) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", code);
        map.put("message", message);
        if(data != null)
            map.put("data", data);
        return JSONObject.toJSONString(map);
    }

    /**
     * 生成一个Http请求的Response
     * @param code 状态码
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static String getResponse(int code, String message) {
        return getResponse(code, message, null);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @return 字符串形式的Response
     */
    public static String getOkResponse(String message) {
        return getResponse(200, message);
    }

    /**
     * 生成一个状态码为200的Http请求的Response
     * @param message 信息内容
     * @param data 数据
     * @return 字符串形式的Response
     */
    public static String getOkResponse(String message, Object data) {
        return getResponse(200, message, data);
    }

    /* ----- ----- 加密 ----- ----- */

    private static String ENCODER_SALT() {
        return "1145141919810"; // 哼，哼哼，啊啊啊啊啊啊啊啊啊啊啊
    }

    /**
     * 密码加密器
     * @param password 原始密码
     * @return 加密后的密码
     */
    public static String passwordEncoder(String password) {
        return DigestUtils.md5Hex(password + ENCODER_SALT());
    }

    /**
     * Token加密
     * @param username 该用户的用户名
     * @param password 该用户的密码
     * @return token
     */
    @SuppressWarnings("unused")
    public static String tokenEncoder(String username, String password) {
        return Base64.getEncoder().encodeToString(username.getBytes());
    }

    /**
     * Token解密
     * @param token token
     * @return 用户名
     */
    public static String tokenDecoder(String token) {
        return new String(Base64.getDecoder().decode(token));
    }


}
