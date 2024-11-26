package kr.co.ibk.domain.enums;

public enum SearchType {
    BRCD("부점코드", "1"),
    CDN("카드번호", "2"),
    BDGT_TSTM_USE_HMS("승인시간", "3"),
    AMSL_AMT("매출금액", "4"),
    AFST_NM("가맹점명", "5"),
    TPBS_NM("업종명", "6"),
    BZDY_YN("영업일여부", "7"),
    AFST_DTL_ADR("가맹점상세주소", "8"),
    BRNC_ADR("부점주소", "9"),
    AFST_BZN("가맹점사업자등록번호", "10"),
    AMSL_AFST_NO("매출가맹점번호", "11"),
    AFST_TPBCD("가맹점업종코드", "12"),
    BDMN_ITEX_MNGM_NO("예산관리비목관리번호", "13"),
    BDGT_PRFR_RSN_FRCS_CON("예산집행사유코드", "14"),
    BDGT_BSNS_FRCS_CON("사업세부사업", "15");

    final private String name;
    final private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    private SearchType(String name, String value) {
        this.name = name;
        this.value = value;
    }

}