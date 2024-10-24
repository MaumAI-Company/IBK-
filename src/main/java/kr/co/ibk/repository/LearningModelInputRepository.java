package kr.co.ibk.repository;

import kr.co.ibk.domain.web.LearningModelInputInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningModelInputRepository {

    public List<LearningModelInputInfo> getList(@Param("id") Integer id);
    public long insertList(@Param("id") Integer id, @Param("inputArr") String[] inputArr);
}
