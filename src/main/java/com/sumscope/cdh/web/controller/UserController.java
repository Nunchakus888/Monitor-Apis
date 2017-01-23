package com.sumscope.cdh.web.controller;

import com.sumscope.cdh.web.domain.UserInfo;
import com.sumscope.cdh.web.service.UserService;
import com.sumscope.cdh.web.util.EmailUtil;
import com.sumscope.cdh.web.util.JsonUtil;
import com.sumscope.cdh.web.util.MD5;
import com.sumscope.cdh.web.util.RegexValidateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by wenshuai.li on 2016/4/6.
 */
@RestController
@RequestMapping(value = "/user", produces = "application/json; charset=UTF-8")
public final class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailUtil emailUtil;

    @Value("${host}")
    private String host;

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request, HttpServletResponse response, @RequestBody String param) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(param);
        String password = (String) map.get("password");
        String userName = (String) map.get("userName");
        String email = (String) map.get("email");
        if (userName != "" && userName != null && password != "" && password != null && email != "" && email != null) {
            if (!RegexValidateUtil.checkEmail(email))
                return "{\"error\": \"邮箱格式不正确\"}";
            map.put("password", new MD5(password).md5());
            try {
                if (userService.register(map) == 1)
                    map.remove("password");
            } catch (Exception e) {
                e.printStackTrace();
                map.put("error", "注册失败");
            }
        } else {
            map.clear();
            map.put("error", "传参不正确");
        }
        return JsonUtil.writeValueAsString(map);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest request, HttpServletResponse response, @RequestBody String param) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(param);
        String password = (String) map.get("password");
        String userName = (String) map.get("userName");
        if (userName != "" && userName != null && password != "" && password != null) {
            UserInfo userInfo = userService.login(map);
            map.remove("password");
            if (userInfo != null && userInfo.getPassword().equals(new MD5(password).md5())) {
                map.clear();
                request.getSession().setAttribute("USER", userInfo);
            } else {
                map.put("error", "用户名或密码错误");
            }
            //Checking whether the userName is exist.
        } else if (map.size() == 1 && userName != "" && userName != null) {
            UserInfo userInfo = userService.login(map);
            if (userInfo != null) {
                map.put("error", "当前用户名已存在");
            }
        } else {
            map.put("error", "传参不正确");
        }

        return JsonUtil.writeValueAsString(map);
    }

    @RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
    public String updatePassword(HttpServletRequest request, HttpServletResponse response, @RequestBody String param) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(param);
        String password = (String) map.get("password");
        String newPassword = (String) map.get("newPassword");
        String userName = (String) map.get("userName");
        if (map.size() == 3) {
            if (userName != "" && userName != null && password != "" && password != null && newPassword != "" && newPassword != null) {
                map.put("password", new MD5(password).md5());
                UserInfo userInfo = userService.login(map);
                if (userInfo != null && userInfo.getPassword().equals(new MD5(password).md5())) {
                    map.put("password", new MD5(newPassword).md5());
                    if ((userService.updatePassword(map) != 1)) {
                        map.put("error", "修改失败");
                    } else {
                        map.clear();
                    }
                } else {
                    map.clear();
                    map.put("error", "原密码不正确");
                }
            } else {
                map.clear();
                map.put("error", "传参不正确");
            }
        } else if (map.size() == 2) {
            if (userName != "" && userName != null && password != "" && password != null) {
                if (null != (userService.login(map))) {
                    map.put("password", new MD5(password).md5());
                    if ((userService.updatePassword(map) != 1)) {
                        map.put("error", "找回失败");
                    } else {
                        map.clear();
                    }
                } else {
                    map.put("error", "当前用户不存在");
                }
            }
        }

        return JsonUtil.writeValueAsString(map);
    }

    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    public String findPassword(HttpServletRequest request, HttpServletResponse response, @RequestBody String param) {
        setResponseHeader(request, response);
        Map map = JsonUtil.json2Map(param);
        String email = (String) map.get("email");
        String userName = (String) map.get("userName");
        boolean tag = map.size() == 3 ? (Boolean) map.get("tag") : false;
        String responseMessage = null;
        if (email != "" && email != null && userName != "" && userName != null) {
            if (!RegexValidateUtil.checkEmail(email))
                return "{\"error\": \"邮箱格式不正确\"}";
            UserInfo userInfo = userService.login(map);
            if (userInfo != null && userInfo.getEmail().equals(email)) {
                if(!tag){
                    emailUtil.sendEmail(email, userName);
                }
            } else {
                responseMessage = "{\"error\": \"该邮箱未注册\"}";
            }
        } else {
            responseMessage = "{\"error\": \"邮箱不能为空\"}";
        }
        return responseMessage;
    }
}