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
public class CardLearningDataInfo {
    private String brcd; //부점코드
    private String hdqrBobDcd; //영업점
    private String bdmnItexMngmNo; //예산관리비목관리번호
    private String bdgtBsnsFrcsCon; //예산사업ID내용
    private String bdgtPrfrRsnFrcsCon; //비목집행사유내용(비목-집행사유코드)
    private LocalDateTime frrgTs; //최초등록일시
    private String lsmdEmn; //최종변경직원번호

    private String bdgtPrfrYm; //예산집행년월
    private String bdgtPrfrNo; //예산집행번호
    private String cdn; //카드번호
    private String bdgtTstmUseHms; //예산증빙사용시각
    private String afstNm; //가맹점명
    private String bzdyYn; //영업일여부
    private Integer amslAmt; //매출금액
    private String tpbsNm; //업종명
    private String afstBzn; //가맹점사업자등록번호
    private String amslAfstNo; //매출가맹점번호
    private String afstTpbcd; //가맹점업종코드
    private String afstDtlAdr; //가맹점상세주소
    private String brncAdr; //부점주소
    private String rcvYmd; //수신년월일
    private String jobYmd; //작업년월일
    private String jobYn; //작업여부
    private String frrgEmn; //최초등록번호
    private LocalDateTime lsmdTs; //최종변경일시
}


