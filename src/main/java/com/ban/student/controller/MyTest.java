package com.ban.student.controller;

import com.ban.student.mapper.UserInfoMapper;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/test")
public class MyTest {
    @Autowired
    UserInfoMapper mapper ;
    @ResponseBody
    @GetMapping("/test")
    public List<Map<String,Object>> test(){
        List<Map<String,Object>> map1 = mapper.selectPeriod();
        //创建表
        for(int a=0 ;a<map1.size();a++){
            Map<String, Object> map = map1.get(a);
            String period = MapUtils.getString(map, "period");
            if(period.compareTo("2019-10")<=0){
                continue;
            }
            Map<String,String> ss = new HashMap<>();
            ss.put("table","`at_fixedassets_periodversion"+"_"+period+"`");
            ss.put("period",period);
            System.out.println("================》》》》"+period);
            try{
                mapper.insertPeriod(ss);
            }catch (Exception e){
                System.out.println(e);
            }
            System.out.println("===============结束=================");


        }
        return map1;
    }
}
