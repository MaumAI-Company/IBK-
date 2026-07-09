package com.mindslab.web.resources.mybatis.mapper.maria;

import java.util.List;

import com.mindslab.WebApplication;
import com.mindslab.web.common.support.utils.CustomMap;
import com.mindslab.web.mapper.maria.MariaAdminDeptMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
public class MariaDeptMapperTest {
    @Autowired
    private MariaAdminDeptMapper mapper;
    private String msg;

    @Test
    public void testGetDeptList() {
        log.info("TEST");
    	/*
        try {
            List<CustomMap> cListMap = mapper.getDeptList();
            msg = "############# cMap" + cListMap.get(0).toString();

            int i = 1;
            for (CustomMap cMap : cListMap) {
                msg += "\n############# " + i + "번 cMap.get(\"deptName\") :" + cMap.get("deptName");
                msg += "\n############# " + i + "번 cMap.get(\"deptSeq\") :" + cMap.get("deptSeq");
                msg += "\n############# " + i + "번 cMap.get(\"deptModDt\") :" + cMap.get("deptModDt");
                msg += "\n############# " + i + "번 cMap.get(\"parId\") :" + cMap.get("parId");
                msg += "\n############# " + i + "번 cMap.get(\"deptDepth\") :" + cMap.get("deptDepth");
                msg += "\n############# " + i + "번 cMap.get(\"deptOrder\") :" + cMap.get("deptOrder");
                msg += "\n############# " + i + "번 cMap.get(\"deptModId\") :" + cMap.get("deptModId");
                msg += "\n############# " + i + "번 cMap.get(\"deptId\") :" + cMap.get("deptId");
                msg += "\n############# " + i + "번 cMap.get(\"deptStat\") :" + cMap.get("deptStat");
                msg += "\n############# " + i + "번 cMap.get(\"deptRegId\") :" + cMap.get("deptRegId");
                msg += "\n############# " + i + "번 cMap.get(\"deptCode\") :" + cMap.get("deptCode");
                msg += "\n############# " + i + "번 cMap.get(\"deptRegDt\") :" + cMap.get("deptRegDt");
                msg += "\n\n";
                i++;
            }
            // msg += "############# cMap.get(\"sysDate\") :" + cMap.get("sysDate");

            log.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    @Test
    public void testGetDeptTree() {
        log.info("TEST");
        try {
            List<CustomMap> cListMap = mapper.getDeptTree();
            msg = "############# cMap" + cListMap.get(0).toString();

            int i = 1;
            for (CustomMap cMap : cListMap) {
                msg += "\n############# " + i + "번 cMap.get(\"deptNamePath\") :" + cMap.get("deptNamePath");
                msg += "\n############# " + i + "번 cMap.get(\"deptListPath\") :" + cMap.get("deptListPath");
                msg += "\n############# " + i + "번 cMap.get(\"deptIdPath\") :" + cMap.get("deptIdPath");
                msg += "\n############# " + i + "번 cMap.get(\"level\") :" + cMap.get("level");
                msg += "\n############# " + i + "번 cMap.get(\"deptName\") :" + cMap.get("deptName");
                msg += "\n############# " + i + "번 cMap.get(\"deptSeq\") :" + cMap.get("deptSeq");
                msg += "\n############# " + i + "번 cMap.get(\"deptModDt\") :" + cMap.get("deptModDt");
                msg += "\n############# " + i + "번 cMap.get(\"parId\") :" + cMap.get("parId");
                msg += "\n############# " + i + "번 cMap.get(\"deptDepth\") :" + cMap.get("deptDepth");
                msg += "\n############# " + i + "번 cMap.get(\"deptOrder\") :" + cMap.get("deptOrder");
                msg += "\n############# " + i + "번 cMap.get(\"deptModId\") :" + cMap.get("deptModId");
                msg += "\n############# " + i + "번 cMap.get(\"deptId\") :" + cMap.get("deptId");
                msg += "\n############# " + i + "번 cMap.get(\"deptStat\") :" + cMap.get("deptStat");
                msg += "\n############# " + i + "번 cMap.get(\"deptRegId\") :" + cMap.get("deptRegId");
                msg += "\n############# " + i + "번 cMap.get(\"deptCode\") :" + cMap.get("deptCode");
                msg += "\n############# " + i + "번 cMap.get(\"deptRegDt\") :" + cMap.get("deptRegDt");
                msg += "\n\n";
                i++;
            }
            // msg += "############# cMap.get(\"sysDate\") :" + cMap.get("sysDate");

            log.info(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
