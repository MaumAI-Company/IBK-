package com.mindslab.web.tests;

import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaTestSqlMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MybatisMariaTestController {
    
    @Autowired
    private MariaTestSqlMapper mapper;

    @GetMapping("/test/getDate")
    public CustomMap getDate() throws Exception{
        return mapper.getDate();
    }
}
