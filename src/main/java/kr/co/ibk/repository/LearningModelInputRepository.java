package kr.co.ibk.repository;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LearningModelInputRepository {
    public long insertList(@Param("id") Integer id, @Param("inputArr") String[] inputArr);
}
