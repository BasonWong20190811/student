package com.ban.student.mapper;

import java.util.List;
import java.util.Map;


/**
 * @author wangban
 */
public interface UserInfoMapper {

    Map<String,Object> selectone();
    List<Map<String,Object>> selectPeriod();
    void createPeriod(Map<String,String> map);
    int insertPeriod(Map<String,String> map);
}
