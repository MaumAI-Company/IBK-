package kr.co.ibk.domain.enums;

public enum InputColumnType {
    BRCD("부점코드", "brcd"),
    CDN("카드번호", "cdn"),
    BDGT_TSTM_USE_HMS("승인시간", "bdgtTstmUseHms"),
    AMSL_AMT("매출금액", "amslAmt"),
    AFST_NM("가맹점명", "afstNm"),
    TPBS_NM("업종명", "tpbsNm"),
    BZDY_YN("영업일여부", "bzdyYn"),
    AFST_DTL_ADR("가맹점상세주소", "afstDtlAdr"),
    BRNC_ADR("부점주소", "brncAdr"),
    AFST_BZN("가맹점사업자등록번호", "afstBzn"),
    AMSL_AFST_NO("매출가맹점번호", "amslAfstNo"),
    AFST_TPBCD("가맹점업종코드", "afstTpbcd");

    final private String name;
    final private String camelColumn;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }

    private InputColumnType(String name, String camelColumn) {
        this.name = name;
        this.camelColumn = camelColumn;
    }

}