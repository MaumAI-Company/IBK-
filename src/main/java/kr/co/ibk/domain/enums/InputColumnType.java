package kr.co.ibk.domain.enums;

public enum InputColumnType {
    BRCD("부점코드", "brcd", true),
    CDN("카드번호", "cdn", true),
    BDGT_TSTM_USE_HMS("승인시간", "bdgtTstmUseHms", true),
    AMSL_AMT("매출금액", "amslAmt", true),
    AFST_NM("가맹점명", "afstNm", true),
    TPBS_NM("업종명", "tpbsNm", true),
    BZDY_YN("영업일여부", "bzdyYn", true),
    AFST_DTL_ADR("가맹점상세주소", "afstDtlAdr", true),
    BRNC_ADR("부점주소", "brncAdr", true),
    AFST_BZN("가맹점사업자등록번호", "afstBzn", false),
    AMSL_AFST_NO("매출가맹점번호", "amslAfstNo", false),
    AFST_TPBCD("가맹점업종코드", "afstTpbcd", false);

    final private String name;
    final private String camelColumn;
    final private boolean isChecked;

    public String getName() {
        return name;
    }

    public String getCamelColumn() {
        return camelColumn;
    }
    public boolean getIsChecked() {
        return isChecked;
    }

    private InputColumnType(String name, String camelColumn, boolean isChecked) {
        this.name = name;
        this.camelColumn = camelColumn;
        this.isChecked = isChecked;
    }

}