package com.ban.student.controller;

import com.ban.student.mapper.UserInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author wangban
 */
@Controller
@RequestMapping("/userinfo")
public class UserInfoController {

    @Autowired
    UserInfoMapper mapper ;

    @PostMapping("/getUserinfo")
    public Map<String, Object> getUserInfo() {
        mapper.selectone();
        return null;
    }
}
