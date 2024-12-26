package kr.co.ibk.repository;

import kr.co.ibk.domain.web.LearningDataInputInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LearningDataInputRepository {
    long insertList(@Param("id") Integer id, @Param("dataArr") String[] dataArr, @Param("gbn") String gbn);

    void delete(@Param("id") Integer id);

    List<LearningDataInputInfo> getPartList(@Param("id") Integer id, @Param("gbn") String gbn);
}
