package kr.co.ibk.domain.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BillLearningDataInfo {
    private String brcd; //부점코드
    private String bdgtPrfrYm; //예산집행년월
    private String bdgtPrfrNo; //예산집행번호
    private String bdgtPrfrYmd; //집행년월일
    private String baseYm; //기준년월
    private String issSrn; //발행일련번호
    private String txblSrn; //세금계산서일련번호
    private String hdqrBobDcd; //영업점
    private String txblDcd; //세금계산서구분코드 (S 세금계산서, K 계산서)
    private String splrBsnnNo; //공급자사업자번호
    private String splrFrm; //공급자상호명
    private String splrBzstNm; //공급자업태명
    private String splrItmsNm; //공급자종목명
    private Integer issAmt; //발행금액
    private String txblLsarNm; //세금계산서품목명
    private String bdmnItexMngmNo; //예산관리비목관리번호(비목코드)
    private String bdgtBsnsFrcsCon; //예산사업ID내용(사업-세부사업)
    private String bdgtPrfrRsnFrcsCon; //비목집행사유내용(비목-집행사유코드)
    private String rcvYmd; //수신년월일
    private String jobYmd; //작업년월일
    private String jobYn; //작업여부
    private String frrgTs; //최초등록일시
    private String frrgEmn; //최초등록번호
    private LocalDateTime lsmdTs; //최종변경일시
    private String lsmdEmn; //최종변경직원번호
    private String bdgtExnsPamtMcd; // 예산경비지급방법코드
    private String acimCon; // 계좌정보내용
}


