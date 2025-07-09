package kr.co.ibk.repository;

import kr.co.ibk.domain.enums.BatchTargetType;
import kr.co.ibk.domain.web.BatchInferenceModelInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BatchInferenceModelRepository {

    int update(@Param("target") String target, @Param("modelId") Integer modelId, @Param("modId") String modId);

    int insert(@Param("target") String target, @Param("modelId") Integer modelId, @Param("modId") String modId);

    void deleteByTarget(BatchTargetType target);

    void deleteByModelId(@Param("modelId") Integer modelId);

    Optional<BatchInferenceModelInfo> findByTarget(BatchTargetType target);
}
