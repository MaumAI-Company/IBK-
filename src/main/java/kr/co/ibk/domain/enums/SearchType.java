package kr.co.ibk.domain.enums;

public enum SearchType {
    BRCD("부점코드"),
    CDN("카드번호"),
    BDGT_TSTM_USE_HMS("승인시간"),
    AMSL_AMT("매출금액"),
    AFST_NM("가맹점명"),
    TPBS_NM("업종명"),
    BZDY_YN("영업일여부"),
    AFST_DTL_ADR("가맹점상세주소"),
    BRNC_ADR("부점주소"),
    AFST_BZN("가맹점사업자등록번호"),
    AMSL_AFST_NO("매출가맹점번호"),
    AFST_TPBCD("가맹점업종코드"),
    BDMN_ITEX_MNGM_NO("예산관리비목관리번호"),
    BDGT_PRFR_RSN_FRCS_CON("예산집행사유코드"),
    BDGT_BSNS_FRCS_CON("사업세부사업");

    final private String name;

    public String getName() {
        return name;
    }

    private SearchType(String name) {
        this.name = name;
    }

}