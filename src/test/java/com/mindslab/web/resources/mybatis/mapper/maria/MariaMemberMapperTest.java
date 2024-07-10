package com.mindslab.web.resources.mybatis.mapper.maria;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindslab.WebApplication;
import com.mindslab.web.mapper.maria.MariaMemberMapper;
import com.mindslab.web.vo.MemberVO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
public class MariaMemberMapperTest {
    @Autowired
    private MariaMemberMapper mariaMemberMapper;

    //@Test
    public void testGetInfo() {
        log.info("TEST");
        MemberVO memberVO = mariaMemberMapper.getUserInfo("admin");
        assertNotNull(memberVO);
        log.info("############# " + memberVO.toString());
        memberVO.getRoleList().forEach(roleVO -> log.debug(roleVO.toString()));
    }

}
