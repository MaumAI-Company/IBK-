package kr.co.ibk.repository;

import kr.co.ibk.domain.web.FileInfo;
import kr.co.ibk.model.FileInfoForm;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileInfoRepository {
    void setInsert(FileInfo form) throws Exception;
    List<FileInfo> selectList(FileInfoForm form) throws Exception;
    void deleteByTableNmAndDataPid(FileInfoForm form) throws Exception;
    void deleteByPrimaryKeys(List<Long> pids) throws Exception;
}
