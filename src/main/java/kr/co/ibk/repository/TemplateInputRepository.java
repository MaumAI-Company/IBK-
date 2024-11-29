package kr.co.ibk.repository;

import kr.co.ibk.domain.web.TemplateInputInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateInputRepository {
    long insertList(@Param("id") Integer id, @Param("dataArr") String[] dataArr, @Param("gbn") String gbn);

    List<TemplateInputInfo> getPartList(@Param("id") Integer id, @Param("gbn") String gbn);
}
