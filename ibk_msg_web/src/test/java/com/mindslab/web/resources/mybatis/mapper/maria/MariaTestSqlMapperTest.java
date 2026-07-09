package com.mindslab.web.resources.mybatis.mapper.maria;

import com.mindslab.WebApplication;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaTestSqlMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
public class MariaTestSqlMapperTest {
    @Autowired
    private MariaTestSqlMapper mapper;
    private String msg;

    @Test
    public void testGetInfo() {
        log.info("TEST");
        try {
            CustomMap cMap = mapper.getDate();
            msg = "############# cMap" + cMap.toString();
            msg += "############# cMap.get(\"sysDate\") :" + cMap.get("sysDate");

            log.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
