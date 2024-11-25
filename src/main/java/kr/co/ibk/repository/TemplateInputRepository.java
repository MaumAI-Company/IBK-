package kr.co.ibk.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateInputRepository {
    long insertList(@Param("id") Integer id, @Param("dataArr") String[] dataArr, @Param("gbn") String gbn);
}
