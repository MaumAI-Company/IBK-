package com.mindslab.web.mapper.maria;

import com.mindslab.web.common.support.utils.CustomMap;

import org.springframework.stereotype.Repository;

@Repository
public interface MariaTestSqlMapper {

    /**
     * Maria DB 연동 테스트
     * 
     * @param null
     * @return
     * @throws Exception
     */ 
    public CustomMap getDate() throws Exception;
    
}
