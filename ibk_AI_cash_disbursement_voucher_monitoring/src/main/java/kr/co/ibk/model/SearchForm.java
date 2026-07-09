package kr.co.ibk.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
public class SearchForm {
    private Integer pageSize;
    private Integer page;
    private Integer perPageNum;
    private Integer curPage;
    private String srchOffsetAt;


    private String regPsId;
    private LocalDateTime regDtm;
    private String updPsId;
    private LocalDateTime updDtm;
    private String delAt;
    private String useAt;

    private String srchRole;
    private String srchWord;
    private String srchType;
    private String srchField;
    private String srchUseAt;  // 사용여부
    private String srchDelAt;  // 삭제여부
    private String srchTableNm;  // 테이블명

    private String srchStDt;  // 시작일
    private String srchEdDt;  // 종료일
    private String srchDtStr;  // 기간검색

    private Long srchPrntCodePid;  // 공통코드 부모pid
    private Long srchCodePid;  // 공통코드 pid

}
