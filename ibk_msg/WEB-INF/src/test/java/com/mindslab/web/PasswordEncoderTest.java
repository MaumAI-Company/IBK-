package com.mindslab.web;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.mindslab.WebApplication;
import com.mindslab.web.config.EncryptConfig;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(classes = WebApplication.class, properties = "spring.profiles.active:local")
@ActiveProfiles("local")
@Slf4j
public class PasswordEncoderTest {

    @Autowired
    private EncryptConfig encryptConfig;

    @Test
    public void encode() throws Exception {// 암호화 테스트
        // setUp();
        String defaultPassword = "ibk!1234";
        String defaultEncPassword = encryptConfig.passwordEncoder("bcrypt").encode(defaultPassword);
        
        String password = "user";
        String encPassword = encryptConfig.passwordEncoder("bcrypt").encode(password);
        
        String adminPassword = "admin";
        String adminEncPassword = encryptConfig.passwordEncoder("bcrypt").encode(adminPassword);
        String adminEncPassword2 = encryptConfig.passwordEncoder("bcrypt").encode(adminPassword);

        assertNotNull(encPassword);

        log.info("##### default password :: " + defaultPassword);
        log.info("##### default encPassword :: " + defaultEncPassword);

        log.info("##### user password :: " + password);
        log.info("##### user encPassword :: " + encPassword);
        
        log.info("##### admin password :: " + adminPassword);
        log.info("##### admin encPassword :: " + adminEncPassword);
        log.info("##### admin encPassword2 :: " + adminEncPassword2);
        
        log.info("##### admin encPassword matches :: " + encryptConfig.passwordEncoder("bcrypt").matches(adminPassword, "{bcrypt}$2a$10$9zksb9cX8KJ1TDstss8icezBRyTloyt/XzjkwivKje54Qp.DX2IgK"));
        log.info("##### admin encPassword2 matches :: " + encryptConfig.passwordEncoder("bcrypt").matches(adminPassword, "{bcrypt}$2a$10$mU9g7WhcBVAElvHNQY5zgeGS1SRdmX5qHETuuDj2nAbtFRGaD74WC"));
        
    }

}
