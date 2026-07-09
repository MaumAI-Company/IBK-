package kr.co.ibk.domain.web;

import kr.co.ibk.domain.enums.FileDvType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FileInfo {
    private Long flPid;
    private FileDvType dvTy;                // 구분 타입
    private String flNm;		        // 원본파일명(확장자포함)
    private String chgFlNm;		        // 서버파일명(확장자포함)
    private Long flSz;                  // 파일 사이즈
    private Long flSno;                  // 파일 순번
    private String flExtsn;		        // 확장자명(.포함)
    private String flPth;		        // 웹서비스를 위한 루트경로(디렉토리) ex)/upload
    private LocalDateTime regDtHms;     // 등록일
    private Long dataPid;             // 데이터Pid
    private String tableNm;             // 테이블명
    private LocalDateTime regDtm;

}
