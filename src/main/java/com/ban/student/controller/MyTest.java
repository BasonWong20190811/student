package com.ban.student.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class MyTest {
    @ResponseBody
    @GetMapping("/test")
    public Map<String,Object> test(){
        Map<String,Object> map = new HashMap<>();
        map.put("a", "a");
        map.put("b", new Date());
        return map;
    }
}
